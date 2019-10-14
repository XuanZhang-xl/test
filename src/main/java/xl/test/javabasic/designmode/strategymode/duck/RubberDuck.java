package xl.test.javabasic.designmode.strategymode.duck;

import xl.test.javabasic.designmode.strategymode.behavior.FlyBehavior;

/**
 * 橡胶鸭
 * created by XUAN on 2019/04/14
 */
public class RubberDuck extends AbstractDuck {
    @Override
    public void fly() {
        super.flyBehavior.fly();
    }

    public RubberDuck(FlyBehavior flyBehavior) {
        super.flyBehavior = flyBehavior;
    }
}
