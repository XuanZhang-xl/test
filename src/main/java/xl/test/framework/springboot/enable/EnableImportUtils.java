package xl.test.framework.springboot.enable;

import org.springframework.core.type.AnnotationMetadata;

import java.util.Map;

/**
 * created by XUAN on 2019/12/23
 */
public class EnableImportUtils {

    public static String[] selectImports(AnnotationMetadata importingClassMetadata) {
        // 读取 @EnableServer 的所有属性方法
        Map<String, Object> annotationAttributes = importingClassMetadata.getAnnotationAttributes(EnableServer.class.getName());
        // 获取type属性
        Server.ServerType type = (Server.ServerType) annotationAttributes.get("type");

        // 导入的类名称数组
        String[] importClassNames = new String[0];
        if (Server.ServerType.HTTP.equals(type)) {
            importClassNames = new String[]{HttpServer.class.getName()};
        } else if (Server.ServerType.FTP.equals(type)) {
            importClassNames = new String[]{FtpServer.class.getName()};
        }
        return importClassNames;
    }

}
