package xl.test.framework.springboot.error;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * created by XUAN on 2019/12/27
 */
public class UnknownErrorSpringBootstrap {

    public static void main(String[] args){
        new SpringApplicationBuilder(UnknownErrorSpringBootstrap.class)
                .initializers(context -> {throw new UnknownError("估意抛出异常");})
                .web(WebApplicationType.NONE)
                .run(args)
                .close();
    }


}
