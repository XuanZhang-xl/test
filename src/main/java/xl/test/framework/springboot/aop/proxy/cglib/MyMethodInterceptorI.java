package xl.test.framework.springboot.aop.proxy.cglib;


import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @author XUAN
 * @since 2019/12/30
 */
public class MyMethodInterceptorI implements MethodInterceptor {

    /**
     *
     * @param target        代理对象
     * @param method        被代理类的方法
     * @param args          调用参数
     * @param methodProxy   代理类对方法的代理引用?
     * @return
     * @throws Throwable
     */
    @Override
    public Object intercept(Object target, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        System.out.println("MyMethodInterceptorI.invoke before");
        // 调用代理类实例上的proxy方法的父类方法，即被代理对象中的方法
        Object result = methodProxy.invokeSuper(target, args);
        // 也可以使用Method来反射调用
        System.out.println("MyMethodInterceptorI.invoke after");
        return result;
    }
}
