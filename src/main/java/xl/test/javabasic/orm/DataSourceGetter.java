package xl.test.javabasic.orm;

import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import javax.sql.DataSource;
import java.sql.Driver;

/**
 * created by XUAN on 2019/12/13
 */
public class DataSourceGetter {

    public static DataSource getDataSource() throws ClassNotFoundException {
        SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
        dataSource.setDriverClass((Class<? extends Driver>) Class.forName("com.mysql.jdbc.Driver"));
        dataSource.setUrl("jdbc:mysql://222.65.172.130:3306/test?allowMultiQueries=true&useUnicode=true&characterEncoding=UTF-8&useSSL=false");
        dataSource.setUsername("xuan");
        dataSource.setPassword("xuan");
        return dataSource;
    }

}
