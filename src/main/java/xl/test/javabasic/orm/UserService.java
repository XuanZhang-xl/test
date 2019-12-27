package xl.test.javabasic.orm;

import xl.test.User;

import java.util.List;

/**
 * created by XUAN on 2019/12/13
 */
public interface UserService {
    void save(User user);
    List<User> listUser();



}
