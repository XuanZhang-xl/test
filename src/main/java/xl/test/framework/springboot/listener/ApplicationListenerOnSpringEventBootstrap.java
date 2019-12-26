package xl.test.framework.springboot.listener;

import org.springframework.context.support.GenericApplicationContext;

/**
 * spring监听器测试非泛型
 *
 * created by XUAN on 2019/12/26
 */
public class ApplicationListenerOnSpringEventBootstrap {

    public static void main(String[] args){
        GenericApplicationContext applicationContext = new GenericApplicationContext();
        System.out.println("创建Spring应用上下文: " + applicationContext.getDisplayName());

        // 添加ApplicationListener非泛型实现
        applicationContext.addApplicationListener(event -> System.out.println("触发事件: " + event.getClass().getSimpleName()));
        System.out.println("应用上下文准备初始化");
        applicationContext.refresh();
        System.out.println("应用上下文已初始化");

        System.out.println("应用上下文准备停止");
        applicationContext.stop();
        System.out.println("应用上下文已停止");

        System.out.println("应用上下文准备启动");
        applicationContext.start();
        System.out.println("应用上下文已启动");

        System.out.println("应用上下文准备关闭");
        applicationContext.close();
        System.out.println("应用上下文已关闭");

    }
}
