# SpringBoot2.1.2启动流程

## 启动前

当SpringBoot项目使用`java -jar`方式部署的时候, 并不是直接去启动Application中的main方法.

而是在`FAT JAR`中把spring-boot-loader项目也打包进去, 使用其中的`JarLauncher`或`WarLauncher`引导Application启动

以`JarLauncher`为例:

首先解压jar包, 查看/META-INF/MANIFEST.MF文件
```
Manifest-Version: 1.0
Implementation-Title: dss-core
Implementation-Version: 1.0.0-SNAPSHOT
Built-By: jm006993
Implementation-Vendor-Id: com.baozun
Spring-Boot-Version: 2.0.4.RELEASE
Main-Class: org.springframework.boot.loader.JarLauncher
Start-Class: com.baozun.ross.dss.core.CoreApplication
Spring-Boot-Classes: BOOT-INF/classes/
Spring-Boot-Lib: BOOT-INF/lib/
Created-By: Apache Maven 3.6.1
Build-Jdk: 1.8.0_112
Implementation-URL: https://projects.spring.io/spring-boot/#/spring-boot-starter-parent/ross-dss/dss-core
```

可以看到直接启动类是`JarLauncher`, 而Application文件在Start-Class属性中列出.

再来看看SpringBoot是如何解析的:

查看 `Launcher#launch`

```
	protected void launch(String[] args) throws Exception {
		JarFile.registerUrlProtocolHandler();
		ClassLoader classLoader = createClassLoader(getClassPathArchives());
		launch(args, getMainClass(), classLoader);
	}
```

`JarFile.registerUrlProtocolHandler();`方法利用了`java.net.URLStreamHandler`扩展机制, 当调用`java.net.URL#getURLStreamHandler`, 传入的参数为`"jar"`时, 
返回是`org.springframework.boot.loader.jar.Handler` 而不是jdk自带的`sun.net.www.protocol.jar.Handler`

之所以需要扩展`Handler`是因为`FAT JAR`被`java -jar`命令引导时, 其内部的jar文件无法被内建实现`sun.net.www.protocol.jar.Handler`当做`ClassPath`, 所以需要替换.

`getClassPathArchives()` 获得需要加载的jar包的封装类, 里面包含了jar所在位置的url, 下一步就是创建`ClassLoader`加载这些jar包

`getMainClass()`  加载/META-INF/MANIFEST.MF文件获得 Start-Class属性

```
	protected void launch(String[] args, String mainClass, ClassLoader classLoader)
			throws Exception {
		Thread.currentThread().setContextClassLoader(classLoader);
		createMainMethodRunner(mainClass, args, classLoader).run();
	}
```
最终在run方法中反射调用`Application#main`启动.

## SpringApplication启动流程方法分析

只挑重点代码

### SpringApplication() 构造方法
```
	public SpringApplication(ResourceLoader resourceLoader, Class<?>... primarySources) {
	    // 传入的resourceLoader, 一般为空
		this.resourceLoader = resourceLoader;
		Assert.notNull(primarySources, "PrimarySources must not be null");
		// 启动类, 一般就是标注@SpringbootApplication的类, 当然也可以是别的
		this.primarySources = new LinkedHashSet<>(Arrays.asList(primarySources));
		// 判断当前应用类型, servlet/reactive/none, 根据当前应用类型会分别实例化以下ApplicationContext
		// servlet   AnnotationConfigServletWebServerApplicationContext
		// reactive  AnnotationConfigReactiveWebServerApplicationContext
		// none	     AnnotationConfigApplicationContext
		this.webApplicationType = WebApplicationType.deduceFromClasspath();
		// /META-INF 加载 ApplicationContextInitializer
		setInitializers((Collection) getSpringFactoriesInstances(
				ApplicationContextInitializer.class));
	    // /META-INF 加载 ApplicationListener
		setListeners((Collection) getSpringFactoriesInstances(ApplicationListener.class));
		// 有main方法的启动类
		this.mainApplicationClass = deduceMainApplicationClass();
	}
```


### SpringApplication.run()
```
        // 初始化监听器, 用于发布Springboot事件
		SpringApplicationRunListeners listeners = getRunListeners(args);
		// 发布ApplicationStartingEvent事件
		listeners.starting();
		// 传入的args参数解析
        ApplicationArguments applicationArguments = new DefaultApplicationArguments(args);
		// 根据 webApplicationType 配置环境，这里面ConfigFileApplicationListener接收到事件后会调用postProcessEnvironment方法加载application.yml配置文件
        ConfigurableEnvironment environment = prepareEnvironment(listeners, applicationArguments);
        // 处理需要忽略的Bean
        configureIgnoreBeanInfo(environment);
        // 打印banner
        Banner printedBanner = printBanner(environment);
        // 根据 webApplicationType 创建Spring的ApplicationContext
        ConfigurableApplicationContext context = createApplicationContext();
        // 刷新应用上下文前的准备阶段, TODO
        prepareContext(context, environment, listeners, applicationArguments, printedBanner);
        // 刷新应用上下文, 最重要, 里面调用了 AbstractApplicationContext.refresh, 以及注册ShutdownHook线程
        refreshContext(context);
        //刷新应用上下文后的扩展接口, 空实现
        afterRefresh(context, applicationArguments);
        // 发布ApplicationStartedEvent事件
        listeners.started(context);
        // 执行ApplicationRunner和CommandLineRunner, 注册为bean之后, 会在springboot启动完成之前会执行run()方法, 貌似并无卵用
        callRunners(context, applicationArguments);
        // 发布ApplicationReadyEvent事件
		listeners.running(context);
```
### SpringApplication.prepareContext()
在SpringApplication.run()中调用
```
	private void prepareContext(ConfigurableApplicationContext context, ConfigurableEnvironment environment, SpringApplicationRunListeners listeners, ApplicationArguments applicationArguments, Banner printedBanner) {
		// 设置环境
		context.setEnvironment(environment);
		// 设置用户配置的beanNameGenerator(用于bean命名), resourceLoader(用于resource加载, 如application.properties), addConversionService(TODO)
		postProcessApplicationContext(context);
		// 执行初始化器
		applyInitializers(context);
		// 发布ApplicationContextInitializedEvent事件
		listeners.contextPrepared(context);
		// Add boot specific singleton beans  注册些看似没什么用的bean
		ConfigurableListableBeanFactory beanFactory = context.getBeanFactory();
		beanFactory.registerSingleton("springApplicationArguments", applicationArguments);
		if (printedBanner != null) {
			beanFactory.registerSingleton("springBootBanner", printedBanner);
		}
		if (beanFactory instanceof DefaultListableBeanFactory) {
			((DefaultListableBeanFactory) beanFactory).setAllowBeanDefinitionOverriding(this.allowBeanDefinitionOverriding);
		}
		// Load the sources 就是启动时传入的class类型以及setSources()增加的类名, 包名, xml资源路径等
		Set<Object> sources = getAllSources();
		// 注册sources中的bean
		load(context, sources.toArray(new Object[0]));
		// 发布ApplicationPreparedEvent事件
		listeners.contextLoaded(context);
	}
```


