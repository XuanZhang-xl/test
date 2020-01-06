# Spring事务源码分析

比较简单, 做一下笔记

也是`Spring Aop`一些细节的补充, 算是`Spring Aop`的一个例子

Spring 版本 5.1.4.RELEASE

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
        // 这里注册了TransactionManager 默认的beanname为 transactionManager
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
```
public List<Object> getInterceptorsAndDynamicInterceptionAdvice(Method method, @Nullable Class<?> targetClass) {
    MethodCacheKey cacheKey = new MethodCacheKey(method);
    // 先从缓存里拿, 如果Advice变了, 缓存会清空
    List<Object> cached = this.methodCache.get(cacheKey);
    if (cached == null) {
        // 重点, 这里穿了个this, 就是把ProxyFacory传进去
        cached = this.advisorChainFactory.getInterceptorsAndDynamicInterceptionAdvice(this, method, targetClass);
        this.methodCache.put(cacheKey, cached);
    }
    return cached;
}
public List<Object> getInterceptorsAndDynamicInterceptionAdvice(Advised config, Method method, @Nullable Class<?> targetClass) {
    // 实现类: DefaultAdvisorAdapterRegistry
    AdvisorAdapterRegistry registry = GlobalAdvisorAdapterRegistry.getInstance();
    // 拿到 Advisor
    Advisor[] advisors = config.getAdvisors();
    List<Object> interceptorList = new ArrayList<>(advisors.length);
    Class<?> actualClass = (targetClass != null ? targetClass : method.getDeclaringClass());
    Boolean hasIntroductions = null;
    for (Advisor advisor : advisors) {
        // BeanFactoryTransactionAttributeSourceAdvisor 是 PointcutAdvisor
        if (advisor instanceof PointcutAdvisor) {
            PointcutAdvisor pointcutAdvisor = (PointcutAdvisor) advisor;
            // getClassFilter() 默认为 TrueClassFilter, 直接返回了true,  其实之前获取增强器的时候已经调用过了
            if (config.isPreFiltered() || pointcutAdvisor.getPointcut().getClassFilter().matches(actualClass)) {
                // 返回 Pointcut 本身 就是 TransactionAttributeSourcePointcut
                MethodMatcher mm = pointcutAdvisor.getPointcut().getMethodMatcher();
                boolean match;
                // 所以这里是false
                if (mm instanceof IntroductionAwareMethodMatcher) {
                    if (hasIntroductions == null) {
                        hasIntroductions = hasMatchingIntroductions(advisors, actualClass);
                    }
                    match = ((IntroductionAwareMethodMatcher) mm).matches(method, actualClass, hasIntroductions);
                }
                else {
                    // 上面已经分析过了, 只要启动之后没改过增强器, 返回true
                    match = mm.matches(method, actualClass);
                }
                if (match) {
                    // 重点
                    MethodInterceptor[] interceptors = registry.getInterceptors(advisor);
                    if (mm.isRuntime()) {
                        for (MethodInterceptor interceptor : interceptors) {
                            interceptorList.add(new InterceptorAndDynamicMethodMatcher(interceptor, mm));
                        }
                    } else {
                        interceptorList.addAll(Arrays.asList(interceptors));
                    }
                }
            }
        } else if (advisor instanceof IntroductionAdvisor) {
            IntroductionAdvisor ia = (IntroductionAdvisor) advisor;
            if (config.isPreFiltered() || ia.getClassFilter().matches(actualClass)) {
                Interceptor[] interceptors = registry.getInterceptors(advisor);
                interceptorList.addAll(Arrays.asList(interceptors));
            }
        } else {
            Interceptor[] interceptors = registry.getInterceptors(advisor);
            interceptorList.addAll(Arrays.asList(interceptors));
        }
    }
    return interceptorList;
}
// DefaultAdvisorAdapterRegistry.getInterceptors()
public MethodInterceptor[] getInterceptors(Advisor advisor) throws UnknownAdviceTypeException {
    List<MethodInterceptor> interceptors = new ArrayList<>(3);
    // 重点, 获得拦截器, 这里直接得到了TransactionIntercrptor
    Advice advice = advisor.getAdvice();
    // TransactionIntercrptor is kind of MethodInterceptor
    if (advice instanceof MethodInterceptor) {
        interceptors.add((MethodInterceptor) advice);
    }
    for (AdvisorAdapter adapter : this.adapters) {
        if (adapter.supportsAdvice(advice)) {
            interceptors.add(adapter.getInterceptor(advisor));
        }
    }
    if (interceptors.isEmpty()) {
        throw new UnknownAdviceTypeException(advisor.getAdvice());
    }
    return interceptors.toArray(new MethodInterceptor[0]);
}
// AbstractBeanFactoryPointcutAdvisor.getAdvice
public Advice getAdvice() {
    Advice advice = this.advice;
    if (advice != null) {
        return advice;
    }
    if (this.beanFactory.isSingleton(this.adviceBeanName)) {
        // 还记得在最开始初始化的时候 把 TransactionInterceptor 注入进了 BeanFactoryTransactionAttributeSourceAdvisor 的 adviceBeanName 这个成员变量么?
        // advisorDef.getPropertyValues().add("adviceBeanName", interceptorName); <---就是这行代码
        advice = this.beanFactory.getBean(this.adviceBeanName, Advice.class);
        this.advice = advice;
        return advice;
    } else {
        synchronized (this.adviceMonitor) {
            advice = this.advice;
            if (advice == null) {
                advice = this.beanFactory.getBean(this.adviceBeanName, Advice.class);
                this.advice = advice;
            }
            return advice;
        }
    }
}
```
至此, 我们终于获得了事务拦截器`TransactionInterceptor`, 可以在其中的`invoke()`方法中进行事务相关的操作, 来看看它是怎么实现的

