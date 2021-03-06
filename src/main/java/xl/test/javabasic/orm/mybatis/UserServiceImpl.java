package xl.test.javabasic.orm.mybatis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xl.test.User;
import xl.test.javabasic.orm.UserService;

import java.util.List;

/**
 * created by XUAN on 2019/12/13
 */
@Service("userServiceImpl")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(User user) {
        userMapper.save(user);
    }

    @Override
    public List<User> listUser() {
        return userMapper.listUser();
    }

    public void setUserMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }
}
