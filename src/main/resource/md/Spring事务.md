# Spring事务源码分析

比较简单, 做一下笔记

也是`Spring Aop`一些细节的补充, 算是`Spring Aop`的一个例子

## 引入方式

主要有xml和Springboot注解:
- xml: `<tx:annotation-driven/>`
- Springboot注解: `@EnableTransactionManagement`

### 引入方式的源码解析

xml:
`TxNamespaceHandler`中注册了`AnnotationDrivenBeanDefinitionParser`

`AnnotationDrivenBeanDefinitionParser#parse()` 中, 如果`mode="proxy"`则会调用`AopAutoProxyConfigurer.configureAutoProxyCreator(element, parserContext);`, 来看看它的源码

```
public static void configureAutoProxyCreator(Element element, ParserContext parserContext) {
    // 这里会去注册 InfrastructureAdvisorAutoProxyCreator
    AopNamespaceUtils.registerAutoProxyCreatorIfNecessary(parserContext, element);

    String txAdvisorBeanName = TransactionManagementConfigUtils.TRANSACTION_ADVISOR_BEAN_NAME;
    if (!parserContext.getRegistry().containsBeanDefinition(txAdvisorBeanName)) {
        Object eleSource = parserContext.extractSource(element);

        // Create the TransactionAttributeSource definition.
        // 创建TransactionAttributeSource的bean
        RootBeanDefinition sourceDef = new RootBeanDefinition("org.springframework.transaction.annotation.AnnotationTransactionAttributeSource");
        sourceDef.setSource(eleSource);
        sourceDef.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
        // 按spring规则生成beanname
        String sourceName = parserContext.getReaderContext().registerWithGeneratedName(sourceDef);

        // Create the TransactionInterceptor definition.
        // 创建TransactionInterceptor的bean
        RootBeanDefinition interceptorDef = new RootBeanDefinition(TransactionInterceptor.class);
        interceptorDef.setSource(eleSource);
        interceptorDef.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
        registerTransactionManager(element, interceptorDef);
        interceptorDef.getPropertyValues().add("transactionAttributeSource", new RuntimeBeanReference(sourceName));
        String interceptorName = parserContext.getReaderContext().registerWithGeneratedName(interceptorDef);

        // Create the TransactionAttributeSourceAdvisor definition.
        // 创建TransactionAttributeSourceAdvisor的bean
        RootBeanDefinition advisorDef = new RootBeanDefinition(BeanFactoryTransactionAttributeSourceAdvisor.class);
        advisorDef.setSource(eleSource);
        advisorDef.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
        // 将sourcename的bean注入advisorDef的transactionAttributeSource属性中
        advisorDef.getPropertyValues().add("transactionAttributeSource", new RuntimeBeanReference(sourceName));
        // 将TransactionInterceptor的bean注入advisorDef的adviceBeanName属性中
        advisorDef.getPropertyValues().add("adviceBeanName", interceptorName);
        // 配置顺序
        if (element.hasAttribute("order")) {
            advisorDef.getPropertyValues().add("order", element.getAttribute("order"));
        }
        parserContext.getRegistry().registerBeanDefinition(txAdvisorBeanName, advisorDef);
        // 创建CompositeComponentDefinition
        CompositeComponentDefinition compositeDef = new CompositeComponentDefinition(element.getTagName(), eleSource);
        compositeDef.addNestedComponent(new BeanComponentDefinition(sourceDef, sourceName));
        compositeDef.addNestedComponent(new BeanComponentDefinition(interceptorDef, interceptorName));
        compositeDef.addNestedComponent(new BeanComponentDefinition(advisorDef, txAdvisorBeanName));
        parserContext.registerComponent(compositeDef);
    }
}
```
在`configureAutoProxyCreator()`方法中, 注册的这三个bean支撑了整个事务的功能, Springboot自动装配其实也是注册了这三个bean, 让我们来看看