## 事务增强器TransactionInterceptor

在分析`TransactionInterceptor`之前有些概念需要提前理清或复习

1.先来复习以下七种事务传播行为的特性

|隔离级别|描述|应用|
|---|---|---|
|PROPAGATION_REQUIRED|默认的spring事务传播级别，如果上下文中已经存在事务，那么就加入到事务中执行，如果当前上下文中不存在事务，则新建事务执行。|这个级别通常能满足处理大多数的业务场景|
|PROPAGATION_SUPPORTS|如果上下文存在事务，则支持事务加入事务，如果没有事务，则使用非事务的方式执行。所以说，并非所有的包在transactionTemplate.execute中的代码都会有事务支持。|这个通常是用来处理那些并非原子性的非核心业务逻辑操作。应用场景较少。|
|PROPAGATION_MANDATORY|该级别的事务要求上下文中必须要存在事务，否则就会抛出异常|配置该方式的传播级别是有效的控制上下文调用代码遗漏添加事务控制的保证手段。比如一段代码不能单独被调用执行，但是一旦被调用，就必须有事务包含的情况，就可以使用这个传播级别。|
|PROPAGATION_REQUIRES_NEW|每次都要一个新事务，该传播级别的特点是，每次都会新建一个事务，并且同时将上下文中的事务挂起，执行当前新建事务完成以后，上下文事务恢复再执行。|举一个应用场景：现在有一个发送100个红包的操作，在发送之前，要做一些系统的初始化、验证、数据记录操作，然后发送100封红包，然后再记录发送日志，发送日志要求100%的准确，如果日志不准确，那么整个父事务逻辑需要回滚。发送红包的子事务不会直接影响到父事务的提交和回滚。|
|PROPAGATION_NOT_SUPPORTED|特点就是上下文中存在事务，则挂起事务，以非事务方式执行当前逻辑，结束后恢复上下文的事务。|可以帮助你将事务极可能的缩小。我们知道一个事务越大，它存在的风险也就越多。所以在处理事务的过程中，要保证尽可能的缩小范围。比如一段代码，是每次逻辑操作都必须调用的，比如循环1000次的某个非核心业务逻辑操作。这样的代码如果包在事务中，势必造成事务太大，导致出现一些难以考虑周全的异常情况。所以这个事务这个级别的传播级别就派上用场了。用当前级别的事务模板抱起来就可以了。|
|PROPAGATION_NEVER|要求上下文中不能存在事务，一旦有事务，就抛出runtime异常||
|PROPAGATION_NESTED|嵌套级别事务。该传播级别特征是，如果上下文中存在事务，则嵌套事务执行，如果不存在事务，则新建事务。和`PROPAGATION_REQUIRES_NEW`很像, 但是它支持嵌入式事务, Spring中允许嵌入式事务的时候, 则首选设置保存点的方式作为异常处理的回滚, 如果不支持保存点,比如`JTA`, 则与`PROPAGATION_REQUIRES_NEW`无异||

2.autocommit自动提交功能
- 关闭自动提交`autocommit=0`，事务则在用户本次对数据进行操作时自动开启，在用户执行commit命令时提交，用户本次对数据库开始进行操作到用户执行`commit`命令之间的一系列操作为一个完整的事务周期。若不执行`commit`命令，系统则默认事务回滚。总而言之，当前情况下事务的状态是需要手动去提交。
- 开启自动提交`autocommit=1`（系统默认值）, 则事务的开启与提交又分为两种状态：
    - 当用户执行`start transaction`命令时（事务初始化），一个事务开启，当用户执行`commit`命令时当前事务提交。从用户执行`start transaction`命令到用户执行`commit`命令之间的一系列操作为一个完整的事务周期。若不执行`commit`命令，系统则默认事务回滚。
    - 如果用户在当前情况下（参数autocommit=1）未执行`start transaction`命令而对数据库进行了操作，系统则默认用户对数据库的每一个操作为一个孤立的事务，也就是说用户每进行一次操作系都会即时提交或者即时回滚。这种情况下用户的每一个操作都是一个完整的事务周期。

