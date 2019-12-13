package xl.test.javabasic.reflection;

/**
 * created by XUAN on 2019/12/12
 */
public class ReflectionInstance extends ReflectionTemple<ReflectionInstance> {

    @Override
    public int compareTo(ReflectionInstance o) {
        return this.getCode() - o.getCode();
    }

    public void myMethod(String name) {
        System.out.println(name + "调用了myMethod");
    }
}
