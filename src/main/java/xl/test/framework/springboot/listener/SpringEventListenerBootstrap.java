package xl.test.framework.springboot.listener;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * 查看spring/springboot事件发布顺序
 *
 * created by XUAN on 2019/12/26
 */
public class SpringEventListenerBootstrap {

    public static void main(String[] args){
        new SpringApplicationBuilder(SpringEventListenerBootstrap.class)
                .listeners(event -> System.out.println("SpringApplication事件监听器 : " + event.getClass().getSimpleName()))
                .web(WebApplicationType.NONE)
                .run(args)
                .close();
    }


}
