package xl.test.javabasic.reflection;

/**
 * 用于反射测试
 * created by XUAN on 2019/12/12
 */
public abstract class ReflectionTemple<T> implements Comparable<T> {

    private T data;

    private String message;

    private int code;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public ReflectionTemple(T data, String message, int code) {
        this.data = data;
        this.message = message;
        this.code = code;
    }

    public ReflectionTemple() {
    }
}
