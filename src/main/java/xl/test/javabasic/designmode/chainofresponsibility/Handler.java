package xl.test.javabasic.designmode.chainofresponsibility;

/**
 * created by XUAN on 2020/1/9
 */
public abstract class Handler {

    protected Handler successor;


    public Handler(Handler successor) {
        this.successor = successor;
    }


    protected abstract void handleRequest(Request request);
}
