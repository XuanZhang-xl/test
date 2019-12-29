package xl.test.framework.springboot.metadata;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于注解 / 派生性 测试
 * created by XUAN on 2019/12/20
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Transactional
@Service
public @interface TransactionService {

    String name() default "";

    /**
     * 覆盖 @Transactional.transactionManager()的默认值
     * @return
     */
    String transactionManager() default "txManager";
}
