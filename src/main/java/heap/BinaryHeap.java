package heap;

import java.util.Arrays;

/**
 * 二叉堆
 * created by XUAN on 2019/03/10
 */
public class BinaryHeap<T extends Comparable> {

    /**
     * 容量
     */
    private int capacity;

    /**
     * 当前元素数量
     */
    private int currentSize;

    /**
     * 堆对象
     */
    private T[] items;

    /**
     * 初始容量
     */
    private static final int DEFAULT_CAPACITY = 128;

    /**
     * 假设插在最后面, 从后往上比较
     * @param item
     */
    public void insert(T item) {
        if (currentSize == items.length - 1) {
            enlargeArray(items.length << 1 + 1);
        }
        int hole = ++ currentSize;
        for (items[0] = item; item.compareTo(items[hole >> 1]) < 0; hole /= 2) {
            items[hole] = items[hole >> 1];
        }
        items[hole] = item;
    }

    /**
     * 找到最小元素
     * @return 最小元素
     */
    public T findMin () {
        return items[1];
    }

    /**
     * 删除堆顶元素
     * @return 删除的元素
     */
    public T deleteMin () {
        if (isEmplty()) {
            throw new NullPointerException();
        }
        T min = findMin();
        items[1] = items[currentSize--];
        percolateDown(1);
        return min;
    }

    /**
     * 是否为空堆
     * @return
     */
    public boolean isEmplty () {
        return currentSize == 0;
    }

    /**
     * 空堆
     */
    public void makeEmpty () {
        for (T item : items) {
            item = null;
        }
        currentSize = 0;
    }

    /**
     * 下滤, 假设要修正在hold这个点上的变量,则将他和他的小的那个子元素比较, 如果小的子元素小, 则将子元素往上移,将他往下移,直到比小的子元素大或到底为止
     * @param hole
     */
    private void percolateDown (int hole) {
        int child;
        T tmp = items[hole];
        for (; hole << 1 <= currentSize; hole = child) {
            child = hole << 1;
            // 不是末尾元素且 选出左右子元素中小的那个
            if (child != currentSize && items[child + 1].compareTo(items[child]) < 0) {
                child++;
            }
            // 和小的子元素比
            if (items[child].compareTo(tmp) < 0) {
                items[hole] = items[child];
            } else {
                break;
            }
        }
        items[hole] = tmp;
    }

    /**
     * 建堆
     */
    private void buildHeap () {
        for (int i = currentSize >> 1; i > 0; i--) {
            percolateDown(i);
        }
    }

    /**
     * 扩容
     */
    private void enlargeArray (int newSize) {
        items = Arrays.copyOf(items, capacity << 1);
        capacity = items.length;
    }

    public BinaryHeap(T[] originalItems) {
        this.currentSize = originalItems.length;
        this.items = (T[])new Comparable[(currentSize + 2) * 11 / 10];
        int i = 1;
        for (T item : originalItems) {
            this.items[i++] = item;
        }
        this.capacity = this.items.length;
        buildHeap();
    }

    public BinaryHeap() {
        this.currentSize = 0;
        this.capacity = DEFAULT_CAPACITY;
        this.items = (T[])new Comparable[DEFAULT_CAPACITY];
    }


    public static void main(String[] args){
        //Integer[] items = new Integer[]{13, 14, 16, 19, 21, 19, 68, 65, 26, 32, 31};
        //Integer[] items = new Integer[]{113, 104, 6, 129, 210, 9, 6, 15, 1826, 362, 31};
        Integer[] items = new Integer[]{113, 104, 100, 29, 10, 9, 6, 5, 4, 3, 1};
        BinaryHeap<Integer>  heap = new BinaryHeap<Integer>(items);
        for (int i = 0; i < items.length; i++) {
            System.out.println(heap.deleteMin());
        }
    }
}
