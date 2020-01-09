package xl.test.javabasic.designmode.chainofresponsibility;

/**
 * created by XUAN on 2020/1/9
 */
public class ChainOfResponsibilityBootstrap {

    public static void main(String[] args){
        Handler handler1 = new ConcreteHandlerI(null);
        Handler handler2 = new ConcreteHandlerII(handler1);

        Request request1 = new Request(RequestType.TYPE1, "request1");
        handler2.handleRequest(request1);

        Request request2 = new Request(RequestType.TYPE2, "request2");
        handler2.handleRequest(request2);
    }
}
