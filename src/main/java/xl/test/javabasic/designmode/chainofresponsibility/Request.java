package xl.test.javabasic.designmode.chainofresponsibility;

/**
 * 模拟请求
 * created by XUAN on 2020/1/9
 */
public class Request {
    private RequestType type;
    private String name;


    public Request(RequestType type, String name) {
        this.type = type;
        this.name = name;
    }


    public RequestType getType() {
        return type;
    }


    public String getName() {
        return name;
    }
}
