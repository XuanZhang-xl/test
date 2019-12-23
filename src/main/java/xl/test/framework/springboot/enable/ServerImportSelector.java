package xl.test.framework.springboot.enable;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Map;

/**
 * created by XUAN on 2019/12/23
 */
public class ServerImportSelector implements ImportSelector {

    /**
     * 会把返回值注册为bean
     *
     * 如果是Configuration, 也会注册Configuration里的bean
     *
     * @param importingClassMetadata
     * @return
     */
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        return EnableImportUtils.selectImports(importingClassMetadata);
    }
}
