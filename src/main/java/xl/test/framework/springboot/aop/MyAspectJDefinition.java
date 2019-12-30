package xl.test.framework.springboot.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

/**
 * created by XUAN on 2019/12/30
 */
@Aspect
public class MyAspectJDefinition {

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
