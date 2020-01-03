package xl.test.framework.springboot.aop.staticproxy;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;

/**
 * 这部分代码已搬运到 start 项目中, 这里暂时不删
 * created by XUAN on 2020/1/2
 */
public class PerfMonAgent {

    private static Instrumentation inst = null;

    public static void premain(String agentArgs, Instrumentation _inst) {
        System.out.println("PerfMonAgent.premain() was called");
        inst = _inst;
        ClassFileTransformer trans = new PerfMonXformer();
        System.out.println("Adding a PerfMonXformer instance to the JVM");
        inst.addTransformer(trans);
    }
}
