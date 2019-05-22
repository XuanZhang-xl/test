package interview;

import java.util.Map;

/**
 * created by XUAN on 2019/04/27
 */
public interface PointerTest {


    public static void main(String[] args) {
        Map.Entry entry1 = new Map.Entry(){

            @Override
            public Object getKey() {
                return 1;
            }

            @Override
            public Object getValue() {
                return 1;
            }

            @Override
            public Object setValue(Object value) {
                return 1;
            }
        };
        Map.Entry entry2 = new Map.Entry(){

            @Override
            public Object getKey() {
                return 2;
            }

            @Override
            public Object getValue() {
                return 2;
            }

            @Override
            public Object setValue(Object value) {
                return 2;
            }
        };


        Map.Entry initEntry = entry1;
        Map.Entry parent = initEntry;
        initEntry = entry2;


        System.out.println(parent.getKey());


    }
}
