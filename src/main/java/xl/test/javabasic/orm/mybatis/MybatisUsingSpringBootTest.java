package xl.test.javabasic.orm.mybatis;

import com.alibaba.fastjson.JSON;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import xl.test.javabasic.orm.OrmPropertyGetter;
import xl.test.common.entity.User;
import xl.test.common.service.UserService;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.List;

/**
 * @SpringBootApplication 本身带有@Configuration 直接在这个类中配置即可
 *
 * created by XUAN on 2019/12/13
 */
@MapperScan(basePackages = {"xl.test.javabasic.orm.mybatis"})
@SpringBootApplication(scanBasePackages={"xl.test.javabasic.orm.mybatis"})
public class MybatisUsingSpringBootTest implements EnvironmentAware {

    public static void main(String[] args) throws ClassNotFoundException, IOException {

        ConfigurableApplicationContext applicationContext = new SpringApplicationBuilder(MybatisUsingSpringBootTest.class).web(WebApplicationType.NONE).run();

        UserService userService = (UserService) applicationContext.getBean("userServiceImpl");
        List<User> users = userService.listUser();
        System.out.println(JSON.toJSONString(users));
        applicationContext.close();
    }

    /**
     * 条件装配
     * user.name=MSI-PC时装配
     * @return
     * @throws ClassNotFoundException
     */
    @Bean
    @ConditionalOnProperty(prefix = "user", name = "name", havingValue = "MSI-PC")
    public DataSource dataSourceAtHome() throws ClassNotFoundException {
        return OrmPropertyGetter.getDataSourceAtHome();
    }

    /**
     * 条件装配
     * 当不存在DataSource实例时装配
     * @return
     * @throws ClassNotFoundException
     */
    @Bean
    @ConditionalOnMissingBean(DataSource.class)
    public DataSource dataSource() throws ClassNotFoundException {
        return OrmPropertyGetter.getDataSourceOutSide();
    }

    @Bean
    public Configuration mybatisConfiguration(DataSource dataSource) throws IOException, ClassNotFoundException {
        return OrmPropertyGetter.getConfiguration(dataSource);
    }

    @Bean
    public SqlSessionFactory qqlSessionFactory(Configuration configuration) {
        return OrmPropertyGetter.getSqlSessionFactory(configuration);
    }

    @Override
    public void setEnvironment(Environment environment) {
        // 把公网ip set进去
        OrmPropertyGetter.setWlanIp(environment.getProperty("wlan.ip"));
        // 把内网ip set进去
        OrmPropertyGetter.setLanIp(environment.getProperty("lan.ip"));
    }
}
