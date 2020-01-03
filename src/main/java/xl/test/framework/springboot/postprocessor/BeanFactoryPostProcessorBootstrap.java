package xl.test.framework.springboot.postprocessor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import xl.test.common.entity.User;
import xl.test.javabasic.orm.MapperLocation;

import java.io.IOException;

/**
 * 从 PostProcessorRegistrationDelegate#invokeBeanFactoryPostProcessors() 这个方法中可以明显看到执行顺序
 *
 * 1. BeanDefinitionRegistryPostProcessor#postProcessBeanDefinitionRegistry
 * 2. BeanDefinitionRegistryPostProcessor#postProcessBeanFactory
 * 3. BeanFactoryPostProcessor#postProcessBeanFactory
 *
 *
 * created by XUAN on 2019/12/27
 */
public class BeanFactoryPostProcessorBootstrap {

    @Value("${owner.name}")
    private String name;


    public static void main(String[] args){
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        // 注册当前引导类
        applicationContext.register(BeanFactoryPostProcessorBootstrap.class);
        // 加入BeanFactoryPostProcessor
        applicationContext.addBeanFactoryPostProcessor(new MyBeanFactoryPostProcessor());
        applicationContext.addBeanFactoryPostProcessor(new MyBeanDefinitionRegistryPostProcessor());
        applicationContext.refresh();
        // 一定要获得自定义的bean, 实例化系统定义的bean的时机在注册BeanPostProcessor之前, 再次获得的时候是从缓存获得了
        // name属性注入成功
        User user = applicationContext.getBean(User.class);
        System.out.println("user中name注入为: " + user.getName());
        // name属性注入失败, 目测是因为当前启动类在很早的时候就实例化了, 比属性读取还早, 所以没有注入
        BeanFactoryPostProcessorBootstrap bootstrap = applicationContext.getBean(BeanFactoryPostProcessorBootstrap.class);
        System.out.println("BeanFactoryPostProcessorBootstrap中name注入为: " + bootstrap.getName());

        // 关闭上下文
        applicationContext.close();
    }

    /**
     * 注册 MyBeanPostProcessor
     * @return
     */
    @Bean
    public MyBeanPostProcessor mybeanPostProcessor () {
        return new MyBeanPostProcessor();
    }

    /**
     * BeanPostProcessor在bean实例化的时候执行
     * @return
     */
    @Bean
    public User user () {
        return new User();
    }

    @Bean
    public PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() throws IOException {
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        String resourceString = "custom.properties";
        Resource[] resources = resolver.getResources(resourceString);
        PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
        configurer.setLocations(resources);
        return configurer;
    }

    public String getName() {
        return name;
    }
}
