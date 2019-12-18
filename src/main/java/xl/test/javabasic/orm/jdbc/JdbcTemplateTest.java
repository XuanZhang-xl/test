package xl.test.javabasic.orm.jdbc;

import com.alibaba.fastjson.JSONObject;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import xl.test.javabasic.orm.DataSourceGetter;
import xl.test.javabasic.orm.User;

import javax.sql.DataSource;
import java.sql.Driver;
import java.util.List;
import java.util.Random;

/**
 * spring 的 JdbcTemplate 操作数据库
 *
 *
 * created by XUAN on 2019/12/13
 */
public class JdbcTemplateTest {


    public static void main(String[] args) throws ClassNotFoundException {
        DataSource dataSource = DataSourceGetter.getDataSource();

        UserServiceImpl userService = new UserServiceImpl();
        userService.setDataSource(dataSource);
        User user = User.getRandomUser();
        userService.save(user);
        List<User> users = userService.listUser();
        System.out.println(JSONObject.toJSONString(users));
    }


}
