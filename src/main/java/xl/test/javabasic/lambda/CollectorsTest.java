package xl.test.javabasic.lambda;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * created by XUAN on 2019/12/2
 */
public class CollectorsTest {

    /**
     * 查看 Collectors 后顺序是否一致
     *
     * 结果: 一致
     */
    @Test
    public void collectors () {
        Activity a = new Activity();
        a.setActivityUrl("apiTestPor");
        a.setStatus("23c2cr ");
        Activity b = new Activity();
        b.setActivityUrl("1101922");
        b.setStatus("e2dwefw");
        Activity c = new Activity();
        c.setActivityUrl("1101927");
        c.setStatus("fnsdfs");
        Activity d = new Activity();
        d.setActivityUrl("23fwef");
        d.setStatus("2po2mwo");
        Activity e = new Activity();
        e.setActivityUrl("23nrnfv");
        e.setStatus("9yhbfd");
        List<Activity> activities = new ArrayList<>();
        activities.add(a);
        activities.add(b);
        activities.add(c);
        activities.add(d);
        activities.add(e);


        System.out.println(activities.stream().map(Activity::getActivityUrl).collect(Collectors.joining(",")));
        System.out.println(activities.stream().map(Activity::getStatus).collect(Collectors.joining(",")));

    }


}