其实这里还有疑问TODO, 比如
    - 开启自动提交的情况下, 显式开启事务`start transaction`, 执行了几条sql之后, 执行`set autocommit=0`, 那么之前执行的几条sql怎么办? 提交还是回滚还是啥也不做, 继续等待`commit`命令?
    - 同理,关闭自动提交的情况下, 执行了几条sql之后, 执行`set autocommit=1`, 那么之前执行的几条sql怎么办? 提交还是回滚还是啥也不做, 继续等待`commit`命令?

`TransactionInterceptor`支撑着整个事务功能的架构, 逻辑相对复杂, 其继承了`MethodInterceptor`, 那么就从`invoke()`方法开始分析

```
public Object invoke(MethodInvocation invocation) throws Throwable {
    // Work out the target class: may be {@code null}.
    // The TransactionAttributeSource should be passed the target class
    // as well as the method, which may be from an interface.
    Class<?> targetClass = (invocation.getThis() != null ? AopUtils.getTargetClass(invocation.getThis()) : null);

    // Adapt to TransactionAspectSupport's invokeWithinTransaction...
    return invokeWithinTransaction(invocation.getMethod(), targetClass, invocation::proceed);
}
protected Object invokeWithinTransaction(Method method, @Nullable Class<?> targetClass, final InvocationCallback invocation) throws Throwable {

    // If the transaction attribute is null, the method is non-transactional.
    TransactionAttributeSource tas = getTransactionAttributeSource();
    // 获得事务属性
    final TransactionAttribute txAttr = (tas != null ? tas.getTransactionAttribute(method, targetClass) : null);
    // 在初始化的时候注册了TransactionManager的beaname, 会根据这个beanname在beanFactory找TransactionManager,
    final PlatformTransactionManager tm = determineTransactionManager(txAttr);
    // 构造方法唯一标识(类.方法. 如 service.UserServiceImpl.save)
    // Convenience method to return a String representation of this Method for use in logging  好吧, 就是用于记日志的
    final String joinpointIdentification = methodIdentification(method, targetClass, txAttr);
    // 声明式事务
    if (txAttr == null || !(tm instanceof CallbackPreferringPlatformTransactionManager)) {
        // Standard transaction demarcation with getTransaction and commit/rollback calls.
        // TransactionInfo 封装了下 TransactionAttribute  PlatformTransactionManager  joinpointIdentification 和事务状态
        // 重点 这里创建了事务, 所以拿到了事务状态 TransactionStatus
        TransactionInfo txInfo = createTransactionIfNecessary(tm, txAttr, joinpointIdentification);
        Object retVal = null;
        try {
            // This is an around advice: Invoke the next interceptor in the chain.
            // This will normally result in a target object being invoked.
            retVal = invocation.proceedWithInvocation();
        } catch (Throwable ex) {
            // target invocation exception 异常回滚
            completeTransactionAfterThrowing(txInfo, ex);
            throw ex;
        } finally {
            // 清除信息
            cleanupTransactionInfo(txInfo);
        }
        // 事务提交
        commitTransactionAfterReturning(txInfo);
        return retVal;
    } else {
        final ThrowableHolder throwableHolder = new ThrowableHolder();
        // 编程式事务
        // It's a CallbackPreferringPlatformTransactionManager: pass a TransactionCallback in.
        try {
            Object result = ((CallbackPreferringPlatformTransactionManager) tm).execute(txAttr, status -> {
                TransactionInfo txInfo = prepareTransactionInfo(tm, txAttr, joinpointIdentification, status);
                try {
                    return invocation.proceedWithInvocation();
                } catch (Throwable ex) {
                    if (txAttr.rollbackOn(ex)) {
                        // A RuntimeException: will lead to a rollback.
                        if (ex instanceof RuntimeException) {
                            throw (RuntimeException) ex;
                        } else {
                            throw new ThrowableHolderException(ex);
                        }
                    } else {
                        // A normal return value: will lead to a commit.
                        throwableHolder.throwable = ex;
                        return null;
                    }
                } finally {
                    cleanupTransactionInfo(txInfo);
                }
            });

            // Check result state: It might indicate a Throwable to rethrow.
            if (throwableHolder.throwable != null) {
                throw throwableHolder.throwable;
            }
            return result;
        } catch (ThrowableHolderException ex) {
            throw ex.getCause();
        } catch (TransactionSystemException ex2) {
            if (throwableHolder.throwable != null) {
                logger.error("Application exception overridden by commit exception", throwableHolder.throwable);
                ex2.initApplicationException(throwableHolder.throwable);
            }
            throw ex2;
        } catch (Throwable ex2) {
            if (throwableHolder.throwable != null) {
                logger.error("Application exception overridden by commit exception", throwableHolder.throwable);
            }
            throw ex2;
        }
    }
}
```
上面代码的重点就是`TransactionAspectSupport.createTransactionIfNecessary()`, 这行代码创建了事务.

