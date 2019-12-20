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

## AbstractApplicationContext#refresh

## BeanFactoryPostProcessor扩展点

主要用于bean的加载

- `MapperScannerConfigurer` 扫描mybatis的mapper.java 加载进入ApplicationContext
- `ConfigurationClassPostProcessor` 解析 `@PropertySources` `@ComponentScans` `@ComponentScan` `@ImportResource` 加载bean或配置文件的注解
    - `ClassPathScanningCandidateComponentProvider#findCandidateComponents`最后在此方法中扫描/获得`BeanDefinition`, 在`isCandidateComponent(metadataReader)`根据`excludeFilters`, `includeFilters`筛选, `ClassPathScanningCandidateComponentProvider`在初始化的时候会在`includeFilters`中添加`@Component`筛选器
    
    
    


## doGetBean

每次bean实例化完成后, 都会调用 `AbstractBeanFactory#getObjectForBeanInstance` 判断得到的bean是不是`FactoryBean`, 如果不是则返回原bean, 如果是则返回`FactoryBean#getBean`的返回值
- MapperFactoryBean 返回mapper的代理类


## Aop切入点

### AbstractAutowireCapableBeanFactory#resolveBeforeInstantiation

## BeanPostProcessor扩展点

## 调用: AbstractAutowireCapableBeanFactory#applyMergedBeanDefinitionPostProcessors

- AutowiredAnnotationBeanPostProcessor 处理@Autowired和@Value
- InitDestroyAnnotationBeanPostProcessor 处理@PostConstruct和@PreDestroy
- CommonAnnotationBeanPostProcessor 处理@Resource及其他一些JSR注解
    - MergedBeanDefinitionPostProcessor#postProcessMergedBeanDefinition 处理/提取@Resource
    - InstantiationAwareBeanPostProcessor#postProcessPropertyValues 注入bean

## 嵌入式容器的启动