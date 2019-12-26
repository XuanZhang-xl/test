package xl.test.framework.springboot.listener;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.event.ApplicationContextEvent;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 *
 * 异步注解驱动监听
 * created by XUAN on 2019/12/26
 */
public class AnnotatedAsyncEventListenerBootstrap {

    public static void main(String[] args){
        // 创建注解驱动应用上下文
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.register(MyAsyncEventListener.class);
        applicationContext.refresh();
        applicationContext.close();
    }

    // 激活异步, 否则 @Async无效
    @EnableAsync
    public static class MyAsyncEventListener {

        @EventListener({ContextRefreshedEvent.class, ContextClosedEvent.class})
        @Async
        public void onApplicationContextEvent(ApplicationContextEvent event) {
            System.out.println("[线程 " + Thread.currentThread().getName() + " ] : MyAsyncEventListener : " + event.getClass().getSimpleName());
        }
    }

}
