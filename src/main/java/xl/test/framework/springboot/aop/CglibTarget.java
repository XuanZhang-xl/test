package xl.test.framework.springboot.aop;

/**
 * created by XUAN on 2019/12/30
 */
class CglibTarget {

    private String target = "cglib";

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public void print() {
        System.out.println(target + "print() invoked");
    }
    public void print(String str) {
        System.out.println(target + str);
    }
}
