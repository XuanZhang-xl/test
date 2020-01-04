package xl.test.framework.springboot.aop.advicechain;

import xl.test.framework.springboot.aop.TargetWrapper;
import xl.test.framework.springboot.aop.advicechain.advice.AfterAdvice;
import xl.test.framework.springboot.aop.advicechain.advice.AroundAdvice;
import xl.test.framework.springboot.aop.advicechain.advice.BeforeAdvice;
import xl.test.framework.springboot.aop.advicechain.advice.XAdvice;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * spring aop中对各增强器的处理方法, 使用的是高级本的迭代器的方式处理的
 *
 * 在这里写个模拟版, 防止忘记
 *
 * created by XUAN on 2020/1/2
 */
public class AdviceChainBootstrap {

    public static void main(String[] args) throws Exception {
        // 被代理类的class
        Class<?> targetClass = TargetWrapper.class;
        // 被代理的method
        Method method = TargetWrapper.class.getDeclaredMethod("getTarget");
        // 被代理的method的参数
        Object[] arguments = new Object[0];
        // 被代理类实例
        Object target = new AdviceChainTarget();
        // 所有的增强器
        List<XAdvice> advices = new ArrayList<>();
        advices.add(new AfterAdvice());
        advices.add(new BeforeAdvice());
        advices.add(new AroundAdvice());
        advices.add(new BeforeAdvice());
        advices.add(new AroundAdvice());
        XMethodInvocation methodInvocation = new MethodInvocationImpl(targetClass, method, arguments, target, advices);
        // 这就是 JdkDynamicAopProxy 和 CglibAopProxy 的 invocation.proceed();
        methodInvocation.proceed();
    }
}
