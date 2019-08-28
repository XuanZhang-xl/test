package javabasic.designmode.strategymode;

import javabasic.designmode.strategymode.behavior.DuckFlyWithWings;
import javabasic.designmode.strategymode.behavior.DuckNoFly;
import javabasic.designmode.strategymode.duck.AbstractDuck;
import javabasic.designmode.strategymode.duck.MallardDuck;
import javabasic.designmode.strategymode.duck.RubberDuck;

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
