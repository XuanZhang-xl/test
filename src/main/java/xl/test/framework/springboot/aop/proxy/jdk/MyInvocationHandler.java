package xl.test.framework.springboot.aop.proxy.jdk;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * JDK代理用于方法增强
 * @author XUAN
 * @since 2019/12/30
 */
public class MyInvocationHandler implements InvocationHandler {

    /**
     * 目标对象
     */
    private Object target;

    /**
     * 执行目标对象的方法
     * @param proxy   代理对象
     * @param method  目标对象的方法
     * @param args    当前执行的参数
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("MyInvocationHandler.invoke before");
        Object result = method.invoke(target, args);
        System.out.println("MyInvocationHandler.invoke after");
        return result;
    }

    public MyInvocationHandler(Object target) {
        this.target = target;
    }
}
