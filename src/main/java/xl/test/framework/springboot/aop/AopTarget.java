package xl.test.framework.springboot.aop;

/**
 * created by XUAN on 2019/12/30
 */
public class AopTarget implements TargetWrapper {

    private String target = "给个默认值吧";

    @Override
    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public void print() {
        System.out.println("print() invoked");
    }
    public void print(String str) {
        System.out.println(str);
    }
}