```
@Import(TransactionManagementConfigurationSelector.class)
public @interface EnableTransactionManagement
```
`EnableTransactionManagement`注解引入了`TransactionManagementConfigurationSelector`

`TransactionManagementConfigurationSelector`又引入了`AutoProxyRegistrar`和`ProxyTransactionManagementConfiguration`

`AutoProxyRegistrar`注册了`InfrastructureAdvisorAutoProxyCreator`

`ProxyTransactionManagementConfiguration`注册了`TransactionAttributeSource`, `TransactionInterceptor`, `TransactionAttributeSourceAdvisor`

## 事务代理类生成器InfrastructureAdvisorAutoProxyCreator

`InfrastructureAdvisorAutoProxyCreator`是`AbstractAdvisorAutoProxyCreator`的实现类, 但是代理类的生成用的貌似依旧是`AnnotationAwareAspectJAutoProxyCreator`

这点有待继续追源码

## 寻找事务增强器

和aop一样, 老地方.
```
protected List<Advisor> findEligibleAdvisors(Class<?> beanClass, String beanName) {
    // 寻找候选增强器
    List<Advisor> candidateAdvisors = findCandidateAdvisors();
    // 候选增强器中寻找匹配项
    List<Advisor> eligibleAdvisors = findAdvisorsThatCanApply(candidateAdvisors, beanClass, beanName);
    extendAdvisors(eligibleAdvisors);
    if (!eligibleAdvisors.isEmpty()) {
        eligibleAdvisors = sortAdvisors(eligibleAdvisors);
    }
    return eligibleAdvisors;
}
```

先看如何获得候选增强器, findCandidateAdvisors() 方法最终会调用这个方法 BeanFactoryAdvisorRetrievalHelper.findAdvisorBeans(), 其中就是从beanfactory中查找已经注册的bean
```
public List<Advisor> findAdvisorBeans() {
    // Determine list of advisor bean names, if not cached already.
    String[] advisorNames = null;
    synchronized (this) {
        advisorNames = this.cachedAdvisorBeanNames;
        if (advisorNames == null) {
            // 获得所有注册的 Advisor 的beanname
            advisorNames = BeanFactoryUtils.beanNamesForTypeIncludingAncestors(this.beanFactory, Advisor.class, true, false);
            this.cachedAdvisorBeanNames = advisorNames;
        }
    }
    if (advisorNames.length == 0) {
        return new LinkedList<>();
    }
    List<Advisor> advisors = new LinkedList<>();
    for (String name : advisorNames) {
        if (isEligibleBean(name)) {
            if (this.beanFactory.isCurrentlyInCreation(name)) {
                logger.debug("Skipping currently created advisor '" + name + "'");
            } else {
                // 获得所有 Advisor
                advisors.add(this.beanFactory.getBean(name, Advisor.class));
            }
        }
    }
    return advisors;
}
```

