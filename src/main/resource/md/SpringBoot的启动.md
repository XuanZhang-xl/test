# SpringBoot启动流程

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

## servletcontext层
reactive?
webAppliactionType选择
webServer选择
ApplicationContext创建

ApplicationListener监听ApplicationEvent

## AbstractApplicationContext#refresh

## BeanFactoryPostProcessor扩展点

主要用于bean的加载

- `MapperScannerConfigurer` 扫描mybatis的mapper.java 加载进入ApplicationContext
    - `ClassPathScanningCandidateComponentProvider#findCandidateComponents`最后在此方法中扫描/获得`BeanDefinition`, 在`isCandidateComponent(metadataReader)`根据`excludeFilters`, `includeFilters`筛选, `ClassPathScanningCandidateComponentProvider`在初始化的时候会在`includeFilters`中添加`@Component`筛选器
- `ConfigurationClassPostProcessor` 扫描到所有@Configuration的BeanDefinition, 并解析这些@Configuration 加载bean
    - 加载到@Configuration后, 最终会调用 `ConfigurationClassParser#doProcessConfigurationClass`解析 `@PropertySources` `@ComponentScans` `@ComponentScan` `@ImportResource` `@Import` `@Component` 加载bean或配置文件的注解
    - `ConfigurationClassParser#doProcessConfigurationClass` 也会解析`org.springframework.context.annotation.ImportSelector`和`org.springframework.context.annotation.ImportBeanDefinitionRegistrar` 获得`ConfigurationClass`, 最终在`ConfigurationClassPostProcessor#processConfigBeanDefinitions`中将这些Configuration实例化并注册
    
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


## Aop切入点

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