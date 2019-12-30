package xl.test.framework.springboot.aop.proxy.cglib;

import org.springframework.cglib.proxy.Enhancer;
import xl.test.common.entity.User;
import xl.test.common.service.UserService;
import xl.test.framework.springboot.aop.proxy.UserServiceForAopImpl;

/**
 * @author XUAN
 * @since 2019/12/30
 */
public class CglibProxyInterceptor {

    public static void main(String[] args) {
        // 创建增强器
        Enhancer enhancer = new Enhancer();
        // 设置目标对象, 必须是具体的实现类, 因为cglib是对类代理
        enhancer.setSuperclass(UserServiceForAopImpl.class);
        enhancer.setCallback(new MyMethodInterceptor());
        // 创建代理对象
        UserService userService = (UserService)enhancer.create();
        userService.save(User.getRandomUser());
    }
}