找出对应增强器, 查看是否与目标class及method匹配
```
// AopUtils.findAdvisorsThatCanApply
public static List<Advisor> findAdvisorsThatCanApply(List<Advisor> candidateAdvisors, Class<?> clazz) {
    if (candidateAdvisors.isEmpty()) {
        return candidateAdvisors;
    }
    List<Advisor> eligibleAdvisors = new LinkedList<>();
    // 处理引介增强
    for (Advisor candidate : candidateAdvisors) {
        if (candidate instanceof IntroductionAdvisor && canApply(candidate, clazz)) {
            eligibleAdvisors.add(candidate);
        }
    }
    boolean hasIntroductions = !eligibleAdvisors.isEmpty();
    for (Advisor candidate : candidateAdvisors) {
        if (candidate instanceof IntroductionAdvisor) {
            // 跳过引介增强
            continue;
        }
        // 处理普通增强, 所以主要看着canApply()方法
        if (canApply(candidate, clazz, hasIntroductions)) {
            eligibleAdvisors.add(candidate);
        }
    }
    return eligibleAdvisors;
}
public static boolean canApply(Advisor advisor, Class<?> targetClass, boolean hasIntroductions) {
    if (advisor instanceof IntroductionAdvisor) {
        return ((IntroductionAdvisor) advisor).getClassFilter().matches(targetClass);
    }
    else if (advisor instanceof PointcutAdvisor) {
        // BeanFactoryTransactionAttributeSourceAdvisor 实现了 PointcutAdvisor
        // 所以要继续看重载的canApply()
        PointcutAdvisor pca = (PointcutAdvisor) advisor;
        return canApply(pca.getPointcut(), targetClass, hasIntroductions);
    }
    else {
        // It doesn't have a pointcut so we assume it applies.
        return true;
    }
}
public static boolean canApply(Pointcut pc, Class<?> targetClass, boolean hasIntroductions) {
    if (!pc.getClassFilter().matches(targetClass)) {
        return false;
    }
    // 这个pc是TransactionAttributeSourcePointcut, getMethodMatcher()返回了this
    MethodMatcher methodMatcher = pc.getMethodMatcher();
    if (methodMatcher == MethodMatcher.TRUE) {
        // No need to iterate the methods if we're matching any method anyway...
        return true;
    }
    IntroductionAwareMethodMatcher introductionAwareMethodMatcher = null;
    if (methodMatcher instanceof IntroductionAwareMethodMatcher) {
        introductionAwareMethodMatcher = (IntroductionAwareMethodMatcher) methodMatcher;
    }

    Set<Class<?>> classes = new LinkedHashSet<>();
    if (!Proxy.isProxyClass(targetClass)) {
        // 如果是cglib的代理类, 则加入它的父类
        classes.add(ClassUtils.getUserClass(targetClass));
    }
    // 获得所有父类及接口
    classes.addAll(ClassUtils.getAllInterfacesForClassAsSet(targetClass));
    for (Class<?> clazz : classes) {
        Method[] methods = ReflectionUtils.getAllDeclaredMethods(clazz);
        for (Method method : methods) {
            if ((introductionAwareMethodMatcher != null && introductionAwareMethodMatcher.matches(method, targetClass, hasIntroductions)) ||
                    // 重点
                    methodMatcher.matches(method, targetClass)) {
                return true;
            }
        }
    }
    return false;
}
// TransactionAttributeSourcePointcut.matches()
public boolean matches(Method method, @Nullable Class<?> targetClass) {
    if (targetClass != null && TransactionalProxy.class.isAssignableFrom(targetClass)) {
        return false;
    }
    TransactionAttributeSource tas = getTransactionAttributeSource();
    return (tas == null || tas.getTransactionAttribute(method, targetClass) != null);
}
```

这里要注意一下, 就是这个时候 TransactionAttributeSource 这个参数的实现类是啥, 可以到`getTransactionAttributeSource()`是个接口, 而实现在`BeanFactoryTransactionAttributeSourceAdvisor`里面,

直接返回了它的成员变量`transactionAttributeSource`, 那这成员变量哪来, 就是`BeanFactoryTransactionAttributeSourceAdvisor`的时候注入的`TransactionAttributeSourcePointcut`, 好, 继续追踪代码

