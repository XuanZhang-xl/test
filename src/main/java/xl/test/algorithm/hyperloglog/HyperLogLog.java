package xl.test.algorithm.hyperloglog;

/**
 * 去重后估计总个数, 有微小误差
 * https://www.jianshu.com/p/55defda6dcd2
 * created by XUAN on 2019/8/23
 */
public class HyperLogLog {

    public static void main(String[] args){
        // 进行10次试验, 每次试验抛硬币次数为10w, 20w, 30w .... 90w
        for (int i = 100000; i < 1000000; i += 100000) {
            Experiment experiment = new Experiment(i);
            experiment.work();
            double estimate = experiment.estimate();
            System.out.printf("%d %.2f %.2f\n", i, estimate, (estimate - i) / i);
        }
    }
}
