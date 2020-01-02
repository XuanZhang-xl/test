# 类加载器ClassLoader

## 从疑问开始

在spring使用cglib创建Enhancer的时候, 有这么一段代码:
```
Enhancer enhancer = createEnhancer();
if (classLoader != null) {
    enhancer.setClassLoader(classLoader);
    // 对SmartClassLoader做了特殊处理
    if (classLoader instanceof SmartClassLoader && ((SmartClassLoader) classLoader).isClassReloadable(proxySuperClass)) {
        enhancer.setUseCache(false);
    }
}
```
那么SmartClassLoader有什么特别之处?