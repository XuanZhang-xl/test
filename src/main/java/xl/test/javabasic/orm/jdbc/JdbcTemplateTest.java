package xl.test.javabasic.orm.jdbc;

import com.alibaba.fastjson.JSONObject;
import xl.test.javabasic.orm.OrmPropertyGetter;
import xl.test.common.entity.User;

import javax.sql.DataSource;
import java.util.List;

/**
 * spring 的 JdbcTemplate 操作数据库
 *
 *
 * created by XUAN on 2019/12/13
 */
public class JdbcTemplateTest {


    public static void main(String[] args) throws ClassNotFoundException {
        DataSource dataSource = OrmPropertyGetter.getDataSource();

        UserServiceImpl userService = new UserServiceImpl();
        userService.setDataSource(dataSource);
        User user = User.getRandomUser();
        userService.save(user);
        List<User> users = userService.listUser();
        System.out.println(JSONObject.toJSONString(users));
    }


}
