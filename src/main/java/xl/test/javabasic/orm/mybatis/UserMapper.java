package xl.test.javabasic.orm.mybatis;

import xl.test.javabasic.orm.User;

import java.util.List;

/**
 * created by XUAN on 2019/12/13
 */
public interface UserMapper {
    void save(User user);
    List<User> listUser();
}