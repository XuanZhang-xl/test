package xl.test.framework.springboot.metadata;

import org.springframework.util.ObjectUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.annotation.Target;
import java.lang.reflect.AnnotatedElement;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * created by XUAN on 2019/12/20
 */
@TransactionService(name = "test")
public class SpringBootAnnotationReflectionBootstrap {

    public static void main(String[] args) {
        // Class 实现了 AnnotatedElement
        AnnotatedElement annotatedElement = SpringBootAnnotationReflectionBootstrap.class;
        TransactionService annotation = annotatedElement.getAnnotation(TransactionService.class);
        System.out.println("@TransactionService.name() = " + annotation.name());


        // 打印TransactionService.class中所有无参方法
        ReflectionUtils.doWithMethods(TransactionService.class,
                method -> {
                    ReflectionUtils.makeAccessible(method);
                    System.out.printf("@TransactionService.%s() = %s\n", method.getName(), ReflectionUtils.invokeMethod(method, annotation));
                },
                method -> method.getParameterCount() == 0);

        System.out.println();
        System.out.println();

        // 将Annotation.class中的方法排除
        printAnnotationAttribute(annotation);

        System.out.println();
        System.out.println();

        // 递归查找所有注解/元注解的方法
        Set<Annotation> allMetaAnnotation = getAllMetaAnnotation(annotation);
        allMetaAnnotation.forEach(SpringBootAnnotationReflectionBootstrap::printAnnotationAttribute);


    }

    private static void printAnnotationAttribute(Annotation annotation) {
        Class<? extends Annotation> annotationType = annotation.annotationType();
        ReflectionUtils.doWithMethods(TransactionService.class,
                method -> {
                    ReflectionUtils.makeAccessible(method);
                    System.out.printf("@%s.%s() = %s\n", annotationType.getSimpleName(), method.getName(), ReflectionUtils.invokeMethod(method, annotation));
                },
                method -> method.getParameterCount() == 0 && method.getDeclaringClass().equals(Annotation.class));
    }

    private static Set<Annotation> getAllMetaAnnotation(Annotation annotation) {
        Annotation[] annotations = annotation.annotationType().getAnnotations();
        if (ObjectUtils.isEmpty(annotations)) {
            return Collections.emptySet();
        }
        Set<Annotation> metaAnnotationSet = Stream.of(annotations)
                .filter(metaAnnotation -> !Target.class.getPackage().equals(metaAnnotation.annotationType().getPackage()))
                .collect(Collectors.toSet());

        // 递归查找注解元素的元注解
        Set<Annotation> metaAnnotationCollect = metaAnnotationSet.stream()
                .map(SpringBootAnnotationReflectionBootstrap::getAllMetaAnnotation)
                .collect(HashSet::new, Set::addAll, Set::addAll);
        // 添加结果
        metaAnnotationSet.addAll(metaAnnotationCollect);
        return metaAnnotationSet;
    }



}