### 事务的创建

事务的创建其实就是获得 `TransactionInfo`的过程. 其封装了事务的所有信息.

直接看`TransactionAspectSupport.createTransactionIfNecessary()`中的方法`getTransaction()`
```
// AbstractPlatformTransactionManager.getTransaction()  TransactionDefinition参数就是TransactionAttribute
public final TransactionStatus getTransaction(@Nullable TransactionDefinition definition) throws TransactionException {
    // 重点 获得事务
    Object transaction = doGetTransaction();

    if (definition == null) {
        // Use defaults if no transaction definition given.
        definition = new DefaultTransactionDefinition();
    }

    // 判断当前线程是否存在事务, 判断依据为当前线程数据库连接不为空且连接为激活状态
    if (isExistingTransaction(transaction)) {
        // 当前线程存在事务, 检查事务传播行为以决定当前行为
        return handleExistingTransaction(definition, transaction, debugEnabled);
    }
    // 下面都是创建新事务时的逻辑
    
    // 事务超时验证
    if (definition.getTimeout() < TransactionDefinition.TIMEOUT_DEFAULT) {
        throw new InvalidTimeoutException("Invalid transaction timeout", definition.getTimeout());
    }

    if (definition.getPropagationBehavior() == TransactionDefinition.PROPAGATION_MANDATORY) {
        // 当前不存在事务, 且传播行为为PROPAGATION_MANDATORY, 则报异常
        throw new IllegalTransactionStateException( "No existing transaction found for transaction marked with propagation 'mandatory'");
    } else if (definition.getPropagationBehavior() == TransactionDefinition.PROPAGATION_REQUIRED ||
            definition.getPropagationBehavior() == TransactionDefinition.PROPAGATION_REQUIRES_NEW ||
            definition.getPropagationBehavior() == TransactionDefinition.PROPAGATION_NESTED) {
        // 这三都是创建新事物
        // 由于当前没有事务, 挂起空事务
        SuspendedResourcesHolder suspendedResources = suspend(null);
        try {
            boolean newSynchronization = (getTransactionSynchronization() != SYNCHRONIZATION_NEVER);
            DefaultTransactionStatus status = newTransactionStatus( definition, transaction, true, newSynchronization, debugEnabled, suspendedResources);
            // 重点 设置transaction
            // 如果是新事务, 则绑定到当前线程
            doBegin(transaction, definition);
            // 将事务信息记录在当前线程中
            prepareSynchronization(status, definition);
            return status;
        } catch (RuntimeException | Error ex) {
            // 还原空事务
            resume(null, suspendedResources);
            throw ex;
        }
    } else {
        // PROPAGATION_SUPPORTS   PROPAGATION_NOT_SUPPORTED  PROPAGATION_NEVER 这三种传播行为都是当前没事务的话以非事务方式执行
        if (definition.getIsolationLevel() != TransactionDefinition.ISOLATION_DEFAULT && logger.isWarnEnabled()) {
            logger.warn("Custom isolation level specified but no actual transaction initiated; " + "isolation level will effectively be ignored: " + definition);
        }
        boolean newSynchronization = (getTransactionSynchronization() == SYNCHRONIZATION_ALWAYS);
        return prepareTransactionStatus(definition, null, true, newSynchronization, debugEnabled, null);
    }
}
protected Object doGetTransaction() {
    // 事务相关封装类
    DataSourceTransactionObject txObject = new DataSourceTransactionObject();
    // 保存点的设置
    txObject.setSavepointAllowed(isNestedTransactionAllowed());
    // 如果当前线程已经记录了数据库连接, 则从ThreadLocal中拿到原连接
    ConnectionHolder conHolder = (ConnectionHolder) TransactionSynchronizationManager.getResource(obtainDataSource());
    // 绑定, false表示非新建连接
    txObject.setConnectionHolder(conHolder, false);
    return txObject;
}
```

现在我们已经获得了事务, 接下来就是根据事务的传播行为已确定接下来的逻辑

对于 隔离级别, timeout等功能的设置是委托给底层的数据库连接去做, 对于数据库连接的设置就是在`doBegin()`函数中处理的

