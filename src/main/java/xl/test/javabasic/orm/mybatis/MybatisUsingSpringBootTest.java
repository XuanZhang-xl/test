package xl.test.javabasic.orm.mybatis;

import com.alibaba.fastjson.JSON;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import xl.test.javabasic.orm.MapperLocation;
import xl.test.javabasic.orm.OrmPropertyGetter;
import xl.test.javabasic.orm.User;
import xl.test.javabasic.orm.UserService;

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
public class MybatisUsingSpringBootTest {

    public static void main(String[] args) throws ClassNotFoundException, IOException {

        ConfigurableApplicationContext applicationContext = new SpringApplicationBuilder(MybatisUsingSpringBootTest.class).web(WebApplicationType.NONE).run();

        UserService userService = (UserService) applicationContext.getBean("userServiceImpl");
        List<User> users = userService.listUser();
        System.out.println(JSON.toJSONString(users));
        applicationContext.close();
    }


    @Bean
    public DataSource getDataSource() throws ClassNotFoundException {
        return OrmPropertyGetter.getDataSource();
    }

    @Bean
    public Configuration getConfiguration(DataSource dataSource) throws IOException, ClassNotFoundException {
        return OrmPropertyGetter.getConfiguration(dataSource);
    }

    @Bean
    public SqlSessionFactory getSqlSessionFactory(Configuration configuration) {
        return OrmPropertyGetter.getSqlSessionFactory(configuration);
    }
}
