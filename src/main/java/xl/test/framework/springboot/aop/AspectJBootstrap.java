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
        // 注册cglib代理bean
        applicationContext.register(AopTarget.class);
        // 被@DeclareParents增强的类
        applicationContext.register(UserServiceForAopImpl.class);
        // cglib代理类
        applicationContext.register(CglibTarget.class);
        applicationContext.refresh();

        // jdk代理测试
        TargetWrapper target = (TargetWrapper) applicationContext.getBean("aopTarget");
        System.out.println("target.getTarget() 返回: " + target.getTarget());

        // cglib代理测试
        CglibTarget cglibTarget = applicationContext.getBean(CglibTarget.class);
        System.out.print("cglibTarget.print() 打印: ");
        cglibTarget.print();

        // @DeclareParents增强的类 测试
        UserService userService = applicationContext.getBean(UserService.class);
        System.out.println(((TargetWrapper)userService).getTarget());

        // 关闭上下文
        applicationContext.close();
    }
}
