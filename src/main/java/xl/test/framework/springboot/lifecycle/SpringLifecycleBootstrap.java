package xl.test.framework.springboot.lifecycle;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * created by XUAN on 2019/12/30
 */
public class SpringLifecycleBootstrap {

    public static void main(String[] args){
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.register(MyLifecycle.class);
        applicationContext.register(MySmartLifecycle.class);
        applicationContext.refresh();
        applicationContext.close();
    }

}
