package xl.test.framework.springboot.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.DeclareParents;
import org.aspectj.lang.annotation.Pointcut;

/**
 * created by XUAN on 2019/12/30
 */
@Aspect
public class MyAspectJDefinition {

    /**
     * UserService+ 表示作用于所有UserService的子类
     * 成员变量的类型是TargetWrapper, 代表被代理类将实现TargetWrapper
     * defaultImpl 必须是TargetWrapper的实现类, 代表动态代理使用AopTarget作为被调用的实例
     *
     * 具体实现查看 DelegatePerTargetObjectIntroductionInterceptor
     */
    @DeclareParents(value = "xl.test.common.service.UserService+", defaultImpl = AopTarget.class)
    private TargetWrapper targetWrapper;

    /**
     * 匹配所有方法名为print的方法
     */
    @Pointcut("execution(* *.print(..))")
    public void point() {
    }

    @Before("point()")
    public void before(){
        System.out.println("before");
    }

    @After("point()")
    public void after(){
        System.out.println("after");
    }

    @Around("point()")
    public Object around(ProceedingJoinPoint p) throws Throwable {
        System.out.println("around begin");
        Object proceed = p.proceed();
        System.out.println("around end");
        return proceed;
    }




}
