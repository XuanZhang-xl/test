package xl.test.javabasic.designmode.strategymode.duck;

import xl.test.javabasic.designmode.strategymode.behavior.FlyBehavior;

/**
 * created by XUAN on 2019/04/14
 */
public class MallardDuck extends AbstractNatureDuck {
    @Override
    public void fly() {
        super.flyBehavior.fly();
    }

    public MallardDuck(FlyBehavior flyBehavior) {
        super.flyBehavior = flyBehavior;
    }
}
