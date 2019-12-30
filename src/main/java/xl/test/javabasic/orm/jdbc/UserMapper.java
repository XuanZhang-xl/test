package xl.test.javabasic.orm.jdbc;


import org.springframework.jdbc.core.RowMapper;
import xl.test.common.entity.User;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * created by XUAN on 2019/12/13
 */
public class UserMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet resultSet, int i) throws SQLException {
        User user = new User();
        user.setId(resultSet.getInt("id"));
        user.setName(resultSet.getString("name"));
        user.setAge(resultSet.getInt("age"));
        user.setSex(resultSet.getString("sex"));
        return user;
    }
}