### AbstractApplicationContext.refresh()
在SpringApplication.run()中调用

接下来的方法分析要涉及具体的`ApplicationContext`实现类, 先来分析一下具体要哪些实现类.

根据`webApplicationType`, `ApplicationContext`实例为
- `AnnotationConfigServletWebServerApplicationContext`
- `AnnotationConfigReactiveWebServerApplicationContext`
- `AnnotationConfigApplicationContext`

以上三种类型都是`GenericApplicationContext`的子类, 所以我们看其实现


```
        // 准备刷新的上下文环境
        prepareRefresh();

        // 此方法在GenericApplicationContext中就 指定序列化id, 可以从这个id反序列化到BeanFactory
        ConfigurableListableBeanFactory beanFactory = obtainFreshBeanFactory();

        // 注册,配置了一系列的bean, 主要功能有:
        // 增加对spel语言的支持   StandardBeanExpressionResolver
        // 增加对属性编辑器的支持  ResourceEditorRegistrar
        // 注册了一些BeanPostProcessor, 如 ApplicationContextAwareProcessor  ApplicationListenerDetector  LoadTimeWeaverAwareProcessor
        // 增加Aspectj的支持
        // 注册Environment
        // 忽略各种注入Aware的接口
        prepareBeanFactory(beanFactory);

        try {
            // Allows post-processing of the bean factory in context subclasses.
            // 入源码所说, 这是空实现, 留给子类扩展
            postProcessBeanFactory(beanFactory);

            // 调用 `BeanFactoryPostProcessor.postProcessBeanFactory()`
            // 此方法中会实例化一些系统自己注册的bean
            invokeBeanFactoryPostProcessors(beanFactory);

            // Register bean processors that intercept bean creation.
            // 注册BeanPostProcessors
            registerBeanPostProcessors(beanFactory);

            // Initialize message source for this context.
            // 初始化Message源, 即不同语言的消息体, 国际化处理
            initMessageSource();

            // Initialize event multicaster for this context.
            // 初始化应用消息广播器, 并放入 applicationEventMulticaster 中
            initApplicationEventMulticaster();

            // Initialize other special beans in specific context subclasses.
            // 留给子类来初始化其他bean
            onRefresh();

            // Check for listener beans and register them.
            // 在所有注册的bean中查找ApplicationListener, 并注册到应用消息广播器
            registerListeners();

            // Instantiate all remaining (non-lazy-init) singletons.
            // 找出ConversionService 转换类. 方便使用
            // 冻结bean的定义, bean定义将不能再修改
            // 初始化剩下的单实例(非惰性)bean, 会遍历所有beanName, 调用getBean()方法初始化bean
            finishBeanFactoryInitialization(beanFactory);

            // Last step: publish corresponding event.
            // LifecycleProcessor 刷新过程  DefaultLifecycleProcessor
            // 发送ContextRefreshedEvent消息
            finishRefresh();
        }

        catch (BeansException ex) {
            if (logger.isWarnEnabled()) {
                logger.warn("Exception encountered during context initialization - " +
                        "cancelling refresh attempt: " + ex);
            }

            // Destroy already created singletons to avoid dangling resources.
            destroyBeans();

            // Reset 'active' flag.
            cancelRefresh(ex);

            // Propagate exception to caller.
            throw ex;
        }

        finally {
            // Reset common introspection caches in Spring's core, since we
            // might not ever need metadata for singleton beans anymore...
            resetCommonCaches();
        }

```


## 监听器, 观察者模式

从META-INF获得默认的
- `SpringApplicationRunListener`   Springboot事件监听器, 只有一个实现`EventPublishingRunListener`, 主要将事件转发给众多的`ApplicationListener`
- `ApplicationListener` 真正干事的listener, 可以根据泛型的实现有选择得监听事件, 参考`ListenerCacheKey`


Spring内建事件, 按发布先后顺序排序
- ContextRefreshEvent Spring应用上下文就绪事件, `AbstractApplicationContext#finishRefresh`
- RequestHandledEvent Spring应用上下文就绪事件
- ContextClosedEvent  Spring应用上下文关闭事件, `AbstractApplicationContext#doClose()`
- ContextStartedEvent Spring应用上下文启动事件, `AbstractApplicationContext#start`, 没有被调用
- ContextStoppedEvent Spring应用上下文停止事件, `AbstractApplicationContext#stop`, 没有被调用

SpringBoot内建事件, 按发布先后顺序排序
- ApplicationStartingEvent
- ApplicationEnvironmentPreparedEvent
- ApplicationContextInitializedEvent
- ApplicationPreparedEvent
- ApplicationStartedEvent
- ApplicationReadyEvent
- ApplicationFailedEvent

监听器的使用请参考 `xl.test.framework.springboot.listener` 包下的类

## ApplicationContextInitializer 初始化器

默认四个初始化器
- ConfigurationWarningsApplicationContextInitializer    貌似就是打几个warn日志
- ContextIdApplicationContextInitializer                初始化ContextId. 从`spring.application.name`属性获取. 没有的话默认为`application`
- DelegatingApplicationContextInitializer               从`context.initializer.classes`属性获得ApplicationContextInitializer并实例化
- ServerPortInfoApplicationContextInitializer           这个初始化器同时也是一个监听器, 监听WebServerInitializedEvent事件,服务启动后将端口设置到Environment中去, `server.ports=xxx`


## BeanFactoryPostProcessor扩展点

其会在`AbstractApplicationContext.invokeBeanFactoryPostProcessors`中被调用, 这个时候Spring已经准备好基本的BeanDefinition了, 这是Spring留下的一个修改BeanDefinition的扩展

BeanFactoryPostProcessor需要手动调用AbstractApplicationContext#addBeanFactoryPostProcessor注册

### BeanFactoryPostProcessor注册:
- AnnotationConfigUtils.registerAnnotationConfigProcessors(), 其中自动注册了
    - ConfigurationClassPostProcessor    处理ImportBeanDefinitionRegistrar和ImportSelector并把结果迭代处理
    - EventListenerMethodProcessor       处理@EventListener
