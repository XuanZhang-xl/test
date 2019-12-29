package xl.test.framework.springboot.postprocessor;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import xl.test.User;

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

    public static void main(String[] args){
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        // 注册当前引导类
        applicationContext.register(BeanFactoryPostProcessorBootstrap.class);
        // 加入BeanFactoryPostProcessor
        applicationContext.addBeanFactoryPostProcessor(new MyBeanFactoryPostProcessor());
        applicationContext.addBeanFactoryPostProcessor(new MyBeanDefinitionRegistryPostProcessor());
        applicationContext.refresh();
        // 一定要获得自定义的bean, 实例化系统定义的bean的时机在注册BeanPostProcessor之前, 再次获得的时候是从缓存获得了
        applicationContext.getBean("user");

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
}
