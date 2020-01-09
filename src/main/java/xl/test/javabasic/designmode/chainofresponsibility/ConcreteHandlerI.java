package xl.test.javabasic.designmode.chainofresponsibility;

/**
 * created by XUAN on 2020/1/9
 */
public class ConcreteHandlerI extends Handler{

    public ConcreteHandlerI(Handler successor) {
        super(successor);
    }


    @Override
    protected void handleRequest(Request request) {
        if (request.getType() == RequestType.TYPE1) {
            System.out.println(request.getName() + " is handle by ConcreteHandlerI");
            return;
        }
        if (successor != null) {
            successor.handleRequest(request);
        }
    }
}
