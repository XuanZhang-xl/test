package xl.test.javabasic.orm;

import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.apache.ibatis.type.TypeAliasRegistry;
import org.springframework.boot.env.PropertiesPropertySourceLoader;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import xl.test.common.entity.User;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Driver;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * created by XUAN on 2019/12/13
 */
public class OrmPropertyGetter {

    private static String userName = "";
    private static String wlanIp = "";
    private static String lanIp = "";

    static {
        userName = System.getProperty("user.name");

        // 源码来自: org.springframework.boot.context.config.ConfigFileApplicationListener
        DefaultResourceLoader resourceLoader = new DefaultResourceLoader();
        String location = "classpath:/application.properties";
        Resource resource = resourceLoader.getResource(location);
        String name = "applicationConfig: [" + location + "]";
        PropertiesPropertySourceLoader loader = new PropertiesPropertySourceLoader();
        try {
            List<PropertySource<?>> propertySources = loader.load(name, resource);
            LinkedHashMap source = (LinkedHashMap) propertySources.get(0).getSource();

            //wlanIp = source.get("wlan.ip").toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static DataSource getDataSource() throws ClassNotFoundException {
        if ("MSI-PC".equals(userName)) {
            return getDataSourceAtHome();
        } else {
            return getDataSourceOutSide();
        }
    }

    /**
     * 外网DataSource
     * @return
     * @throws ClassNotFoundException
     */
    public static DataSource getDataSourceOutSide() throws ClassNotFoundException {
        SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
        dataSource.setDriverClass((Class<? extends Driver>) Class.forName("com.mysql.jdbc.Driver"));
        dataSource.setUrl("jdbc:mysql://" + wlanIp + ":3306/test?allowMultiQueries=true&useUnicode=true&characterEncoding=UTF-8&useSSL=false");
        dataSource.setUsername("xuan");
        dataSource.setPassword("xuan");
        return dataSource;
    }

    /**
     * 内网DataSource
     * @return
     * @throws ClassNotFoundException
     */
    public static DataSource getDataSourceAtHome() throws ClassNotFoundException {
        SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
        dataSource.setDriverClass((Class<? extends Driver>) Class.forName("com.mysql.jdbc.Driver"));
        dataSource.setUrl("jdbc:mysql://" + lanIp + ":3306/test?allowMultiQueries=true&useUnicode=true&characterEncoding=UTF-8&useSSL=false");
        dataSource.setUsername("xuan");
        dataSource.setPassword("xuan");
        return dataSource;
    }

    public static Configuration getConfiguration(DataSource dataSource) throws IOException, ClassNotFoundException {
        // 相当于mybatis-config.xml
        Configuration configuration = new Configuration();
        configuration.setCacheEnabled(false);
        configuration.setUseGeneratedKeys(true);
        configuration.setDefaultExecutorType(ExecutorType.REUSE);
        //设置别名
        TypeAliasRegistry typeAliasRegistry = configuration.getTypeAliasRegistry();
        typeAliasRegistry.registerAlias("user", User.class);

        // 设置环境, 数据源, 事务管理器
        Environment environment = new Environment("dev", new JdbcTransactionFactory(), dataSource);
        configuration.setEnvironment(environment);

        // 加载xml, spring提供的加载器可以一次性加载所有xml, 并且语法有所改变, 需要加classoath*:
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        String resourceString = MapperLocation.SPRING_MYBATIS;
        Resource[] resources = resolver.getResources(resourceString);
        for (Resource resource : resources) {
            new XMLMapperBuilder(resource.getInputStream(), configuration, resourceString, configuration.getSqlFragments()).parse();
        }


        // 这是从XMLConfigBuilder.mapperElement()复制下来的代码, Resources.getResourceAsStream(resource)  这句话还不能加载出xml来
        // 因为mybatis本身不支持*.xml写这样的写法,,, 需要多个xml, 就只有配多个<mapper>标签, 这里手写加载代码的话就要一个一个mapper加载
        //ErrorContext.instance().resource(resource);
        //InputStream inputStream = Resources.getResourceAsStream(resource);
        //XMLMapperBuilder mapperParser = new XMLMapperBuilder(inputStream, configuration, resource, configuration.getSqlFragments());
        //mapperParser.parse();

        return configuration;
    }
    public static SqlSessionFactory getSqlSessionFactory(Configuration configuration) {
        return new SqlSessionFactoryBuilder().build(configuration);
    }

    public static void setWlanIp(String wlanIp) {
        OrmPropertyGetter.wlanIp = wlanIp;
    }

    public static void setLanIp(String lanIp) {
        OrmPropertyGetter.lanIp = lanIp;
    }

    public static void main(String[] args) {
        System.out.println("系统属性");
        System.getProperties().entrySet().forEach(System.out::println);
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println("环境变量");
        System.getenv().entrySet().forEach(System.out::println);
    }

}