```
//DataSourceTransactionManager.doBegin
protected void doBegin(Object transaction, TransactionDefinition definition) {
    DataSourceTransactionObject txObject = (DataSourceTransactionObject) transaction;
    Connection con = null;
    try {
        if (!txObject.hasConnectionHolder() || txObject.getConnectionHolder().isSynchronizedWithTransaction()) {
            // 获得连接  条件是 本来没有连接, 或   TODO: 事务同步表示为true 
            Connection newCon = obtainDataSource().getConnection();
            logger.debug("Acquired Connection [" + newCon + "] for JDBC transaction");
            txObject.setConnectionHolder(new ConnectionHolder(newCon), true);
        }
        txObject.getConnectionHolder().setSynchronizedWithTransaction(true);
        con = txObject.getConnectionHolder().getConnection();
        // 设置隔离级别
        Integer previousIsolationLevel = DataSourceUtils.prepareConnectionForTransaction(con, definition);
        txObject.setPreviousIsolationLevel(previousIsolationLevel);
        // 更改自动提交设置, 由spring控制提交
        if (con.getAutoCommit()) {
            txObject.setMustRestoreAutoCommit(true);
            con.setAutoCommit(false);
        }
        // 设置读事务 SET TRANSACTION READ ONLY
        prepareTransactionalConnection(con, definition);
        // 设置判断当前线程是否存在事务的依据
        txObject.getConnectionHolder().setTransactionActive(true);
        // 设置超时时间
        int timeout = determineTimeout(definition);
        if (timeout != TransactionDefinition.TIMEOUT_DEFAULT) {
            txObject.getConnectionHolder().setTimeoutInSeconds(timeout);
        }
        // 如果是新连接, 则把连接与当前线程绑定
        if (txObject.isNewConnectionHolder()) {
            TransactionSynchronizationManager.bindResource(obtainDataSource(), txObject.getConnectionHolder());
        }
    } catch (Throwable ex) {
        if (txObject.isNewConnectionHolder()) {
            DataSourceUtils.releaseConnection(con, obtainDataSource());
            txObject.setConnectionHolder(null, false);
        }
        throw new CannotCreateTransactionException("Could not open JDBC Connection for transaction", ex);
    }
}
```

可以说事务是从`doBegin()`这个函数开始的, 里面还有些细节有拿出来看的价值, 比如如何设置隔离级别?
```
public static Integer prepareConnectionForTransaction(Connection con, @Nullable TransactionDefinition definition) throws SQLException {
    // 是否只读
    if (definition != null && definition.isReadOnly()) {
        try {
            con.setReadOnly(true);
        } catch (SQLException | RuntimeException ex) {
            // 超时异常抛出, 其他无视
        }
    }
    // 设置连接的隔离级别
    Integer previousIsolationLevel = null;
    if (definition != null && definition.getIsolationLevel() != TransactionDefinition.ISOLATION_DEFAULT) {
        logger.debug("Changing isolation level of JDBC Connection [" + con + "] to " + definition.getIsolationLevel());
        int currentIsolation = con.getTransactionIsolation();
        if (currentIsolation != definition.getIsolationLevel()) {
            previousIsolationLevel = currentIsolation;
            con.setTransactionIsolation(definition.getIsolationLevel());
        }
    }
    return previousIsolationLevel;
}
```

