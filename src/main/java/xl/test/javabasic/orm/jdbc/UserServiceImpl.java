package xl.test.javabasic.orm.jdbc;

import org.springframework.jdbc.core.JdbcTemplate;
import xl.test.javabasic.orm.User;
import xl.test.javabasic.orm.UserService;

import javax.sql.DataSource;
import java.sql.Types;
import java.util.List;

/**
 * created by XUAN on 2019/12/13
 */
public class UserServiceImpl implements UserService {

    private JdbcTemplate jdbcTemplate;

    public void setDataSource(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public void save(User user) {
        jdbcTemplate.update(
                "insert into user(name,age,sex) values (?,?,?)",
                new Object[]{user.getName(), user.getAge(), user.getSex()},
                new int[]{Types.VARCHAR,Types.INTEGER,Types.VARCHAR});
    }

    @Override
    public List<User> listUser() {
        return jdbcTemplate.query("select * from user", new UserMapper());
    }
}
