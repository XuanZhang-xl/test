package xl.test.framework.springboot.error;

import org.springframework.boot.diagnostics.FailureAnalysis;
import org.springframework.boot.diagnostics.FailureAnalyzer;

/**
 * created by XUAN on 2019/12/27
 */
public class UnknownErrorFailureAnalyzer implements FailureAnalyzer {

    @Override
    public FailureAnalysis analyze(Throwable failure) {
        if (failure instanceof UnknownError) {
            return new FailureAnalysis("未知错误", "请重试", failure);
        }
        return null;
    }
}
