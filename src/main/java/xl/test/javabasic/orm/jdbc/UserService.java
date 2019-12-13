package xl.test.javabasic.orm.jdbc;

import xl.test.javabasic.orm.User;

import java.util.List;

/**
 * created by XUAN on 2019/12/13
 */
public interface UserService {
    void save(User user);
    List<User> listUser();



}
