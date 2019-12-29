package xl.test.framework.springboot.error;

import org.springframework.boot.diagnostics.FailureAnalysis;
import org.springframework.boot.diagnostics.FailureAnalysisReporter;

/**
 * 这个在spring.factories里注册后, 就会和系统自带的report一起打印, 导致异常打印两遍, 目前不知道有什么方法能使系统的不打印
 * created by XUAN on 2019/12/27
 */
public class ConsoleFailureAnalysisReport implements FailureAnalysisReporter {

    @Override
    public void report(FailureAnalysis analysis) {
        System.out.printf("故障描述: %s \n 执行动作: %s \n 异常堆栈: %s \n",
                analysis.getDescription(),
                analysis.getAction(),
                analysis.getCause());
    }
}
