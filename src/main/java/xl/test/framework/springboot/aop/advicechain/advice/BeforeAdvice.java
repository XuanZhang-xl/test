package xl.test.framework.springboot.aop.advicechain.advice;

import xl.test.framework.springboot.aop.advicechain.AdviceMethodContainer;
import xl.test.framework.springboot.aop.advicechain.XMethodInvocation;

import java.lang.reflect.Method;

/**
 * 前置增强器
 * created by XUAN on 2020/1/2
 */
public class BeforeAdvice implements XAdvice {

    @Override
    public Object invoke(XMethodInvocation invocation) throws Exception {
        // 先执行代理方法
        Method adviceMethod = AdviceMethodContainer.class.getMethod("before", null);
        adviceMethod.invoke(AdviceMethodContainer.getInstance(), null);
        // 后执行下一个增强器
        return invocation.proceed();
    }
}
