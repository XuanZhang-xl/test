package xl.test.framework.springboot.aop.advicechain.advice;

import xl.test.framework.springboot.aop.advicechain.XMethodInvocation;

/**
 * created by XUAN on 2020/1/2
 */
public interface XAdvice {

    /**
     * 调用增强器 / XMethodInvocation.process()已进行下一个增强器的处理
     * @param invocation
     * @return
     * @throws Throwable
     */
    Object invoke(XMethodInvocation invocation) throws Exception;

}
