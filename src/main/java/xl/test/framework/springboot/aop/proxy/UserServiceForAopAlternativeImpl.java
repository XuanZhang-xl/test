package xl.test.framework.springboot.aop.proxy;

import xl.test.common.entity.User;
import xl.test.common.service.UserService;

import java.util.Arrays;
import java.util.List;

/**
 * @author XUAN
 * @since 2019/12/30
 */
public class UserServiceForAopAlternativeImpl implements UserService {
    @Override
    public void save(User user) {
        System.out.println("UserServiceForAopAlternativeImpl#saved user: " + user);
    }

    @Override
    public List<User> listUser() {
        List<User> users = Arrays.asList(User.getRandomUser(), User.getRandomUser());
        System.out.println("UserServiceForAopAlternativeImpl#" + users);
        return users;
    }
}
