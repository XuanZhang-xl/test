package xl.test.framework.springboot.metadata;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;

import java.io.IOException;
import java.util.Set;

/**
 * 获得类的注解
 * 
 * created by XUAN on 2019/12/20
 * @see org.springframework.core.type.classreading.MetadataReader
 */
@SpringBootApplication
public class SpringBootAnnotationMetadataBootstrap {

    public static void main(String[] args) throws IOException {
        String className = SpringBootAnnotationMetadataBootstrap.class.getName();
        // 构建工厂实例
        MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory();

        // 获得类的元信息读取器
        MetadataReader metadataReader = metadataReaderFactory.getMetadataReader(className);

        // 读取当前类的注解信息
        AnnotationMetadata annotationMetadata = metadataReader.getAnnotationMetadata();
        for (String annotationType : annotationMetadata.getAnnotationTypes()) {
            Set<String> metaAnnotationTypes = annotationMetadata.getMetaAnnotationTypes(annotationType);
            for (String metaAnnotationType : metaAnnotationTypes) {
                System.out.printf("注解 @%s 元标注 @%s \n", annotationType, metaAnnotationType);
            }

        }
    }


}
