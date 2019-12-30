package xl.test.framework.springboot.aop.proxy.cglib;


import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @author XUAN
 * @since 2019/12/30
 */
public class MyMethodInterceptor implements MethodInterceptor {

    /**
     *
     * @param target        目标对象
     * @param method        ?
     * @param args          调用参数
     * @param methodProxy   目标对象的方法?
     * @return
     * @throws Throwable
     */
    @Override
    public Object intercept(Object target, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        System.out.println("MyMethodInterceptor.invoke before");
        Object result = methodProxy.invokeSuper(target, args);
        System.out.println("MyMethodInterceptor.invoke after");
        return result;
    }
}
