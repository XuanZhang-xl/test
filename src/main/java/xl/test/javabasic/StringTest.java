package xl.test.javabasic;

/**
 * 为什么 String 类被 final 修饰
 *
 * 字符串池的需求
 * 字符串池(String intern pool)是方法区域中的一个特殊存储区域。当创建一个字符串时，如果该字符串已经存在于池中，那么返回现有字符串的引用，而不是创建一个新对象。所以说，如果一个字符串是可变的，那么改变一个引用的值，将导致原本指向该值的引用获取到错误的值
 *
 * 缓存 hashcode
 * 字符串的hashcode在Java中经常使用。例如，在HashMap或HashSet中。不可变保证hashcode始终是相同的，这样就可以在不担心更改的情况下兑现它。这意味着，不需要每次使用hashcode时都计算它。这样更有效率。所以你会在 String 类中看到下面的成员变量的定义:
 *  Cache the hash code for the string
 *  private int hash; // Default to
 *
 * 安全性
 * String被广泛用作许多java类的参数，例如网络连接、打开文件等。如果字符串不是不可变的，连接或文件将被更改，这可能导致严重的安全威胁。该方法认为它连接到一台机器上，但实际上并没有。可变字符串也可能导致反射中的安全问题，因为参数是字符串。
 *
 * 不可变对象天生是线程安全的
 * 由于不可变对象不能被更改，所以它们可以在多个线程之间自由共享。这消除了同步的需求。
 *
 * 总之，出于效率和安全性的考虑，String 被设计为不可变的。这也是为什么在一般情况下，不可变类是首选的原因。
 * created by zhangxuan9 on 2019/2/13
 */
public class StringTest {

    public static void main(String[] args){
        String str1 = "abc";
        String str2 = "abc";

        //如果方法区已存在"abc", 那么只创建一个 new String 的对象
        //如果方法区没有"abc", 那么要创建两个对象，一个在方法区，一个在堆中
        // 这里只创建了一个堆中的对象
        String str3 = new String("abc");
        String str4 = new String("abc");

        System.out.println(str1 == str2);           //true
        System.out.println(str1 == str3);           //false
        System.out.println(str3 == str4);           //false
        System.out.println(str3.equals(str4));      //true

        // intern()方法在不同的jdk是不一样的 TODO
        String c = new String("abc").intern();
        String d = new String("abc").intern();
        System.out.println(c == d);  // true
        System.out.println(c.equals(d)); // True
    }
}
