package xl.test.framework.springboot.aop.advicechain;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * 增强方法存储类
 * 相当于这是个标注 @Aspectj 的类
 * created by XUAN on 2020/1/2
 */
public class AdviceMethodContainer {


    public void before(){
        System.out.println("AdviceMethodContainer#before");
    }


    public void after(){
        System.out.println("AdviceMethodContainer#after");
    }


    public Object around(XMethodInvocation p) throws Exception {
        System.out.println("AdviceMethodContainer#around begin");
        Object proceed = p.proceed();
        System.out.println("AdviceMethodContainer#around end");
        return proceed;
    }

    public static AdviceMethodContainer getInstance() {
        return new AdviceMethodContainer();
    }

    private AdviceMethodContainer() {
    }

    public static void main(String[] args){
        for (Method method : AdviceMethodContainer.class.getMethods()) {
            System.out.println(method);
            Parameter[] parameters = method.getParameters();
            if (parameters != null && parameters.length > 0) {
                for (Parameter parameter : parameters) {
                    System.out.println(parameter.getType());
                }
            }
            System.out.println();
        }
    }
}
