package xl.test.framework.springboot.aop;

import org.springframework.aop.config.AopConfigUtils;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * created by XUAN on 2019/12/30
 */
public class AspectJBootstrap {

    public static void main(String[] args){
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        // 注册aspectj解析器, 其功能同@EnableAspectJAutoProxy
        AopConfigUtils.registerAspectJAnnotationAutoProxyCreatorIfNecessary(applicationContext);
        // 注册切入点的定义
        applicationContext.register(MyAspectJDefinition.class);
        // 注册目标的bean
        applicationContext.register(AopTarget.class);
        applicationContext.refresh();
        AopTarget target = applicationContext.getBean(AopTarget.class);
        target.print();
        applicationContext.close();
    }
}
