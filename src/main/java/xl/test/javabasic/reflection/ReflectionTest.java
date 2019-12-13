package xl.test.javabasic.reflection;

import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * created by XUAN on 2019/12/12
 */
public class ReflectionTest {


    @Test
    public void methodReflection () throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Class<ReflectionInstance> reflectionTempleClass = ReflectionInstance.class;
        System.out.println(reflectionTempleClass.getName());

        Method myMethod = reflectionTempleClass.getMethod("myMethod", String.class);
        if (myMethod != null) {
            myMethod.invoke(new ReflectionInstance(), "xl");
        }
    }
}
