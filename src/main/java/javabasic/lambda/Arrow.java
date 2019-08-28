package javabasic.lambda;

import java.util.ArrayList;
import java.util.List;

/**
 * 箭头函数
 * created by zhangxuan9 on 2019/2/12
 */
public class Arrow {


    public static void main(String[] args){
        List<String> strs=new ArrayList<String>();
        strs.add("AbC"); strs.add("Wu");
        //使用lambda表达式输出
        strs.forEach(str->System.out.println(str));
        strs.forEach(System.out::println);

        Runnable r2=()->System.out.println("hello,world");
        // 直接调用 run 方法(没开新线程哦!)
        r2.run();

    }

}
