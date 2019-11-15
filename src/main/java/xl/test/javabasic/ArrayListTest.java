package xl.test.javabasic;

import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * created by XUAN on 2018/12/5
 */
public class ArrayListTest {

    public static void main(String[] args){
        //ArrayList<Integer> integers = new ArrayList<Integer>() {
        //    @Override
        //    public boolean add(Integer integer) {
        //        return super.add(integer);
        //    }
        //
        //    {
        //        add(1);
        //    }
        //};
        //System.out.println(integers);


        int i = 2;
        Integer i2 = new Integer(100);
        Integer i3 = new Integer(100);
        System.out.println(i2.equals(i3));
        System.out.println(i2 == i3);


        System.out.println("---------------------");

        List<String> stringList = new ArrayList<>();
        stringList.add("a");
        stringList.add("b");
        stringList.add("c");
        stringList.add("d");

        String[] strings = stringList.toArray(new String[stringList.size()]);
        System.out.println(JSONObject.toJSONString(strings));


    }
}
