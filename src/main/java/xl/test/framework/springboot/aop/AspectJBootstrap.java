package xl.test.framework.springboot.aop;

import org.springframework.aop.config.AopConfigUtils;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import xl.test.common.service.UserService;
import xl.test.framework.springboot.aop.proxy.UserServiceForAopImpl;

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
        // 被@DeclareParents增强的类
        applicationContext.register(UserServiceForAopImpl.class);
        applicationContext.refresh();
        TargetWrapper target = (TargetWrapper) applicationContext.getBean("aopTarget");

        UserService userService = applicationContext.getBean(UserService.class);

        System.out.println(((TargetWrapper)userService).getTarget());

        applicationContext.close();
    }
}
