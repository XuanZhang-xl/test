package xl.test.framework.springboot.aop.advicechain;

import xl.test.framework.springboot.aop.TargetWrapper;
import xl.test.framework.springboot.aop.advicechain.advice.XAdvice;

import java.lang.reflect.Method;
import java.util.List;

/**
 * created by XUAN on 2020/1/2
 */
public class MethodInvocationImpl implements XMethodInvocation {

    // 被代理类的class
    private Class<?> targetClass;
    // 被代理的method
    private Method method;
    // 被代理的method的参数
    private Object[] arguments = new Object[0];
    // 被代理类实例
    private Object target;
    // 所有的增强器
    private List<XAdvice> advices = null;

    // 当前增强器索引
    private int currentAdviceIndex = -1;

    @Override
    public Object proceed() throws Exception {
        // 如果增强器全部遍历完, 则直接调用被代理类方法
        if (this.currentAdviceIndex == this.advices.size() - 1) {
            return method.invoke(target, arguments);
        }
        // 获得下一个增强器, 调用增强方法
        return advices.get(++currentAdviceIndex).invoke(this);
    }

    public MethodInvocationImpl(Class<?> targetClass, Method method, Object[] arguments, Object target, List<XAdvice> advices) {
        if (targetClass == null) {
            throw new IllegalArgumentException("targetClass不能为空");
        }
        if (method == null) {
            throw new IllegalArgumentException("method不能为空");
        }
        if (target == null) {
            throw new IllegalArgumentException("target不能为空");
        }
        if (advices == null) {
            throw new IllegalArgumentException("advices不能为空");
        }
        this.targetClass = targetClass;
        this.method = method;
        this.arguments = arguments;
        this.target = target;
        this.advices = advices;
    }
}
