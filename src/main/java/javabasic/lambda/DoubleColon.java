package javabasic.lambda;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Consumer;

/**
 * 双冒号
 * created by zhangxuan9 on 2019/3/8
 */
public class DoubleColon {

    public static void main(String[] args){
        Consumer<String> printStrConsumer = DoubleColon::printStr;
        printStrConsumer.accept("printStrConsumer");

        // 非静态方法, 要传入实例
        Consumer<DoubleColon> toUpperConsumer = DoubleColon::toUpper;
        toUpperConsumer.accept(new DoubleColon());

        // 传入实例及参数
        BiConsumer<DoubleColon, String> toLowerConsumer = DoubleColon::toLower;
        toLowerConsumer.accept(new DoubleColon(),"toLowerConsumer");

        // 传入实例, 参数, 返回值
        BiFunction<DoubleColon, String, Integer> toIntFunction = DoubleColon::toInt;
        int i = toIntFunction.apply(new DoubleColon(),"toInt");

        BiPredicate<DoubleColon, Boolean> toBoolean = DoubleColon::toBoolean;
        toBoolean.test(new DoubleColon(), true);
        BiFunction<DoubleColon, Boolean, Boolean> toBoolean2 = DoubleColon::toBoolean;
        toBoolean2.apply(new DoubleColon(), true);

    }

    public static void printStr(String str) {
        System.out.println("printStr : " + str);
    }

    public void toUpper(){
        System.out.println("toUpper : " + this.toString());
    }

    public void toLower(String str){
        System.out.println("toLower : " + str);
    }

    public int toInt(String str){
        System.out.println("toInt : " + str);
        return 1;
    }

    public Boolean toBoolean(Boolean b){
        System.out.println("toBoolean : " + b);
        return !b;
    }
}
