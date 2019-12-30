package xl.test.javabasic.orm.mybatis;

import com.alibaba.fastjson.JSON;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import xl.test.common.entity.User;
import xl.test.common.service.UserService;

import java.io.IOException;
import java.util.List;

/**
 * 公网ip一变, 就可能报错. application.properties与#{}貌似不兼容
 * created by XUAN on 2019/12/13
 */
public class MybatisUsingSpringTest {

    public static void main(String[] args) throws ClassNotFoundException, IOException {

        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("orm/mybatis/applicationContext.xml");
        UserMapper userMapper = (UserMapper) applicationContext.getBean("userMapper");
        List<User> users = userMapper.listUser();
        System.out.println(JSON.toJSONString(users));

        // 事务测试
        UserService userService = (UserService) applicationContext.getBean("userService");
        userService.save(User.getRandomUser());
        users = userMapper.listUser();
        System.out.println(JSON.toJSONString(users));
    }
}
