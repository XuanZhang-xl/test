package xl.test.framework.springboot.listener;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.event.ApplicationContextEvent;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

/**
 *
 * 注解驱动监听
 * created by XUAN on 2019/12/26
 */
public class AnnotatedEventListenerBootstrap {

    public static void main(String[] args){
        // 创建注解驱动应用上下文
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        //context.register(MyApplicationListener.class);
        applicationContext.register(MyMultiEventListener.class);
        applicationContext.refresh();
        applicationContext.publishEvent(new ApplicationListenerOnCustomEventBootstrap.MyApplicationEvent(applicationContext));
        applicationContext.close();
    }

    public static abstract class AbstractApplicationListener {
        @EventListener(ContextRefreshedEvent.class)
        public void onContextRefreshedEvent(ContextRefreshedEvent event) {
            System.out.println("AbstractApplicationListener: " + event.getClass().getSimpleName());
        }
    }

    public static class MyApplicationListener extends AbstractApplicationListener {

        @EventListener(ContextClosedEvent.class)
        public boolean onContextClosedEvent(ContextClosedEvent event) {
            System.out.println("MyApplicationListener: " + event.getClass().getSimpleName());
            return true;
        }
    }

    public static class MyMultiEventListener {

        @EventListener({ContextRefreshedEvent.class, ContextClosedEvent.class})
        public void onEvent() {
            // 同时监听多个, 无参
            System.out.println("onEvent");
        }

        /**
         * 传入的参数类型必须是监听对象自己或父类, 否则报错
         * @param event
         */
        @EventListener({ContextRefreshedEvent.class, ContextClosedEvent.class})
        public void onApplicationContextEvent(ApplicationContextEvent event) {
            // 父类参数
            System.out.println("onApplicationContextEvent: " + event.getClass().getSimpleName());
        }

        /**
         * 参数最好写 ApplicationEvent
         * @param event
         */
        @EventListener({ContextRefreshedEvent.class, ContextClosedEvent.class})
        public void onApplicationEvent(ApplicationEvent event) {
            // 总接口
            System.out.println("onApplicationEvent: " + event.getClass().getSimpleName());
        }

        // 不支持多个参数, 就不试了
        //@EventListener({ContextRefreshedEvent.class, ContextClosedEvent.class})
        //public void onEvents(ContextRefreshedEvent refreshedEvent, ContextClosedEvent contextClosedEvent) {
        //    //多个参数
        //    System.out.println("onEvents: " + refreshedEvent.getClass().getSimpleName() + ", " +  contextClosedEvent.getClass().getSimpleName());
        //}
    }

}
