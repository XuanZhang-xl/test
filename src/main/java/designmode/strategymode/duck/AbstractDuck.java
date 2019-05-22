package designmode.strategymode.duck;

import designmode.strategymode.behavior.FlyBehavior;

/**
 * 鸭子抽象类
 * created by XUAN on 2019/04/14
 */
public abstract class AbstractDuck implements Duck {


    /**
     * 委托飞行的动作
     */
    public FlyBehavior flyBehavior;

    /**
     * 飞行
     */
    public abstract void fly();


    @Override
    public void swim() {
        System.out.println("swimming");
    }

    @Override
    public void display() {
        System.out.println("cute");
    }

    @Override
    public void quack() {
        System.out.println("嘎嘎嘎");
    }
}