- ImportBeanDefinitionRegistrar和ImportSelector 自定义注册

### 用途示例

主要用于BeanDefinition的加载

- `MapperScannerConfigurer` 扫描mybatis的mapper.java 加载进入ApplicationContext
    - `ClassPathScanningCandidateComponentProvider#findCandidateComponents`最后在此方法中扫描/获得`BeanDefinition`, 在`isCandidateComponent(metadataReader)`根据`excludeFilters`, `includeFilters`筛选, `ClassPathScanningCandidateComponentProvider`在初始化的时候会在`includeFilters`中添加`@Component`筛选器
- `ConfigurationClassPostProcessor` 扫描到所有@Configuration的BeanDefinition, 并解析这些@Configuration 加载bean
    - 加载到@Configuration后, 最终会调用 `ConfigurationClassParser#doProcessConfigurationClass`解析 `@PropertySources` `@ComponentScans` `@ComponentScan` `@ImportResource` `@Import` `@Component` 加载bean或配置文件的注解
    - `ConfigurationClassParser#doProcessConfigurationClass` 也会解析`org.springframework.context.annotation.ImportSelector`和`org.springframework.context.annotation.ImportBeanDefinitionRegistrar` 获得`ConfigurationClass`, 最终在`ConfigurationClassPostProcessor#processConfigBeanDefinitions`中将这些Configuration实例化并注册
- `EventListenerMethodProcessor`获得所有EventListenerFactory实例, 通过这些实例获得`ApplicationListener`并将其加入ApplicationContext使其生效

### 调用的先后顺序

即PostProcessorRegistrationDelegate.invokeBeanFactoryPostProcessors() 的实现


### 条件装配: @Conditional

@Conditional 扩展出了很多注解, 比如
- @Profile
- @ConditionalOnXxxx

其中 `@Conditional`与`@Profile`在`spring framework`, 而大部分`@ConditionalOnXxxx`在`springboot-autoconfig`, 也就是说`@Conditional`其实是`spring framework`, `springboot`只是其扩展

扩展@Conditional的同时, 也需要实现一个处理类实现`org.springframework.context.annotation.Condition`接口

处理这个`Condition`实现类的有三个地方:
- org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider     处理包扫描到条件装配
- org.springframework.context.annotation.AnnotatedBeanDefinitionReader                   // TODO: 这个类与上面的区别是?             
- org.springframework.context.annotation.ConfigurationClassParser                        处理`@Configuration`标注的条件装配

这三个地方最终都会转到统一处理实现 ---- org.springframework.context.annotation.ConditionEvaluator#shouldSkip

TODO: 比如 OnBeanCondition 有getOutcomes 与getMatchOutcome两个匹配方法, 这两个匹配方法各自的用途是?  
    
### 自动装配: @EnableAutoConfiguration
自动装配流程:
- 编写`XxxxConfiguration`, 标注@Configuration
- 在 `META-INF/spring.factories`中添加 `org.springframework.boot.autoconfigure.EnableAutoConfiguration=XxxxConfiguration`

自动装配源码解析:
- 入口为`ConfigurationClassPostProcessor`
- 在`ConfigurationClassParser#processImports`中处理`org.springframework.boot.autoconfigure.AutoConfigurationImportSelector`, 其是`ImportSelector`的实现类
- 在`AutoConfigurationImportSelector#selectImports`中, 它做了:
    - `AutoConfigurationMetadataLoader.loadMetadata(this.beanClassLoader);`加载`"META-INF/spring-autoconfigure-metadata.properties`, 这个文件用于自动过滤, 可以更快速的过滤不满足条件的Configuration, 格式为`自动配置的类全名.条件=值`
    - 加载`@EnableAutoConfiguration`的元信息, 封装为`AnnotationAttributes`
    - 从`META-INF/spring.factories`中获得 `org.springframework.boot.autoconfigure.EnableAutoConfiguration`为key的value值, 转换为List<String>
    - 去重
    - 过滤: 从`元信息`, `spring.autoconfig.exclude`中获得exclusions参数, 从结果中去除exclusions参数中的值
    - 自动过滤: 从`/META-INF/spring.factories`中获得`AutoConfigurationImportFilter`, 默认过滤器是`onClassCondition`, 使用其进行过滤, 一般就是看这个Class/Bean有没有
    - `fireAutoConfigurationImportEvents(configurations, exclusions);`
        - 从`/META-INF/spring.factories`中获得`AutoConfigurationImportListener`, 其用于监听`AutoConfigurationImportEvent`, 默认内建实现为`ConditionEvaluationReportAutoConfigurationImportListener`, 用于记录自动装配的条件评估详情


#### 自动装配的层次性 `DeferredImportSelector`

`DeferredImportSelector`接口会在实现了`ImportSelector`的Configuration注册后再才被注册, 调用的地方在`ConfigurationClassParser#parse`的最后一行代码`this.deferredImportSelectorHandler.process();`

`DeferredImportSelector`有内部接口`DeferredImportSelector.Group`, 在注册的时候会根据`Group`分类, 并按照`Ordered接口`或`@Order`排序后注册, 从而实现了组内的`ImportSelector`注册时的层次性,而组间的注册顺序貌似并没有处理

当然springboot2.1.2版本暂时只有一个实现类`AutoConfigurationImportSelector`, Group实现类为`AutoConfigurationImportSelector.AutoConfigurationGroup`,最终迭代注册的是`ConfigurationClassParser.DeferredImportSelectorGrouping`

疑问:

```ConfigurationClassParser
	private class DeferredImportSelectorHandler {

		@Nullable
		private List<DeferredImportSelectorHolder> deferredImportSelectors = new ArrayList<>();

		public void handle(ConfigurationClass configClass, DeferredImportSelector importSelector) {
			DeferredImportSelectorHolder holder = new DeferredImportSelectorHolder(
					configClass, importSelector);
			// 这里是添加DeferredImportSelectorHolder的地方, 为什么deferredImportSelectors还能是null, 难道已经调用了process()方法了, 还能再添加DeferredImportSelectorHolder吗?
			if (this.deferredImportSelectors == null) {
				DeferredImportSelectorGroupingHandler handler = new DeferredImportSelectorGroupingHandler();
				handler.register(holder);
				handler.processGroupImports();
			}
			else {
				this.deferredImportSelectors.add(holder);
			}
		}

		public void process() {
			List<DeferredImportSelectorHolder> deferredImports = this.deferredImportSelectors;
			this.deferredImportSelectors = null;
			try {
				if (deferredImports != null) {
					DeferredImportSelectorGroupingHandler handler = new DeferredImportSelectorGroupingHandler();
					deferredImports.sort(DEFERRED_IMPORT_COMPARATOR);
					deferredImports.forEach(handler::register);
					handler.processGroupImports();
				}
			}
			finally {
				this.deferredImportSelectors = new ArrayList<>();
			}
		}
	}
```

