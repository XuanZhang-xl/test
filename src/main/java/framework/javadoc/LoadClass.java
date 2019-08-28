package framework.javadoc;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * created by zhangxuan9 on 2019/2/19
 */
public class LoadClass {


    public static void main(String[] args) throws MalformedURLException, ClassNotFoundException {
        ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();
        //获取路径classes的路径
        String classPath = "C:\\Users\\zhangxuan9\\IdeaProjects\\test\\src\\main\\java\\framework.javadoc\\StaticProperty.java";
        String prefix = classPath.substring(0,classPath.indexOf("classes")+8);
        //这里得到的就是com.company.base.controller包的全名
        String className = "framework.javadoc.StaticProperty";

        URL classes = new URL("framework.javadoc:///"+prefix);
        ClassLoader custom = new URLClassLoader(new URL[]{classes}, systemClassLoader);
        Class<?> clazz = custom.loadClass(className);
        System.out.println(clazz);
    }
}
