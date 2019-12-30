package xl.test.framework.springboot.aop;

/**
 * created by XUAN on 2019/12/30
 */
public class AopTarget {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void print() {
        System.out.println("print() invoked");
    }
    public void print(String str) {
        System.out.println(str);
    }
}
