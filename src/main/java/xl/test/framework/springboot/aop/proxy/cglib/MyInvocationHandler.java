package xl.test.framework.springboot.aop.proxy.cglib;

import org.springframework.cglib.proxy.InvocationHandler;

import java.lang.reflect.Method;

/**
 * created by XUAN on 2019/12/31
 */
public class MyInvocationHandler implements InvocationHandler {

    private Object target;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("MyInvocationHandler.invoke before");
        Object result = method.invoke(target, args);
        System.out.println("MyInvocationHandler.invoke after");
        return result;
    }


    public Object getTarget() {
        return target;
    }

    public void setTarget(Object target) {
        this.target = target;
    }

    public MyInvocationHandler(Object target) {
        this.target = target;
    }
}