#### 自动装配的组内排序

- 绝对自动装配排序: @AutoConfigureOrder  不建议使用
- 相对自动装配排序: @AutoConfigureBefore  @AutoConfigureAfter

源码解析: `AutoConfigurationSorter#getInPriorityOrder`

```
	public List<String> getInPriorityOrder(Collection<String> classNames) {
		AutoConfigurationClasses classes = new AutoConfigurationClasses(
				this.metadataReaderFactory, this.autoConfigurationMetadata, classNames);
		List<String> orderedClassNames = new ArrayList<>(classNames);
		// 按照全类名字典顺序排序
		Collections.sort(orderedClassNames);
		// 按照@AutoConfigureOrder排序
		orderedClassNames.sort((o1, o2) -> {
			int i1 = classes.get(o1).getOrder();
			int i2 = classes.get(o2).getOrder();
			return Integer.compare(i1, i2);
		});
		// 按照@AutoConfigureBefore @AutoConfigureAfter排序
		orderedClassNames = sortByAnnotation(classes, orderedClassNames);
		return orderedClassNames;
	}
```

#### spring-autoconfigure-metadata.properties 装配条件及解释
- Configuration
- AutoConfigureBefore   X1.AutoConfigureBefore=X2 代表X1在X2之前装配, 等价于`@AutoConfigureBefore`, 优先级高于`@AutoConfigureBefore`
- AutoConfigureAfter    X1.AutoConfigureAfter=X2  代表X1在X2之后装配, 等价于`@AutoConfigureAfter`, 优先级高于`@AutoConfigureAfter`
- ConditionalOnClass
- ConditionalOnBean


## doGetBean

每次bean实例化完成后, 都会调用 `AbstractBeanFactory#getObjectForBeanInstance` 判断得到的bean是不是`FactoryBean`, 如果不是则返回原bean, 如果是则返回`FactoryBean#getBean`的返回值
- MapperFactoryBean 返回mapper的代理类


## Aop

- JDK动态代理: 代理对象必须是某接口的实现, 因为它是通过创建 `extends Proxy implements 某接口`这样的类来实现的
- CGLIB动态代理:实现原理类似JDK动态代理, 但是它是 直接继承的被代理对象, 底层依靠ASM操作字节码, 性能比JDK高

从`@EnableAspectJAutoProxy`开始分析, 它引入了`AspectJAutoProxyRegistrar`

`AspectJAutoProxyRegistrar`中有一行这样的代码:
```
AopConfigUtils.registerAspectJAnnotationAutoProxyCreatorIfNecessary(registry);
```
点进去后发现其注册了`AnnotationAwareAspectJAutoProxyCreator`, 而`AnnotationAwareAspectJAutoProxyCreator`是`BeanPostProcessor`的实现类.

其实现`BeanPostProcessor`的地方在其父类`AbstractAutoProxyCreator`

至此, aop引入流程明了.

### AbstractAutoProxyCreator

AbstractAutoProxyCreator成员变量
- `Set<String> targetSourcedBeans`
- `Set<Object> earlyProxyReferences`
- `Map<Object, Class<?>> proxyTypes`
- `Map<Object, Boolean> advisedBeans`    存放aop基础信息类, 用于代理时跳过基础信息类, 如标注@Aspectj的类, 继承Advisor的类等, 具体见方法`isInfrastructureClass()`

### 获得增强器

