package xl.test.javabasic.designmode.strategymode.behavior;

/**
 * created by XUAN on 2019/04/14
 */
public class DuckFlyBehavior implements FlyBehavior{

    @Override
    public void fly() {
        System.out.println("duck flying");
    }
}
