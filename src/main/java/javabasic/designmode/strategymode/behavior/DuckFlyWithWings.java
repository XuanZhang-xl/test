package javabasic.designmode.strategymode.behavior;

/**
 * created by XUAN on 2019/04/14
 */
public class DuckFlyWithWings extends DuckFlyBehavior {

    @Override
    public void fly() {
        super.fly();
        System.out.println("but, not far");
    }
}