以上是新建事务的情况, 那么在看看当前存在事务的情况
```
// AbstractPlatformTransactionManager.handleExistingTransaction
private TransactionStatus handleExistingTransaction(TransactionDefinition definition, Object transaction, boolean debugEnabled) throws TransactionException {
    // 传播行为为 永远不适用事务, 抛异常
    if (definition.getPropagationBehavior() == TransactionDefinition.PROPAGATION_NEVER) {
        throw new IllegalTransactionStateException("Existing transaction found for transaction marked with propagation 'never'");
    }

    if (definition.getPropagationBehavior() == TransactionDefinition.PROPAGATION_NOT_SUPPORTED) {
        // 有事务, 则挂起事务, 以非事务方式执行
        Object suspendedResources = suspend(transaction);
        boolean newSynchronization = (getTransactionSynchronization() == SYNCHRONIZATION_ALWAYS);
        return prepareTransactionStatus(definition, null, false, newSynchronization, debugEnabled, suspendedResources);
    }
    if (definition.getPropagationBehavior() == TransactionDefinition.PROPAGATION_REQUIRES_NEW) {
        // 挂起当前事务, 并新建事务
        SuspendedResourcesHolder suspendedResources = suspend(transaction);
        try {
            boolean newSynchronization = (getTransactionSynchronization() != SYNCHRONIZATION_NEVER);
            DefaultTransactionStatus status = newTransactionStatus(definition, transaction, true, newSynchronization, debugEnabled, suspendedResources);
            doBegin(transaction, definition);
            prepareSynchronization(status, definition);
            return status;
        } catch (RuntimeException | Error beginEx) {
            resumeAfterBeginException(transaction, suspendedResources, beginEx);
            throw beginEx;
        }
    }
    // 嵌入式事务处理
    if (definition.getPropagationBehavior() == TransactionDefinition.PROPAGATION_NESTED) {
        if (!isNestedTransactionAllowed()) {
            throw new NestedTransactionNotSupportedException("Transaction manager does not allow nested transactions by default - " + "specify 'nestedTransactionAllowed' property with value 'true'");
        }
        logger.debug("Creating nested transaction with name [" + definition.getName() + "]");
        if (useSavepointForNestedTransaction()) {
            // Create savepoint within existing Spring-managed transaction,
            // through the SavepointManager API implemented by TransactionStatus.
            // Usually uses JDBC 3.0 savepoints. Never activates Spring synchronization.
            // 如果没有可以使用保存点的方式控制事务回滚, 那么在嵌入式事务的建立初始建立保存点
            DefaultTransactionStatus status = prepareTransactionStatus(definition, transaction, false, false, debugEnabled, null);
            status.createAndHoldSavepoint();
            return status;
        } else {
            // Nested transaction through nested begin and commit/rollback calls.
            // Usually only for JTA: Spring synchronization might get activated here
            // in case of a pre-existing JTA transaction.
            // 不能使用保存点的情况, 如JTA, 那么建立新事务
            boolean newSynchronization = (getTransactionSynchronization() != SYNCHRONIZATION_NEVER);
            DefaultTransactionStatus status = newTransactionStatus( definition, transaction, true, newSynchronization, debugEnabled, null);
            doBegin(transaction, definition);
            prepareSynchronization(status, definition);
            return status;
        }
    }

    // Assumably PROPAGATION_SUPPORTS or PROPAGATION_REQUIRED.
    // 沿用当前事务
    logger.debug("Participating in existing transaction");
    if (isValidateExistingTransaction()) {
        if (definition.getIsolationLevel() != TransactionDefinition.ISOLATION_DEFAULT) {
            Integer currentIsolationLevel = TransactionSynchronizationManager.getCurrentTransactionIsolationLevel();
            if (currentIsolationLevel == null || currentIsolationLevel != definition.getIsolationLevel()) {
                Constants isoConstants = DefaultTransactionDefinition.constants;
                throw new IllegalTransactionStateException("Participating transaction with definition [" + definition + "] specifies isolation level which is incompatible with existing transaction: " +
                        (currentIsolationLevel != null ? isoConstants.toCode(currentIsolationLevel, DefaultTransactionDefinition.PREFIX_ISOLATION) : "(unknown)"));
            }
        }
        if (!definition.isReadOnly()) {
            if (TransactionSynchronizationManager.isCurrentTransactionReadOnly()) {
                throw new IllegalTransactionStateException("Participating transaction with definition [" + definition + "] is not marked as read-only but existing transaction is");
            }
        }
    }
    boolean newSynchronization = (getTransactionSynchronization() != SYNCHRONIZATION_NEVER);
    return prepareTransactionStatus(definition, transaction, false, newSynchronization, debugEnabled, null);
}
```

这里解释下什么是挂起事务, 就不分析代码了, 挂起操作的主要操作是记录原有事务的状态, 使其与当前线程解绑, 其目的是方便后续操作对事务的恢复.

获得了事务状态后, 就是封装事务信息. 这个实例包含了目标方法开始前的所有状态信息, 一旦事务执行失败,Spring会通过TransactionInfo的实例中的信息来进行回滚等后续操作.
```
protected TransactionInfo prepareTransactionInfo(@Nullable PlatformTransactionManager tm, @Nullable TransactionAttribute txAttr, String joinpointIdentification, @Nullable TransactionStatus status) {
    // 封装事务信息
    TransactionInfo txInfo = new TransactionInfo(tm, txAttr, joinpointIdentification);
    if (txAttr != null) {
        // 记录事务状态
        txInfo.newTransactionStatus(status);
    } else {
        logger.trace("Don't need to create transaction for [" + joinpointIdentification + "]: This method isn't transactional.");

    }
    // 绑定到当前线程
    txInfo.bindToThread();
    return txInfo;
}
```

之前的操作已经完成了目标方法运行前的准备工作, 这些准备工作的最大目的就是准备 如果程序没有按我们期待的那样运行, 也就是出现错误的时候的处理工作, 所以来看看如果出现错误, Spring的处理方式

### 事务错误处理

```
// TransactionAspectSupport.completeTransactionAfterThrowing()
protected void completeTransactionAfterThrowing(@Nullable TransactionInfo txInfo, Throwable ex) {
    // 首先判断是否存在事务
    if (txInfo != null && txInfo.getTransactionStatus() != null) {
        logger.trace("Completing transaction for [" + txInfo.getJoinpointIdentification() + "] after exception: " + ex);
        // 回滚的 异常是 RuntimeException或Error
        if (txInfo.transactionAttribute != null && txInfo.transactionAttribute.rollbackOn(ex)) {
            try {
                // 回滚操作
                txInfo.getTransactionManager().rollback(txInfo.getTransactionStatus());
            } catch (TransactionSystemException ex2) {
                logger.error("Application exception overridden by rollback exception", ex);
                ex2.initApplicationException(ex);
                throw ex2;
            } catch (RuntimeException | Error ex2) {
                logger.error("Application exception overridden by rollback exception", ex);
                throw ex2;
            }
        } else {
            // 不满足回滚条件, 则提交
            try {
                txInfo.getTransactionManager().commit(txInfo.getTransactionStatus());
            } catch (TransactionSystemException ex2) {
                logger.error("Application exception overridden by commit exception", ex);
                ex2.initApplicationException(ex);
                throw ex2;
            } catch (RuntimeException | Error ex2) {
                logger.error("Application exception overridden by commit exception", ex);
                throw ex2;
            }
        }
    }
}
```