先来看看增强器获取方法`AbstractAdvisorAutoProxyCreator.getAdvicesAndAdvisorsForBean()`, 其主要逻辑最终在`BeanFactoryAspectJAdvisorsBuilder.buildAspectJAdvisors()`中
```
public List<Advisor> buildAspectJAdvisors() {
    List<String> aspectNames = this.aspectBeanNames;
    if (aspectNames == null) {
        synchronized (this) {
            aspectNames = this.aspectBeanNames;
            if (aspectNames == null) {
                List<Advisor> advisors = new ArrayList<>();
                aspectNames = new ArrayList<>();
                // 获得所有beanname
                String[] beanNames = BeanFactoryUtils.beanNamesForTypeIncludingAncestors(this.beanFactory, Object.class, true, false);
                // 循环所有beanname找出增强方法
                for (String beanName : beanNames) {
                    // 不合法的bean忽略, 由子类定义规则, 默认true
                    if (!isEligibleBean(beanName)) {
                        continue;
                    }
                    // 获得ban类型
                    Class<?> beanType = this.beanFactory.getType(beanName);
                    if (beanType == null) {
                        continue;
                    }
                    // 如果存在@Aspect注解
                    if (this.advisorFactory.isAspect(beanType)) {
                        aspectNames.add(beanName);
                        AspectMetadata amd = new AspectMetadata(beanType, beanName);
                        // TODO: 这个判断是干嘛?
                        if (amd.getAjType().getPerClause().getKind() == PerClauseKind.SINGLETON) {
                            // 将解析标记Aspectj注解的任务交给MetadataAwareAspectInstanceFactory
                            MetadataAwareAspectInstanceFactory factory = new BeanFactoryAspectInstanceFactory(this.beanFactory, beanName);
                            // 获得增强方法, 此方法中处理了普通增强器, 增强延迟初始化, 和@DeclareParent的处理
                            // 普通增强器实现类为InstantiationModelAwarePointcutAdvisorImpl
                            // @DeclareParent增强(引介增强? 不知所谓的垃圾翻译) 实现类为 DeclareParentsAdvisor, 实现接口为 IntroductionAdvisor
                            List<Advisor> classAdvisors = this.advisorFactory.getAdvisors(factory);
                            // 放入缓存
                            if (this.beanFactory.isSingleton(beanName)) {
                                this.advisorsCache.put(beanName, classAdvisors);
                            } else {
                                this.aspectFactoryCache.put(beanName, factory);
                            }
                            advisors.addAll(classAdvisors);
                        }
                        else {
                            // Per target or per this.
                            if (this.beanFactory.isSingleton(beanName)) {
                                throw new IllegalArgumentException("Bean with name '" + beanName + "' is a singleton, but aspect instantiation model is not singleton");
                            }
                            MetadataAwareAspectInstanceFactory factory = new PrototypeAspectInstanceFactory(this.beanFactory, beanName);
                            this.aspectFactoryCache.put(beanName, factory);
                            advisors.addAll(this.advisorFactory.getAdvisors(factory));
                        }
                    }
                }
                this.aspectBeanNames = aspectNames;
                return advisors;
            }
        }
    }
    // 如果已经执行过, 则去缓存中取
    if (aspectNames.isEmpty()) {
        return Collections.emptyList();
    }
    List<Advisor> advisors = new ArrayList<>();
    for (String aspectName : aspectNames) {
        List<Advisor> cachedAdvisors = this.advisorsCache.get(aspectName);
        if (cachedAdvisors != null) {
            advisors.addAll(cachedAdvisors);
        }
        else {
            MetadataAwareAspectInstanceFactory factory = this.aspectFactoryCache.get(aspectName);
            advisors.addAll(this.advisorFactory.getAdvisors(factory));
        }
    }
    return advisors;
}
```
在初始化`InstantiationModelAwarePointcutAdvisorImpl`的过程中, 还初始化了`Advice`, 根据Aspectj注解的不同, 初始化不同的`Advice`
```
// 此方法在InstantiationModelAwarePointcutAdvisorImpl中
private Advice instantiateAdvice(AspectJExpressionPointcut pointcut) {
    Advice advice = this.aspectJAdvisorFactory.getAdvice(this.aspectJAdviceMethod, pointcut, this.aspectInstanceFactory, this.declarationOrder, this.aspectName);
    return (advice != null ? advice : EMPTY_ADVICE);
}

public Advice getAdvice(Method candidateAdviceMethod, AspectJExpressionPointcut expressionPointcut, MetadataAwareAspectInstanceFactory aspectInstanceFactory, int declarationOrder, String aspectName) {

    Class<?> candidateAspectClass = aspectInstanceFactory.getAspectMetadata().getAspectClass();
    validate(candidateAspectClass);

    AspectJAnnotation<?> aspectJAnnotation = AbstractAspectJAdvisorFactory.findAspectJAnnotationOnMethod(candidateAdviceMethod);
    if (aspectJAnnotation == null) {
        return null;
    }

    // If we get here, we know we have an AspectJ method.
    // Check that it's an AspectJ-annotated class
    if (!isAspect(candidateAspectClass)) {
        throw new AopConfigException("Advice must be declared inside an aspect type: " + "Offending method '" + candidateAdviceMethod + "' in class [" + candidateAspectClass.getName() + "]");
    }
    AbstractAspectJAdvice springAdvice;
    // 根据注解不同, 初始化不同增强器
    switch (aspectJAnnotation.getAnnotationType()) {
        case AtPointcut:
            return null;
        case AtAround:
            springAdvice = new AspectJAroundAdvice(candidateAdviceMethod, expressionPointcut, aspectInstanceFactory);
            break;
        case AtBefore:
            springAdvice = new AspectJMethodBeforeAdvice(candidateAdviceMethod, expressionPointcut, aspectInstanceFactory);
            break;
        case AtAfter:
            springAdvice = new AspectJAfterAdvice(candidateAdviceMethod, expressionPointcut, aspectInstanceFactory);
            break;
        case AtAfterReturning:
            springAdvice = new AspectJAfterReturningAdvice(candidateAdviceMethod, expressionPointcut, aspectInstanceFactory);
            AfterReturning afterReturningAnnotation = (AfterReturning) aspectJAnnotation.getAnnotation();
            if (StringUtils.hasText(afterReturningAnnotation.returning())) {
                springAdvice.setReturningName(afterReturningAnnotation.returning());
            }
            break;
        case AtAfterThrowing:
            springAdvice = new AspectJAfterThrowingAdvice(candidateAdviceMethod, expressionPointcut, aspectInstanceFactory);
            AfterThrowing afterThrowingAnnotation = (AfterThrowing) aspectJAnnotation.getAnnotation();
            if (StringUtils.hasText(afterThrowingAnnotation.throwing())) {
                springAdvice.setThrowingName(afterThrowingAnnotation.throwing());
            }
            break;
        default:
            throw new UnsupportedOperationException("Unsupported advice type on method: " + candidateAdviceMethod);
    }

    // Now to configure the advice...
    springAdvice.setAspectName(aspectName);
    springAdvice.setDeclarationOrder(declarationOrder);
    String[] argNames = this.parameterNameDiscoverer.getParameterNames(candidateAdviceMethod);
    if (argNames != null) {
        springAdvice.setArgumentNamesFromStringArray(argNames);
    }
    springAdvice.calculateArgumentBindings();
    return springAdvice;
}
```

### 寻找匹配的增强器
```
// 入口: AbstractAdvisorAutoProxyCreator.findAdvisorsThatCanApply()
protected List<Advisor> findAdvisorsThatCanApply(
        List<Advisor> candidateAdvisors, Class<?> beanClass, String beanName) {

    ProxyCreationContext.setCurrentProxiedBeanName(beanName);
    try {
        return AopUtils.findAdvisorsThatCanApply(candidateAdvisors, beanClass);
    }
    finally {
        ProxyCreationContext.setCurrentProxiedBeanName(null);
    }
}
```
`AopUtils.findAdvisorsThatCanApply()`这个方法里先处理@DeclareParent增强, 再处理普通增强, 具体实现很复杂.


