package javabasic.designmode.commandmode;

/**
 * 遥控器
 * created by XUAN on 2019/04/22
 */
public class SimpleRemoteControl {

    Command slot;

    /**
     * 只要调用这个方法, 换不一样的Command实现不同的执行方法
     * ..........................有什么软用?
     */
    public void buttonWasPressed () {
        slot.execute();
    }

    public void setSlot(Command slot) {
        this.slot = slot;
    }

    public SimpleRemoteControl() {
    }
}
