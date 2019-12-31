package xl.test.framework.springboot.aop.proxy.cglib;

import org.springframework.cglib.proxy.CallbackFilter;

import java.lang.reflect.Method;

/**
 * created by XUAN on 2019/12/31
 */
public class MyCallbackFilter implements CallbackFilter {

    @Override
    public int accept(Method method) {
        if (method.getParameterCount() > 0) {
            // 有参数的方法, 使用 Callback[0]  第0个Callback
            return 0;
        } else {
            // 没参数的方法, 使用 Callback[1]  第1个Callback
            return 1;
        }
    }
}