#### 创建代理
```
// AbstractAutoProxyCreator#createProxy
// beanClass:代理类的Class
// beanName: 代理类的beanname
// specificInterceptors:匹配完毕的增强器
// targetSource: 封装了bean, 提供了对bean的一些操作方法
protected Object createProxy(Class<?> beanClass, @Nullable String beanName, @Nullable Object[] specificInterceptors, TargetSource targetSource) {
    // 貌似是spring5新加逻辑, 设置BeanDefinition的属性 org.springframework.aop.framework.autoproxy.AutoProxyUtils.originalTargetClass 为被代理类Class
    if (this.beanFactory instanceof ConfigurableListableBeanFactory) {
        AutoProxyUtils.exposeTargetClass((ConfigurableListableBeanFactory) this.beanFactory, beanName, beanClass);
    }

    ProxyFactory proxyFactory = new ProxyFactory();
    // 获得当前类中相关属性, 如proxyTargetClass, exposeProxy等
    proxyFactory.copyFrom(this);

    // proxyTargetClass默认false, 貌似用于仅代理接口方法还是代理整个类的区分
    if (!proxyFactory.isProxyTargetClass()) {
        if (shouldProxyTargetClass(beanClass, beanName)) {
            proxyFactory.setProxyTargetClass(true);
        }
        else {
            // 里面会判断添加代理接口或proxyFactory.setProxyTargetClass(true);
            evaluateProxyInterfaces(beanClass, proxyFactory);
        }
    }

    Advisor[] advisors = buildAdvisors(beanName, specificInterceptors);
    // 加入增强器
    proxyFactory.addAdvisors(advisors);
    // 设置要代理的类
    proxyFactory.setTargetSource(targetSource);
    // 定制代理
    customizeProxyFactory(proxyFactory);
    // 用来控制代理被配置后, 是否还允许修改通知, 默认fasle, 即一旦代理被配置, 不允许修改代理
    proxyFactory.setFrozen(this.freezeProxy);
    if (advisorsPreFiltered()) {
        proxyFactory.setPreFiltered(true);
    }

    return proxyFactory.getProxy(getProxyClassLoader());
}
```

上面方法中的buildAdvisors()方法的主要逻辑

```
public Advisor wrap(Object adviceObject) throws UnknownAdviceTypeException {
    // 如果要封装的对象本身是Advisor, 不做处理
    if (adviceObject instanceof Advisor) {
        return (Advisor) adviceObject;
    }
    // 本方法只对Advisor和Advice有效, 其他抛异常
    if (!(adviceObject instanceof Advice)) {
        throw new UnknownAdviceTypeException(adviceObject);
    }
    // 如果是MethodInterceptor则使用DefaultPointcutAdvisor封装
    Advice advice = (Advice) adviceObject;
    if (advice instanceof MethodInterceptor) {
        // So well-known it doesn't even need an adapter.
        return new DefaultPointcutAdvisor(advice);
    }
    // 如果存在Advisor适配器也封装
    for (AdvisorAdapter adapter : this.adapters) {
        // Check that it is supported.
        if (adapter.supportsAdvice(advice)) {
            return new DefaultPointcutAdvisor(advice);
        }
    }
    throw new UnknownAdviceTypeException(advice);
}
```
### DefaultAopProxyFactory.createAopProxy() 代理方式的选择与创建

JDK代理还是CGLIB代理的选择
- optimize: 控制通过CGLIB创建的代理是否使用激进的优化策略, 仅CGLIB有效, 尽量不要修改
- proxyTargetClass: 为true时, 目标类本身被代理而不是目标类接口, 也就是CGLIB代理
- Proxy.isProxyClass() 是否存在代理接口

```
// 这个AdvisedSupport就是上面的proxyFactory
public AopProxy createAopProxy(AdvisedSupport config) throws AopConfigException {
    if (config.isOptimize() || config.isProxyTargetClass() || hasNoUserSuppliedProxyInterfaces(config)) {
        Class<?> targetClass = config.getTargetClass();
        if (targetClass == null) {
            throw new AopConfigException("TargetSource cannot determine target class: "Either an interface or a target is required for proxy creation.");
        }
        if (targetClass.isInterface() || Proxy.isProxyClass(targetClass)) {
            return new JdkDynamicAopProxy(config);
        }
        return new ObjenesisCglibAopProxy(config);
    } else {
        return new JdkDynamicAopProxy(config);
    }
}
```
### 获取代理
分别来看看jdk及cglib的代理获取过程
```
// jdk代理获取
public Object getProxy(@Nullable ClassLoader classLoader) {
    // 获得被代理类接口, 如果advised实现了SpringProxy, Advised, DecoratingProxy, 那么这些接口也会被获得
    Class<?>[] proxiedInterfaces = AopProxyUtils.completeProxiedInterfaces(this.advised, true);
    // ???
    findDefinedEqualsAndHashCodeMethods(proxiedInterfaces);
    // 获得代理类
    return Proxy.newProxyInstance(classLoader, proxiedInterfaces, this);
}
// cglib代理获取
public Object getProxy(@Nullable ClassLoader classLoader) {
    if (logger.isTraceEnabled()) {
        logger.trace("Creating CGLIB proxy: " + this.advised.getTargetSource());
    }
    Class<?> rootClass = this.advised.getTargetClass();
    Assert.state(rootClass != null, "Target class must be available for creating a CGLIB proxy");

    Class<?> proxySuperClass = rootClass;
    if (ClassUtils.isCglibProxyClass(rootClass)) {
        proxySuperClass = rootClass.getSuperclass();
        Class<?>[] additionalInterfaces = rootClass.getInterfaces();
        for (Class<?> additionalInterface : additionalInterfaces) {
            this.advised.addInterface(additionalInterface);
        }
    }

    // Validate the class, writing log messages as necessary.
    // 验证class
    validateClassIfNecessary(proxySuperClass, classLoader);

    // Configure CGLIB Enhancer...
    // 配置Enhancer
    Enhancer enhancer = createEnhancer();
    if (classLoader != null) {
        enhancer.setClassLoader(classLoader);
        if (classLoader instanceof SmartClassLoader && ((SmartClassLoader) classLoader).isClassReloadable(proxySuperClass)) {
            enhancer.setUseCache(false);
        }
    }
    // 设置被代理类
    enhancer.setSuperclass(proxySuperClass);
    enhancer.setInterfaces(AopProxyUtils.completeProxiedInterfaces(this.advised));
    enhancer.setNamingPolicy(SpringNamingPolicy.INSTANCE);
    enhancer.setStrategy(new ClassLoaderAwareUndeclaredThrowableStrategy(classLoader));

    // 设置拦截器
    Callback[] callbacks = getCallbacks(rootClass);
    Class<?>[] types = new Class<?>[callbacks.length];
    for (int x = 0; x < types.length; x++) {
        types[x] = callbacks[x].getClass();
    }
    // fixedInterceptorMap only populated at this point, after getCallbacks call above
    enhancer.setCallbackFilter(new ProxyCallbackFilter(this.advised.getConfigurationOnlyCopy(), this.fixedInterceptorMap, this.fixedInterceptorOffset));
    enhancer.setCallbackTypes(types);

    // Generate the proxy class and create a proxy instance.
    // 创建代理类
    return createProxyClassAndInstance(enhancer, callbacks);
}
private Callback[] getCallbacks(Class<?> rootClass) throws Exception {
    // Parameters used for optimization choices...
    boolean exposeProxy = this.advised.isExposeProxy();
    boolean isFrozen = this.advised.isFrozen();
    boolean isStatic = this.advised.getTargetSource().isStatic();

    // 将增强器封装在DynamicAdvisedInterceptor中
    Callback aopInterceptor = new DynamicAdvisedInterceptor(this.advised);
    Callback targetInterceptor;
    if (exposeProxy) {
        targetInterceptor = (isStatic ? new StaticUnadvisedExposedInterceptor(this.advised.getTargetSource().getTarget()) : new DynamicUnadvisedExposedInterceptor(this.advised.getTargetSource()));
    }
    else {
        targetInterceptor = (isStatic ? new StaticUnadvisedInterceptor(this.advised.getTargetSource().getTarget()) : new DynamicUnadvisedInterceptor(this.advised.getTargetSource()));
    }
    Callback targetDispatcher = (isStatic ? new StaticDispatcher(this.advised.getTargetSource().getTarget()) : new SerializableNoOp());
    // 一共注册了7个拦截器, 每个方法会通过ProxyCallbackFilter选择一个合适的拦截器
    Callback[] mainCallbacks = new Callback[] {
            aopInterceptor,  // 普通增强器
            targetInterceptor,  // invoke target without considering advice, if optimized
            new SerializableNoOp(),  // no override for methods mapped to this
            targetDispatcher, this.advisedDispatcher,
            new EqualsInterceptor(this.advised), // 拦截equal()方法
            new HashCodeInterceptor(this.advised) // 拦截hashcode()方法
    };
    Callback[] callbacks;
    if (isStatic && isFrozen) {
        Method[] methods = rootClass.getMethods();
        Callback[] fixedCallbacks = new Callback[methods.length];
        this.fixedInterceptorMap = new HashMap<>(methods.length);

        for (int x = 0; x < methods.length; x++) {
            List<Object> chain = this.advised.getInterceptorsAndDynamicInterceptionAdvice(methods[x], rootClass);
            fixedCallbacks[x] = new FixedChainStaticTargetInterceptor( chain, this.advised.getTargetSource().getTarget(), this.advised.getTargetClass());
            this.fixedInterceptorMap.put(methods[x].toString(), x);
        }
        callbacks = new Callback[mainCallbacks.length + fixedCallbacks.length];
        System.arraycopy(mainCallbacks, 0, callbacks, 0, mainCallbacks.length);
        System.arraycopy(fixedCallbacks, 0, callbacks, mainCallbacks.length, fixedCallbacks.length);
        this.fixedInterceptorOffset = mainCallbacks.length;
    }
    else {
        callbacks = mainCallbacks;
    }
    return callbacks;
}
protected Object createProxyClassAndInstance(Enhancer enhancer, Callback[] callbacks) {
    enhancer.setInterceptDuringConstruction(false);
    enhancer.setCallbacks(callbacks);
    return (this.constructorArgs != null && this.constructorArgTypes != null ?
            enhancer.create(this.constructorArgTypes, this.constructorArgs) :
            enhancer.create());
}
```

