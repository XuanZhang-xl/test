package xl.test.framework.springboot.listener;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.event.ApplicationContextEvent;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

/**
 * 注解驱动监听器, 可以接受/发布任意对象
 *
 * created by XUAN on 2019/12/26
 */
public class GenericEventListenerBootstrap {

    public static void main(String[] args){
        // 创建注解驱动应用上下文
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        //context.register(MyApplicationListener.class);
        applicationContext.register(MyGenericEventListener.class);
        applicationContext.refresh();
        applicationContext.publishEvent("Hello world!");
        applicationContext.publishEvent(11111);
        applicationContext.close();
    }

    public static class MyGenericEventListener {

        //@EventListener(String.class)
        @EventListener
        public void onStringEvent(String s) {
            System.out.println("onStringEvent: " + s);
        }

        @EventListener
        public void onIntegerEvent(Integer i) {
            System.out.println("onIntegerEvent: " + i);
        }
    }

}
