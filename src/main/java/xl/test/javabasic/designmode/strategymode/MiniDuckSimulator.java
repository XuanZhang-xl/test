package xl.test.javabasic.designmode.strategymode;

import xl.test.javabasic.designmode.strategymode.behavior.DuckFlyWithWings;
import xl.test.javabasic.designmode.strategymode.behavior.DuckNoFly;
import xl.test.javabasic.designmode.strategymode.duck.AbstractDuck;
import xl.test.javabasic.designmode.strategymode.duck.MallardDuck;
import xl.test.javabasic.designmode.strategymode.duck.RubberDuck;

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
