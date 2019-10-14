package xl.test.algorithm;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * created by zhangxuan9 on 2019/2/22
 */
public class NumberMatch {


    public static void main(String[] args){
        List<Integer> listA = Arrays.asList(5,8,18,0,8,12);
        List<Integer> listB = Arrays.asList(1,8,0,5,18,3,10);
        List<Integer> result = new ArrayList<>();

        Iterator<Integer> iteratorA = listA.iterator();
        Iterator<Integer> iteratorB = listB.iterator();
        Integer A = 0;
        Integer B = 0;
        while (true) {
            if (A <= 0) {
                if (iteratorA.hasNext()) {
                    A = iteratorA.next();
                } else {
                    break;
                }
            }
            if (B <= 0) {
                if (iteratorB.hasNext()) {
                    B = iteratorB.next();
                } else {
                    break;
                }
            }

            if (A > B) {
                if (B > 0) {
                    result.add(B);
                }
                A = A - B;
                B = 0;
            } else if (A == B) {
                if (B > 0) {
                    result.add(B);
                }
                A = 0;
                B = 0;
            } else {
                if (A > 0) {
                    result.add(A);
                }
                B = B - A;
                A = 0;
            }
        }
        System.out.println(result);
    }

    @Test
    public void test1 () {
        List<Integer> listA = Arrays.asList(5,8,18,0,8,12);
        List<Integer> listB = Arrays.asList(1,8,0,5,18,3,10);
        List<Integer> result = new ArrayList<>();

        boolean next = true;
        Integer B = 0;
        Iterator<Integer> iteratorB = listB.iterator();
        for (Integer A : listA) {
            if (next) {
                next = false;
                if (iteratorB.hasNext()) {
                    B = iteratorB.next();
                } else {
                    break;
                }
            }
            if (A > B) {
                if (B > 0) {
                    result.add(B);
                }
                A = A - B;
                B = 0;
            } else if (A == B) {
                if (B > 0) {
                    result.add(B);
                }
                A = 0;
                B = 0;
            } else {
                if (A > 0) {
                    result.add(A);
                }
                B = B - A;
                A = 0;
            }
        }



    }


}
