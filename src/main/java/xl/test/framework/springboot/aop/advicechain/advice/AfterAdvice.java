package xl.test.framework.springboot.aop.advicechain.advice;

import xl.test.framework.springboot.aop.advicechain.AdviceMethodContainer;
import xl.test.framework.springboot.aop.advicechain.XMethodInvocation;

import java.lang.reflect.Method;

/**
 * 后置增强器
 * created by XUAN on 2020/1/2
 */
public class AfterAdvice implements XAdvice {

    @Override
    public Object invoke(XMethodInvocation invocation) throws Exception {
        try {
            // 先执行下一个增强器
            return invocation.proceed();
        }
        finally {
            // 后执行代理方法
            Method adviceMethod = AdviceMethodContainer.class.getMethod("after", null);
            adviceMethod.invoke(AdviceMethodContainer.getInstance(), null);
        }
    }
}