至此, 我们已经获得了代理类实例, 接下来就是看运行时的代理类调用流程了. 也就是jdk的`InvocationHandler`和cglib的`Callback`

### 代理类的调用

先来分析`JdkDynamicAopProxy`, 这个类同时也实现了InvocationHandler, 所以我们主要看其`invoke()`方法
```
public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    MethodInvocation invocation;
    Object oldProxy = null;
    boolean setProxyContext = false;
    TargetSource targetSource = this.advised.targetSource;
    Object target = null;
    try {
        // equals方法处理
        if (!this.equalsDefined && AopUtils.isEqualsMethod(method)) {
            return equals(args[0]);
        }
        // hashCode方法处理
        else if (!this.hashCodeDefined && AopUtils.isHashCodeMethod(method)) {
            return hashCode();
        }
        // TODO
        else if (method.getDeclaringClass() == DecoratingProxy.class) {
            return AopProxyUtils.ultimateTargetClass(this.advised);
        }
        // TODO
        else if (!this.advised.opaque && method.getDeclaringClass().isInterface() && method.getDeclaringClass().isAssignableFrom(Advised.class)) {
            return AopUtils.invokeJoinpointUsingReflection(this.advised, method, args);
        }
        // 返回值
        Object retVal;
        // 暴露代理类
        if (this.advised.exposeProxy) {
            oldProxy = AopContext.setCurrentProxy(proxy);
            setProxyContext = true;
        }
        // 获得被代理类实例
        target = targetSource.getTarget();
        Class<?> targetClass = (target != null ? target.getClass() : null);
        // 获得当前方法的拦截器链
        List<Object> chain = this.advised.getInterceptorsAndDynamicInterceptionAdvice(method, targetClass);
        if (chain.isEmpty()) {
            // 没拦截器, 直接反射调用切点方法
            Object[] argsToUse = AopProxyUtils.adaptArgumentsIfNecessary(method, args);
            retVal = AopUtils.invokeJoinpointUsingReflection(target, method, argsToUse);
        } else {
            // 将连接器封装在ReflectiveMethodInvocation中,以便于使用proceed()方法进行链接表用拦截器
            invocation = new ReflectiveMethodInvocation(proxy, target, method, args, targetClass, chain);
            // 重点, 所有增强器的调用都在这个方法中完成
            retVal = invocation.proceed();
        }

        // 返回结果
        Class<?> returnType = method.getReturnType();
        if (retVal != null && retVal == target && returnType != Object.class && returnType.isInstance(proxy) && !RawTargetAccess.class.isAssignableFrom(method.getDeclaringClass())) {
            // TODO:返回了代理类, 我去, 这是什么情况下??
            retVal = proxy;
        } else if (retVal == null && returnType != Void.TYPE && returnType.isPrimitive()) {
            throw new AopInvocationException( "Null return value from advice does not match primitive return type for: " + method);
        }
        return retVal;
    } finally {
        if (target != null && !targetSource.isStatic()) {
            targetSource.releaseTarget(target);
        }
        if (setProxyContext) {
            AopContext.setCurrentProxy(oldProxy);
        }
    }
}
```
ReflectiveMethodInvocation#proceed()方法中使用类似迭代器的方式完成了对所有增强方法的调用, 我们来看看它是怎么做的
```
public Object proceed() throws Throwable {
    //所有增强器都已调用, 调用被代理的方法
    // 注意: 这时候有的增强器的方法在方法栈中, 还未执行完毕, 比如后置增强
    if (this.currentInterceptorIndex == this.interceptorsAndDynamicMethodMatchers.size() - 1) {
        return invokeJoinpoint();
    }
    // 每次调用此方法都获得下一增强器
    // 第0个拦截器是ExposeInvocationInterceptor, 在ThreadLocal中记录下当前类ReflectiveMethodInvocation, 全部增强类调用完毕后移除, 暂不知有何作用TODO
    Object interceptorOrInterceptionAdvice = this.interceptorsAndDynamicMethodMatchers.get(++this.currentInterceptorIndex);
    if (interceptorOrInterceptionAdvice instanceof InterceptorAndDynamicMethodMatcher) {
        // 这个if貌似是对运行时动态增强器的处理, 还没用过, 用过以后再来分析 TODO
        // Evaluate dynamic method matcher here: static part will already have
        // been evaluated and found to match.
        InterceptorAndDynamicMethodMatcher dm = (InterceptorAndDynamicMethodMatcher) interceptorOrInterceptionAdvice;
        Class<?> targetClass = (this.targetClass != null ? this.targetClass : this.method.getDeclaringClass());
        if (dm.methodMatcher.matches(this.method, targetClass, this.arguments)) {
            return dm.interceptor.invoke(this);
        } else {
            // Dynamic matching failed.
            // Skip this interceptor and invoke the next in the chain.
            return proceed();
        }
    } else {
        // 这里是重点, 直接调用增强器的invoke()方法, 我们分别对before,after,around增强器进行分析
        // 注意: 这里传了个this, 为之后的递归调用铺垫
        return ((MethodInterceptor) interceptorOrInterceptionAdvice).invoke(this);
    }
}

// AspectJAfterAdvice
public Object invoke(MethodInvocation mi) throws Throwable {
    try {
        // @After先回调, 调用别的增强器
        return mi.proceed();
    } finally {
        // 被代理方法执行完了, 才会执行到这儿
        // 这个方法里是准备参数, 反射调用增强方法
        invokeAdviceMethod(getJoinPointMatch(), null, null);
    }
}

// MethodBeforeAdviceInterceptor  这个名字没统一, 是因为它把增强方法的调用给了 AspectJMethodBeforeAdvice, 不知道为什么不直接用AspectJMethodBeforeAdvice???
private final MethodBeforeAdvice advice; // 实现类是 AspectJMethodBeforeAdvice
public Object invoke(MethodInvocation mi) throws Throwable {
    // 先执行增强方法
    this.advice.before(mi.getMethod(), mi.getArguments(), mi.getThis());
    // 在调用别的增强器
    return mi.proceed();
}

// 	AspectJAroundAdvice
public Object invoke(MethodInvocation mi) throws Throwable {
    if (!(mi instanceof ProxyMethodInvocation)) {
        throw new IllegalStateException("MethodInvocation is not a Spring ProxyMethodInvocation: " + mi);
    }
    ProxyMethodInvocation pmi = (ProxyMethodInvocation) mi;
    ProceedingJoinPoint pjp = lazyGetProceedingJoinPoint(pmi);
    JoinPointMatch jpm = getJoinPointMatch(pmi);
    // 这里貌似啥也没有, 因为在这方法里组织调用增强方法参数的时候, 会把 MethodInvocation mi 传给增强方法, 也就是我们常见却不知道是啥的的 ProceedingJoinPoint
    // 然后在增强方法里调用 ProceedingJoinPoint#proceed() 以继续调用下一个增强器
    // 终于知道我们常写的ProceedingJoinPoint#proceed()是在干嘛了!
    return invokeAdviceMethod(pjp, jpm, null, null);
}
```