```
// AbstractFallbackTransactionAttributeSource.getTransactionAttribute
public TransactionAttribute getTransactionAttribute(Method method, @Nullable Class<?> targetClass) {
    if (method.getDeclaringClass() == Object.class) {
        return null;
    }

    // First, see if we have a cached value.
    Object cacheKey = getCacheKey(method, targetClass);
    Object cached = this.attributeCache.get(cacheKey);
    // 缓存机制
    if (cached != null) {
        // Value will either be canonical value indicating there is no transaction attribute or an actual transaction attribute.
        if (cached == NULL_TRANSACTION_ATTRIBUTE) {
            return null;
        } else {
            return (TransactionAttribute) cached;
        }
    }
    else {
        // We need to work it out.
        // 重点
        TransactionAttribute txAttr = computeTransactionAttribute(method, targetClass);
        // Put it in the cache.
        if (txAttr == null) {
            this.attributeCache.put(cacheKey, NULL_TRANSACTION_ATTRIBUTE);
        } else {
            String methodIdentification = ClassUtils.getQualifiedMethodName(method, targetClass);
            if (txAttr instanceof DefaultTransactionAttribute) {
                ((DefaultTransactionAttribute) txAttr).setDescriptor(methodIdentification);
            }
            logger.debug("Adding transactional method '" + methodIdentification + "' with attribute: " + txAttr);
            this.attributeCache.put(cacheKey, txAttr);
        }
        return txAttr;
    }
}
protected TransactionAttribute computeTransactionAttribute(Method method, @Nullable Class<?> targetClass) {
    // Don't allow no-public methods as required.
    if (allowPublicMethodsOnly() && !Modifier.isPublic(method.getModifiers())) {
        return null;
    }

    // The method may be on an interface, but we need attributes from the target class.
    // If the target class is null, the method will be unchanged.
    Method specificMethod = AopUtils.getMostSpecificMethod(method, targetClass);
    
    // 查看方法中是否存在事务申明
    TransactionAttribute txAttr = findTransactionAttribute(specificMethod);
    if (txAttr != null) {
        return txAttr;
    }
    
    // 查看方法所在的类中是否有事务申明
    txAttr = findTransactionAttribute(specificMethod.getDeclaringClass());
    if (txAttr != null && ClassUtils.isUserLevelMethod(method)) {
        return txAttr;
    }
    
    // 如果存在接口, 到接口中去寻找
    if (specificMethod != method) {
        // 查找接口方法
        txAttr = findTransactionAttribute(method);
        if (txAttr != null) {
            return txAttr;
        }
        // 查找接口所在的类
        txAttr = findTransactionAttribute(method.getDeclaringClass());
        if (txAttr != null && ClassUtils.isUserLevelMethod(method)) {
            return txAttr;
        }
    }
    return null;
}
// 查找事务的属性
// AnnotationTransactionAttributeSource.findTransactionAttribute()
protected TransactionAttribute findTransactionAttribute(Method method) {
    return determineTransactionAttribute(method);
}
protected TransactionAttribute determineTransactionAttribute(AnnotatedElement ae) {
    for (TransactionAnnotationParser annotationParser : this.annotationParsers) {
        TransactionAttribute attr = annotationParser.parseTransactionAnnotation(ae);
        if (attr != null) {
            return attr;
        }
    }
    return null;
}
// SpringTransactionAnnotationParser.parseTransactionAnnotation()
public TransactionAttribute parseTransactionAnnotation(AnnotatedElement element) {
    AnnotationAttributes attributes = AnnotatedElementUtils.findMergedAnnotationAttributes(
            element, Transactional.class, false, false);
    if (attributes != null) {
        return parseTransactionAnnotation(attributes);
    }
    else {
        return null;
    }
}
```

查找属性用的`this.annotationParsers`是当前类`AnnotationTransactionAttributeSource` 初始化的时候初始化的, 可看其构造方法, 一般都是`SpringTransactionAnnotationParser`

大段大段的源码之后, 终于看到了我们常用的`Transactional.class`, 之后就是在parseTransactionAnnotation()这方法中解析参数, 就不分析了.

## TransactionInterceptor的引入

在`JdkDynamicAopProxy.invoke()`方法中, 有这样一行代码:

`List<Object> chain = this.advised.getInterceptorsAndDynamicInterceptionAdvice(method, targetClass);`

这里的`advised`就是`ProxyFacory`

这行代码, 通过`AnnotationTransactionAttributeSource`获得了`TransactionInterceptor`

看其实现:














