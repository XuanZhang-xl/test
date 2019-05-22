package designmode.strategymode;

import designmode.strategymode.behavior.DuckFlyWithWings;
import designmode.strategymode.behavior.DuckNoFly;
import designmode.strategymode.duck.AbstractDuck;
import designmode.strategymode.duck.MallardDuck;
import designmode.strategymode.duck.RubberDuck;

/**
 * 模拟器
 * created by XUAN on 2019/04/14
 */
public class MiniDuckSimulator {

    public static void main(String[] args){
        AbstractDuck rubberDuck = new RubberDuck(new DuckNoFly());
        AbstractDuck mallardDuck = new MallardDuck(new DuckFlyWithWings());
        rubberDuck.fly();
        mallardDuck.fly();
    }
}
