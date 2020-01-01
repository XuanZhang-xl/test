package xl.test.framework.springboot.aop.proxy.cglib;


import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import xl.test.framework.springboot.aop.proxy.UserServiceForAopAlternativeImpl;
import xl.test.framework.springboot.aop.proxy.UserServiceForAopImpl;

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
        /**
         * 调用代理类上的proxy方法的父类方法，即被代理类中的方法
         * 如果被代理类是接口, 则会报java.lang.NoSuchMethodError, 因为cglib生成的代码是 super.save(var1); super个接口, 自然找不到方法
         *
         * 实际上, 这样的调用方法是静态代理, 可以看到这样调用压根就没有被代理类的实例.
         *
         * 而 methodProxy.invoke(new UserServiceForAopImpl(), args);
         *
         */
        Object result = methodProxy.invokeSuper(target, args);
        // 也可以使用Method来反射调用, 但是这样要提供一个被代理类实例
        methodProxy.invoke(new UserServiceForAopImpl(), args);
        methodProxy.invoke(new UserServiceForAopAlternativeImpl(), args);
        System.out.println("MyMethodInterceptorI.invoke after");
        return result;
    }
}
