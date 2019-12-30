package xl.test.framework.springboot.aop.proxy.jdk;

import xl.test.common.entity.User;
import xl.test.common.service.UserService;
import xl.test.framework.springboot.aop.proxy.UserServiceForAopImpl;

import java.lang.reflect.Proxy;

/**
 * @author XUAN
 * @since 2019/12/30
 */
public class JdkProxyBootstrap {

    public static void main(String[] args) {
        // 实例化目标对象
        UserService userService = new UserServiceForAopImpl();
        // 实例化InvocationHandler
        MyInvocationHandler handler = new MyInvocationHandler(userService);
        // 生成代理对象
        UserService proxy = (UserService) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), userService.getClass().getInterfaces(), handler);
        // 调用代理对象方法
        proxy.save(User.getRandomUser());
    }
}
