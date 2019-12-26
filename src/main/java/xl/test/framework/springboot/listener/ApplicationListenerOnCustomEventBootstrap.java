package xl.test.framework.springboot.listener;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.support.GenericApplicationContext;

/**
 * spring监听器测试泛型
 *
 * created by XUAN on 2019/12/26
 */
public class ApplicationListenerOnCustomEventBootstrap {

    public static void main(String[] args){
        GenericApplicationContext applicationContext = new GenericApplicationContext();

        applicationContext.registerBean(MyApplicationListener.class);
        applicationContext.refresh();
        applicationContext.publishEvent(new MyApplicationEvent(applicationContext));
        applicationContext.publishEvent(new MyApplicationEvent("Hello"));
        applicationContext.close();
        applicationContext.publishEvent(new MyApplicationEvent("Hello Again"));
    }

    /**
     * 仅监听MyApplicationEvent事件
     */
    public static class MyApplicationListener implements ApplicationListener<MyApplicationEvent> {

        @Override
        public void onApplicationEvent(MyApplicationEvent event) {
            System.out.println(event.getClass().getSimpleName());
        }
    }

    public static class MyApplicationEvent extends ApplicationEvent {
        public MyApplicationEvent(Object source) {
            super(source);
        }
    }
}
