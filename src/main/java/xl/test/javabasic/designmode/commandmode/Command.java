package xl.test.javabasic.designmode.commandmode;

/**
 * created by XUAN on 2019/04/22
 */
public interface Command {

    /**
     * 命令接口
     */
    public void execute();

    /**
     * 撤销
     */
    public void undo();

}
