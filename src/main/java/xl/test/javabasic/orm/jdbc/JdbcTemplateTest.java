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

    private static String[] SEX = new String[]{"男", "女"};
    private static String[] ALPHABET = new String[]{"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "j", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "1", "4", "2"};

    public static void main(String[] args) throws ClassNotFoundException {
        DataSource dataSource = DataSourceGetter.getDataSource();

        UserServiceImpl userService = new UserServiceImpl();
        userService.setDataSource(dataSource);
        User user = getRandomUser();
        userService.save(user);
        List<User> users = userService.listUser();
        System.out.println(JSONObject.toJSONString(users));
    }

    private static User getRandomUser() {
        Random random = new Random();
        User user = new User();
        user.setSex(SEX[random.nextInt(1)]);
        user.setAge(random.nextInt(99) + 1);
        StringBuilder name = new StringBuilder();
        for (int i = 0; i < random.nextInt(7) + 1; i++) {
            name.append(ALPHABET[random.nextInt(ALPHABET.length)]);
        }
        user.setName(name.toString());
        return user;
    }
}
