package xl.test.framework.springboot.enable;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

import java.util.stream.Stream;

/**
 * created by XUAN on 2019/12/23
 */
public class ServerImportBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {

    /**
     * 与ImportSelector的区别在于, 把注册的流程也放在这个方法里了
     *
     * @param importingClassMetadata
     * @param registry
     */
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        // 获得需要注册的bean name
        String[] selectImports = EnableImportUtils.selectImports(importingClassMetadata);
        // 注册
        Stream.of(selectImports)
                // 转换为BeanDefinitionBuilder对象
                .map(BeanDefinitionBuilder::genericBeanDefinition)
                // 转换为BeanDefinition
                .map(BeanDefinitionBuilder::getBeanDefinition)
                // 注册到 BeanDefinitionRegistry
                .forEach(beanDefinition -> BeanDefinitionReaderUtils.registerWithGeneratedName(beanDefinition, registry));
    }
}
