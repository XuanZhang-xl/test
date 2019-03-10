package test;

/**
 * created by zhangxuan9 on 2019/2/13
 */
public class TestString {

    public static void main(String[] args){
        String str1 = "abc";
        String str2 = "abc";
        String str3 = new String("abc");
        String str4 = new String("abc");

        System.out.println(str1 == str2);           //true
        System.out.println(str1 == str3);           //false
        System.out.println(str3 == str4);           //false
        System.out.println(str3.equals(str4));      //true
    }
}