默认情况下, Spring 只对 `RuntimeException`和`Error` 进行回滚

我们可以通过 `@Transaction`注解的
```
DefaultTransactionAttribute.rollbackOn
public boolean rollbackOn(Throwable ex) {
    return (ex instanceof RuntimeException || ex instanceof Error);
}
```

而符合回滚条件的事务, 会执行`rollback()`方法
```
public final void rollback(TransactionStatus status) throws TransactionException {
    if (status.isCompleted()) {
        throw new IllegalTransactionStateException("Transaction is already completed - do not call commit or rollback more than once per transaction");
    }
    DefaultTransactionStatus defStatus = (DefaultTransactionStatus) status;
    processRollback(defStatus, false);
}
private void processRollback(DefaultTransactionStatus status, boolean unexpected) {
    try {
        boolean unexpectedRollback = unexpected;
        try {
            // 激活 TransactionSynchronization 中对应的方法
            triggerBeforeCompletion(status);

            if (status.hasSavepoint()) {
                // 有保存点, 退回到保存点
                status.rollbackToHeldSavepoint();
            } else if (status.isNewTransaction()) {
                // 新事务, 直接退回
                doRollback(status);
            } else {
                // Participating in larger transaction
                // 当前事务不是独立事务, 而是分布式事务, 等到事务链执行完毕后统一回滚
                if (status.hasTransaction()) {
                    if (status.isLocalRollbackOnly() || isGlobalRollbackOnParticipationFailure()) {
                        if (status.isDebug()) {
                            logger.debug("Participating transaction failed - marking existing transaction as rollback-only");
                        }
                        doSetRollbackOnly(status);
                    } else {
                        if (status.isDebug()) {
                            logger.debug("Participating transaction failed - letting transaction originator decide on rollback");
                        }
                    }
                } else {
                    logger.debug("Should roll back transaction but cannot - no transaction available");
                }
                // Unexpected rollback only matters here if we're asked to fail early
                if (!isFailEarlyOnGlobalRollbackOnly()) {
                    unexpectedRollback = false;
                }
            }
        } catch (RuntimeException | Error ex) {
            triggerAfterCompletion(status, TransactionSynchronization.STATUS_UNKNOWN);
            throw ex;
        }

        // 激活 TransactionSynchronization 中对应的方法
        triggerAfterCompletion(status, TransactionSynchronization.STATUS_ROLLED_BACK);

        // Raise UnexpectedRollbackException if we had a global rollback-only marker
        if (unexpectedRollback) {
            throw new UnexpectedRollbackException("Transaction rolled back because it has been marked as rollback-only");
        }
    } finally {
        // 清空记录并将挂起的资源恢复
        cleanupAfterCompletion(status);
    }
}
```
其中有`triggerBeforeCompletion()` 和 `triggerAfterCompletion`都是去执行`TransactionSynchronization`事务监听器中对应的方法

最后看看清空记录到底是干了什么

```
private void cleanupAfterCompletion(DefaultTransactionStatus status) {
    // 设置事务状态为完成
    status.setCompleted();
    // 如果是新的同步状态, 则清除绑定到当前线程的事务信息
    if (status.isNewSynchronization()) {
        TransactionSynchronizationManager.clear();
    }
    // 如果是新事务则做些清除资源的工作
    if (status.isNewTransaction()) {
        doCleanupAfterCompletion(status.getTransaction());
    }
    if (status.getSuspendedResources() != null) {
        if (status.isDebug()) {
            logger.debug("Resuming suspended transaction after completion of inner transaction");
        }
        // 如果有事务是挂起状态, 那么恢复该事务
        Object transaction = (status.hasTransaction() ? status.getTransaction() : null);
        resume(transaction, (SuspendedResourcesHolder) status.getSuspendedResources());
    }
}

```

TODO: 这里有一个判断是否是新同步状态, 这是什么意义? `status.isNewSynchronization()`

```
// DataSourceTransactionManager.doCleanupAfterCompletion()
protected void doCleanupAfterCompletion(Object transaction) {
    DataSourceTransactionObject txObject = (DataSourceTransactionObject) transaction;

    // 将连接从当前线程解绑
    if (txObject.isNewConnectionHolder()) {
        TransactionSynchronizationManager.unbindResource(obtainDataSource());
    }

    // 释放连接
    Connection con = txObject.getConnectionHolder().getConnection();
    try {
        // 恢复自动提交
        if (txObject.isMustRestoreAutoCommit()) {
            con.setAutoCommit(true);
        }
        // 恢复数据库连接的隔离别
        DataSourceUtils.resetConnectionAfterTransaction(con, txObject.getPreviousIsolationLevel());
    } catch (Throwable ex) {
        logger.debug("Could not reset JDBC Connection after transaction", ex);
    }
    // 如果是新连接, 则释放连接
    if (txObject.isNewConnectionHolder()) {
        logger.debug("Releasing JDBC Connection [" + con + "] after transaction");
        DataSourceUtils.releaseConnection(con, this.dataSource);
    }
    // 清除连接的引用
    txObject.getConnectionHolder().clear();
}
```
### 事务提交

