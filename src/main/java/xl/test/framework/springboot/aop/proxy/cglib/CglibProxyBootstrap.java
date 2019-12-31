package xl.test.framework.springboot.aop.proxy.cglib;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.cglib.core.DebuggingClassWriter;
import org.springframework.cglib.proxy.Callback;
import org.springframework.cglib.proxy.Dispatcher;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.FixedValue;
import org.springframework.cglib.proxy.InvocationHandler;
import org.springframework.cglib.proxy.LazyLoader;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.NoOp;
import xl.test.common.entity.User;
import xl.test.common.service.UserService;
import xl.test.framework.springboot.aop.proxy.UserServiceForAopImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * @author XUAN
 * @since 2019/12/30
 */
public class CglibProxyBootstrap {

    private Enhancer enhancer = null;

    private boolean execAfter = true;

    @Before
    public void createEnhancer() {
        // 输出class文件
        System.setProperty(DebuggingClassWriter.DEBUG_LOCATION_PROPERTY, "D:\\classes");
        // 创建增强器
        enhancer = new Enhancer();
        // 设置目标对象
        // callback是MethodInterceptor. 则必须是实现类, 否则报找不到方法
        // callback是Dispatcher, 则可以是接口, 因为Dispatcher#loadObject返回代理类, 每次发生对原方法的调用时都会被调用并返回一个代理对象来调用原方法. 即Spring中的Prototype类型
        // callback是LazyLoader, 则可以是接口, 因为LazyLoader#loadObject返回代理类, 此callback返回的代理类会被缓存起来. 即Spring中的Singleton类型
        // callback是FixedValue，直接返回原方法调用想要的结果。也就是说不会调用原方法了
        // callback是NoOp, 则啥也不干, 直接调用原方法
        // callback是InvocationHandler
        enhancer.setSuperclass(UserServiceForAopImpl.class);
    }

    @Test
    public void dispatcher() {
        // 设置callback
        enhancer.setCallback(new Dispatcher(){
            @Override
            public Object loadObject() throws Exception {
                // 返回代理类对象
                return new InnerUserServiceImpl();
            }
        });
    }

    @Test
    public void lazyLoader() {
        enhancer.setCallback(new LazyLoader() {
            @Override
            public Object loadObject() throws Exception {
                return new InnerUserServiceImpl();
            }
        });
    }

    @Test
    public void fixedValue() {
        enhancer.setCallbacks(new Callback[]{new FixedValue() {
            @Override
            public Object loadObject() throws Exception {
                System.out.println("FixedValue[0] invoked, return null");
                return null;
            }
        }, new FixedValue() {
            @Override
            public Object loadObject() throws Exception {
                System.out.println("FixedValue[1] invoked, return ArrayList");
                return new ArrayList<>();
            }
        }});
        // 注册callback过滤器, 当有多个Callback时, 使用过滤器选择其中一个
        enhancer.setCallbackFilter(new MyCallbackFilter());
    }

    /**
     * 最常用的MethodInterceptor
     *
     * 代理类的所有方法调用都会转而执行这个接口中的intercept方法而不是原方法。
     * 如果需要在intercept方法中执行原方法可以使用参数method进行反射调用或者使用参数proxy，后者会快一些（反射调用比正常的方法调用的速度慢很多）。
     * MethodInterceptor允许我们完全控制被拦截的方法，并且提供了手段对原方法进行调用，那为什么还会有其它的Callback接口实现呢？
     * 因为 MethodInterceptor的效率不高，它需要产生不同类型的字节码，并且需要生成一些运行时对象（InvocationHandler就不需要），所以Cglib提供了其它的接口供我们选择。
     *
     * TODO: 这方法连被代理类都没有设置, 是怎么代理的? 难道自己创建的代理类?
     */
    @Test
    public void methodInterceptor() {
        enhancer.setCallbacks(new MethodInterceptor[]{new MyMethodInterceptorI(), new MyMethodInterceptorII()});
        enhancer.setCallbackFilter(new MyCallbackFilter());
    }

    /**
     * 和jdk 的InvocationHandler一模一样
     * 可以随意变换被代理类实例
     */
    @Test
    public void invocationHandler() {
        UserService userService= new UserServiceForAopImpl();
        InvocationHandler callback = new MyInvocationHandler(userService);
        // 设置callback
        enhancer.setCallback(callback);

        // 创建代理类
        UserService proxyUserService = (UserService)enhancer.create();
        // 被代理类是UserServiceForAopImpl时打印
        proxyUserService.save(User.getRandomUser());
        System.out.println();
        proxyUserService.listUser();

        System.out.println();
        System.out.println();
        System.out.println();

        // 被代理类是InnerUserServiceImpl时打印
        ((MyInvocationHandler) callback).setTarget(new InnerUserServiceImpl());
        proxyUserService.save(User.getRandomUser());
        System.out.println();
        proxyUserService.listUser();

        // 不执行@After
        execAfter = false;
    }

    @Test
    public void noOp() {
        enhancer.setCallback(NoOp.INSTANCE);
    }

    @After
    public void print() {
        if (!execAfter) {
            return;
        }
        // 创建代理对象
        UserService userService = (UserService)enhancer.create();
        // 调用代理方法
        userService.save(User.getRandomUser());
        System.out.println();
        userService.listUser();
        System.out.println();
        System.out.println();
        System.out.println();
    }

    private static class InnerUserServiceImpl extends UserServiceForAopImpl {
        @Override
        public void save(User user) {
            super.save(user);
            System.out.println("this UserService implements by Dispatcher, and save method is invoked");
        }
        @Override
        public List<User> listUser() {
            List<User> users = super.listUser();
            System.out.println("this UserService implements by Dispatcher, and listUser method is invoked");
            return users;
        }
    }
}
