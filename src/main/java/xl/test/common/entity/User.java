package xl.test.common.entity;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Value;

import java.util.Random;

/**
 * created by XUAN on 2019/12/13
 */
public class User {

    private int id;

    @Value("${owner.name}")
    private String name;
    private int age;
    private String sex;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    private static String[] SEX = new String[]{"男", "女"};
    private static String[] ALPHABET = new String[]{"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "j", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "1", "4", "2"};

    public static User getRandomUser() {
        Random random = new Random();
        User user = new User();
        user.setSex(SEX[random.nextInt(SEX.length)]);
        user.setAge(random.nextInt(99) + 1);
        StringBuilder name = new StringBuilder();
        for (int i = 0; i < random.nextInt(7) + 1; i++) {
            name.append(ALPHABET[random.nextInt(ALPHABET.length)]);
        }
        user.setName(name.toString());
        return user;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
