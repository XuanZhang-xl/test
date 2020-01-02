package xl.test.framework.springboot.aop.advicechain;

import xl.test.framework.springboot.aop.TargetWrapper;

/**
 * 被代理类
 * created by XUAN on 2020/1/2
 */
public class AdviceChainTarget implements TargetWrapper {
    @Override
    public Object getTarget() {
        String info = "AdviceChainTarget#getTarget已被调用";
        System.out.println(info);
        return info;
    }
}
