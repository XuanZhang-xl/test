package xl.test.framework.springboot.exit;

import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

/**
 * created by XUAN on 2019/12/27
 */
@EnableAutoConfiguration
public class ExitCodeGeneratorBootstrap {

    @Bean
    public ExitCodeGenerator exitCodeGenerator () {
        System.out.println("ExitCodeGenerator bean 创建");
        return () -> {
            System.out.println("执行退出码88");
            return 88;
        };
    }

    public static void main(String[] args){
        ConfigurableApplicationContext context = new SpringApplicationBuilder(ExitCodeGeneratorBootstrap.class)
                .web(WebApplicationType.NONE)
                .run(args);
        int exitCode = SpringApplication.exit(context);
        System.exit(exitCode);
    }

}