上面分析了异常处理机制, 如果事务执行没有出现异常, 那么走正常的事务提交流程:
```
protected void commitTransactionAfterReturning(@Nullable TransactionInfo txInfo) {
    // 有事务才处理
    // TransactionAspectSupport.commitTransactionAfterReturning
    if (txInfo != null && txInfo.getTransactionStatus() != null) {
        logger.trace("Completing transaction for [" + txInfo.getJoinpointIdentification() + "]");
        txInfo.getTransactionManager().commit(txInfo.getTransactionStatus());
    }
}
/ AbstractPlatformTransactionManager.commit
public final void commit(TransactionStatus status) throws TransactionException {
    if (status.isCompleted()) {
        throw new IllegalTransactionStateException("Transaction is already completed - do not call commit or rollback more than once per transaction");
    }
    // 事务已经被标记回滚, 则执行回滚逻辑
    DefaultTransactionStatus defStatus = (DefaultTransactionStatus) status;
    if (defStatus.isLocalRollbackOnly()) {
        logger.debug("Transactional code has requested rollback");
        processRollback(defStatus, false);
        return;
    }
    // JTA回滚全局处理
    if (!shouldCommitOnGlobalRollbackOnly() && defStatus.isGlobalRollbackOnly()) {
        logger.debug("Global transaction is marked as rollback-only but transactional code requested commit");
        processRollback(defStatus, true);
        return;
    }
    // 重点
    processCommit(defStatus);
}
```

可以看到当事务没有抛异常也不一定会提交, 当执行到processCommit()才是真正的提交流程
```
private void processCommit(DefaultTransactionStatus status) throws TransactionException {
    try {
        boolean beforeCompletionInvoked = false;
        try {
            boolean unexpectedRollback = false;
            // 预留接口
            prepareForCommit(status);
            // 调用TransactionSynchronization中对应方法
            triggerBeforeCommit(status);
            // 调用TransactionSynchronization中对应方法
            triggerBeforeCompletion(status);
            beforeCompletionInvoked = true;

            if (status.hasSavepoint()) {
                // 清除保存点信息, 且不提交事务
                unexpectedRollback = status.isGlobalRollbackOnly();
                status.releaseHeldSavepoint();
            } else if (status.isNewTransaction()) {
                // 独立事务直接提交
                unexpectedRollback = status.isGlobalRollbackOnly();
                doCommit(status);
            } else if (isFailEarlyOnGlobalRollbackOnly()) {
                unexpectedRollback = status.isGlobalRollbackOnly();
            }

            // Throw UnexpectedRollbackException if we have a global rollback-only
            // marker but still didn't get a corresponding exception from commit.
            if (unexpectedRollback) {
                throw new UnexpectedRollbackException("Transaction silently rolled back because it has been marked as rollback-only");
            }
        } catch (UnexpectedRollbackException ex) {
            // can only be caused by doCommit
            triggerAfterCompletion(status, TransactionSynchronization.STATUS_ROLLED_BACK);
            throw ex;
        } catch (TransactionException ex) {
            // can only be caused by doCommit
            if (isRollbackOnCommitFailure()) {
                doRollbackOnCommitException(status, ex);
            }
            else {
                triggerAfterCompletion(status, TransactionSynchronization.STATUS_UNKNOWN);
            }
            throw ex;
        } catch (RuntimeException | Error ex) {
            // 提交时出现异常, 回滚
            if (!beforeCompletionInvoked) {
                triggerBeforeCompletion(status);
            }
            doRollbackOnCommitException(status, ex);
            throw ex;
        }
        try {
            triggerAfterCommit(status);
        } finally {
            triggerAfterCompletion(status, TransactionSynchronization.STATUS_COMMITTED);
        }
    } finally {
        // 和回滚一样的操作
        cleanupAfterCompletion(status);
    }
}
```

至此我们分析了Spring事务的主干逻辑, 这对我们以后在工作中遇到问题时, Debug调试源码提供很大帮助, 也可以更深入得了解Spring的风格及编码习惯, 使得自己的代码能力有很大提升.

## 总结
还有很多细节并没有分析, 在此列出:
- JTA相关的逻辑, JTA可以说在很多方法中加了判断逻辑, Spring如此大力支持JTA, 自然值得深究一番
- `TransactionSynchronizationManager`及`TransactionSynchronization`的具体应用
- `ResourceHolderSupport`成员变量`synchronizedWithTransaction`的意义

虚心学习, 每日提升!
