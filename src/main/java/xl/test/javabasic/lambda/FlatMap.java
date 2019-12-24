package xl.test.javabasic.lambda;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * created by XUAN on 2019/12/24
 */
public class FlatMap {

    @Test
    public void flatMap() {
        String mark = "!";
        String[] words = new String[]{"Hello","World", mark, mark};
        List<String[]> a = Arrays.stream(words)
                .map(word -> word.split(""))
                // 感叹号去不掉???为啥?
                .distinct()
                .collect(Collectors.toList());
        a.forEach(FlatMap::printArray);

        System.out.println();
        System.out.println();
        System.out.println();

        List<String> collect = Arrays.stream(words)
                .map(word -> word.split(""))
                // flatMap是把多个流合并成一个流, Arrays.stream  Collection.stream
                .flatMap(FlatMap::stream)
                .distinct()
                .collect(Collectors.toList());
        collect.forEach(System.out::print);

        System.out.println();
        System.out.println();
        System.out.println();

        /*******************下面是重点*******************/
        List<MyStaticClass> classList = new ArrayList<>();
        classList.add(new MyStaticClass("a"));
        classList.add(new MyStaticClass("b"));
        classList.add(new MyStaticClass("c"));

        Set<String> resultSet = classList.stream()
                // 获得每个MyStaticClass的set属性, 由于方法返回类型是Set<String>, 所以总类型变成了Stream<Set<String>>
                // map方法有两种情况:
                // 1. 如果调用的方法是迭代元素自身的方法, 则这个方法可以没有入参, 必须是非静态方法, 现在是这种情况
                // 2. 调用的方法不是自身的方法, 则必须是静态方法且入参为当前迭代元素
                .map(MyStaticClass::getExclusions)
                // 扁平化, 将所有set合起来, 由于返回的是Set类型, 合并的时候天然地会去重
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
        resultSet.forEach(System.out::println);
    }

    public static class MyStaticClass{

        Set<String> set = new LinkedHashSet<>();

        String symbol = "";

        private Set<String> getExclusions() {
            set.add(symbol + "1");
            set.add(symbol + "2");
            set.add(symbol + "3");
            set.add("4");
            return set;
        }

        public MyStaticClass(String symbol) {
            this.symbol = symbol;
        }
    }


    public static void printArray(String[] array) {
        for (int i = 0; i < array.length; i++) {
            System.out.print(array[i]);
        }
        System.out.println();
    }

    public static <T> Stream<T> stream(T[] array) {
        System.out.println("处理前:");
        for (T t : array) {
            System.out.print(t);
        }
        System.out.println();
        return Arrays.stream(array);
    }
}
