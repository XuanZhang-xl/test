package xl.test.common.service;

import xl.test.common.entity.User;

import java.util.List;

/**
 * created by XUAN on 2019/12/13
 */
public interface UserService {
    void save(User user);
    List<User> listUser();



}
