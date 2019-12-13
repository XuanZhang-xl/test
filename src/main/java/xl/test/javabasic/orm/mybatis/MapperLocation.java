package xl.test.javabasic.orm.mybatis;

/**
 * created by XUAN on 2019/12/13
 */
public interface MapperLocation {

    // 原生mybatis mapper位置写法, 不支持*, 没classpath
    String MYBATIS = "orm/mybatis/mapper/UserMapper.xml";
    String MYBATIS_CONFIG = "orm/mybatis/mybatis-config.xml";

    // spring增强 mapper位置写法, 支持*, 要classpath
    String SPRING_MYBATIS = "classpath*:orm/mybatis/mapper/*.xml";
}