至此, 我们已经把spring的jdk代理大体脉络理清, 而cglib同样也是调用`invocation.proceed();`实现对增强器的调用, 只是调用`invocation.proceed();`的方法不同, 我们来看看cglib的实现

由于spring 注册了7个Callback, 我们只看常用的`DynamicAdvisedInterceptor`
```
// 注意Callback类型是 MethodInterceptor
private static class DynamicAdvisedInterceptor implements MethodInterceptor, Serializable {

    private final AdvisedSupport advised;

    public DynamicAdvisedInterceptor(AdvisedSupport advised) {
        this.advised = advised;
    }

    @Override
    @Nullable
    public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        // 记录之前的代理类, 用于暴露当前代理类的功能
        Object oldProxy = null;
        boolean setProxyContext = false;
        // 被代理类实例
        Object target = null;
        TargetSource targetSource = this.advised.getTargetSource();
        try {
            if (this.advised.exposeProxy) {
                // Make invocation available if necessary.
                oldProxy = AopContext.setCurrentProxy(proxy);
                setProxyContext = true;
            }
            // Get as late as possible to minimize the time we "own" the target, in case it comes from a pool...
            target = targetSource.getTarget();
            Class<?> targetClass = (target != null ? target.getClass() : null);
            // 获取连接器链
            List<Object> chain = this.advised.getInterceptorsAndDynamicInterceptionAdvice(method, targetClass);
            Object retVal;
            if (chain.isEmpty() && Modifier.isPublic(method.getModifiers())) {
                // 拦截器链为空, 直接调用被代理类的方法
                Object[] argsToUse = AopProxyUtils.adaptArgumentsIfNecessary(method, args);
                retVal = methodProxy.invoke(target, argsToUse);
            }
            else {
                // 不为空, 同样调用 ReflectiveMethodInvocation.proceed
                retVal = new CglibMethodInvocation(proxy, target, method, args, targetClass, chain, methodProxy).proceed();
            }
            retVal = processReturnType(proxy, target, method, retVal);
            return retVal;
        } finally {
            if (target != null && !targetSource.isStatic()) {
                targetSource.releaseTarget(target);
            }
            if (setProxyContext) {
                AopContext.setCurrentProxy(oldProxy);
            }
        }
    }
}
```


### AbstractAutowireCapableBeanFactory#resolveBeforeInstantiation

## BeanPostProcessor扩展点

ApplicationContextAwareProcessor 注入各种 XxxAware

## 调用: AbstractAutowireCapableBeanFactory#applyMergedBeanDefinitionPostProcessors

- AutowiredAnnotationBeanPostProcessor 处理@Autowired和@Value
- InitDestroyAnnotationBeanPostProcessor 处理@PostConstruct和@PreDestroy
- CommonAnnotationBeanPostProcessor 处理@Resource及其他一些JSR注解
    - MergedBeanDefinitionPostProcessor#postProcessMergedBeanDefinition 处理/提取@Resource
    - InstantiationAwareBeanPostProcessor#postProcessPropertyValues 注入bean

## 嵌入式容器的启动