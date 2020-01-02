package xl.test.framework.springboot.aop.staticproxy;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtBehavior;
import javassist.CtClass;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

/**
 * created by XUAN on 2020/1/2
 */
public class PerfMonXformer implements ClassFileTransformer {

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        byte[] transformed = null;
        System.out.println("transforming" + className);
        ClassPool pool = ClassPool.getDefault();
        CtClass cl = null;
        try {
            cl = pool.makeClass(new ByteArrayInputStream(classfileBuffer));
            if (cl.isInterface() == false) {
                CtBehavior[] methods = cl.getDeclaredBehaviors();
                for (int i = 0; i < methods.length; i++) {
                    if (!methods[i].isEmpty()) {
                        // 修改字节码
                        doMethod(methods[i]);
                    }
                    transformed = cl.toBytecode();
                }
            }
        } catch (Exception e) {
            System.err.println("could not instrument   " + className + ", exception: " + e.getMessage());
        } finally {
            if (cl != null) {
                cl.detach();
            }
        }

        return transformed;
    }

    private void doMethod(CtBehavior method) throws CannotCompileException {
        method.insertBefore("long stime = System.nanoTime()");
        method.insertAfter("System.out.println(\"leave\" + method.getName() + \" and time: \" + System.nanoTime()-stime);");
    }
}
