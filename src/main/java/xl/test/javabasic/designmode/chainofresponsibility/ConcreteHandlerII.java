package xl.test.javabasic.designmode.chainofresponsibility;

/**
 * created by XUAN on 2020/1/9
 */
public class ConcreteHandlerII extends Handler {

    public ConcreteHandlerII(Handler successor) {
        super(successor);
    }


    @Override
    protected void handleRequest(Request request) {
        if (request.getType() == RequestType.TYPE2) {
            System.out.println(request.getName() + " is handle by ConcreteHandlerII");
            return;
        }
        if (successor != null) {
            successor.handleRequest(request);
        }
    }
}
