package designmode.strategymode.behavior;

/**
 * created by XUAN on 2019/04/14
 */
public class DuckNoFly extends DuckFlyBehavior {

    @Override
    public void fly() {
        System.out.println("cannot fly");
    }
}
