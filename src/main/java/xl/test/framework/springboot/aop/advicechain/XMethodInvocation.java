package xl.test.framework.springboot.aop.advicechain;

import xl.test.framework.springboot.aop.advicechain.advice.XAdvice;

/**
 * created by XUAN on 2020/1/2
 */
public interface XMethodInvocation {

    /**
     * advice调用的协调方法
     * @return
     * @throws Throwable
     */
    public Object proceed() throws Exception;

}
