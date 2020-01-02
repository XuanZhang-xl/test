package xl.test.framework.springboot.aop.advicechain.advice;

import xl.test.framework.springboot.aop.advicechain.AdviceMethodContainer;
import xl.test.framework.springboot.aop.advicechain.XMethodInvocation;

import java.lang.reflect.Method;

/**
 * 环绕增强器
 * created by XUAN on 2020/1/2
 */
public class AroundAdvice implements XAdvice {

    @Override
    public Object invoke(XMethodInvocation invocation) throws Exception {
        // 直接调用增强方法, 并传入XMethodInvocation即可
        Method adviceMethod = AdviceMethodContainer.class.getMethod("around", new Class[]{invocation.getClass().getInterfaces()[0]});
        // Method adviceMethod = AdviceMethodContainer.class.getMethod("around", XMethodInvocation.class);
        return adviceMethod.invoke(AdviceMethodContainer.getInstance(), new Object[]{invocation});
    }
}
