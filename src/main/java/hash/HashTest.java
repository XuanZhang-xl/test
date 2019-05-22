package hash;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * created by zhangxuan9 on 2019/2/27
 */
public class HashTest {

    public static void main(String[] args){
        List<Object> objList = new ArrayList<Object>();
        for (int i = 0; i < 100; i++) {
            objList.add(new Object());
        }

        for (Object o : objList) {
            int h;
            System.out.println("hashCode:" + o.hashCode() + "   h >>> 16:" + (o.hashCode() >>> 16) + "  hashKey:" + ((h = o.hashCode()) ^ (h >>> 16)));
        }
    }

    @Test
    public void testHash () {
        System.out.println("布尔");
        System.out.println(Boolean.TRUE.hashCode());
        Boolean bo = true;
        System.out.println(bo.hashCode());

        System.out.println("int");
        Integer one1 = new Integer(1);
        Integer one2 = new Integer(1);
        Integer two1 = new Integer(2);
        Integer two2 = new Integer(2);
        Integer th1 = new Integer(1000);
        Integer th2 = new Integer(1000);
        System.out.println(one1.hashCode());
        System.out.println(one2.hashCode());
        System.out.println(two1.hashCode());
        System.out.println(two2.hashCode());
        System.out.println(th1.hashCode());
        System.out.println(th2.hashCode());
        System.out.println("byte");
        byte a = 97;
        System.out.println("97的byte为" + a);
        Byte aByte1 = new Byte("97");
        Byte aByte2 = new Byte("97");
        System.out.println(aByte1.hashCode());
        System.out.println(aByte2.hashCode());
    }


    @Test
    public void test() {
        System.out.println(31 & 16);
        System.out.println(1 & 1);
        System.out.println(3 & 1);
        System.out.println(7 & 1);
        System.out.println(15 & 1);
        System.out.println(63 & 1);
        double v = 1 - 0.9;
        System.out.println(v);
        System.out.println((byte)v);
    }
    @Test
    public void testTableSizeFor() {
        for (int i = 0; i < 16; i++) {
            System.out.println(i + "   " + tableSizeFor(i));
        }
    }

    /**
     * 得到的一定是2的幂次方，并且是那个离cap最近的2的幂次方
     * @param cap
     * @return
     */
    int tableSizeFor(int cap) {
        int n = cap - 1;
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        return (n < 0) ? 1 : (n >= Integer.MAX_VALUE) ? Integer.MAX_VALUE : n + 1; }
}
