package xl.test.javabasic.orm.mybatis;

import com.alibaba.fastjson.JSON;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import xl.test.javabasic.orm.User;

import java.io.IOException;
import java.io.Reader;
import java.util.List;

/**
 * created by XUAN on 2019/12/13
 */
public class MybatisUsingXmlTest {

    public static void main(String[] args) throws ClassNotFoundException, IOException {

        String resource = MapperLocation.MYBATIS_CONFIG;

        // 获得配置文件的位置
        String file = Thread.currentThread().getContextClassLoader().getResource(resource).getFile();
        System.out.println(file);
        // 获得配置文件的字符流
        Reader resourceAsReader = Resources.getResourceAsReader(resource);

        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(resourceAsReader);

        SqlSession sqlSession = sqlSessionFactory.openSession();
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        List<User> users = userMapper.listUser();

        System.out.println(JSON.toJSONString(users));

    }
}
