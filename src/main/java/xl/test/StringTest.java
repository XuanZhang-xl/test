package xl.test;

/**
 * created by XUAN on 2019/05/07
 */
public class StringTest {

    public static void main(String[] args) {
        String a = "a";

        String a2 = new String(a);

        System.out.println(a == a2);
        System.out.println(a.equals(a2));
    }


}
