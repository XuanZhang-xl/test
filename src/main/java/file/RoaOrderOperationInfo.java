package file;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseException;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.comments.Comment;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * created by zhangxuan9 on 2019/2/19
 */
public class RoaOrderOperationInfo {

    private static String PREFIX = "REQ_TYPE_";

    private int total = 0;

    private String table = "order_operation_info";

    private static Map<String, String> MAP = new HashMap<>();

    static {
        for (OperationType value : OperationType.values()) {
            MAP.put(value.name(), value.desc);
        }
    }

    @Test
    public void findLogPlace() throws IOException {
        System.out.println("表名\t类名\t行数\t类型\t业务说明");
        File file = new File("C:\\Users\\zhangxuan9\\IdeaProjects\\roa\\branch\\roa-api\\src\\main\\java\\com\\tuniu\\adapter");
        findLogPlace0(file);
        System.out.println("共有" + total + "条");
    }

    public void findLogPlace0 (File file) throws IOException {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File subFile : files) {
                findLogPlace0(subFile);
            }
        } else {
            BufferedReader  reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            String line = null;
            String packageName = null;
            int lineNum = 0;

            String fileName = file.getName().substring(0, file.getName().length() - 5);

            int correctLineNum = 0;
            boolean needNext1 = false;
            boolean needNext2 = false;

            while ((line = reader.readLine()) != null) {
                lineNum++;

                Matcher matcher5 = LOG_PATTERN5.matcher(line);
                if (matcher5.matches()) {
                    packageName = matcher5.group(1);
                    continue;
                }

                if (needNext1) {
                    needNext1 = false;
                    Matcher matcher1 = LOG_PATTERN4.matcher(line);
                    if (matcher1.find()) {
                        System.out.println(table + "\t" + packageName + "." + fileName + "\t" + lineNum + "\t" + MAP.get(matcher1.group(1)));
                    } else {
                        System.out.println(table + "\t" + packageName + "." + fileName + "\t" + lineNum);
                    }
                    continue;
                }

                if (needNext2) {
                    needNext2 = false;
                    if (line.contains("0")) {
                        total++;
                        System.out.println(table + "\t" + packageName + "." + fileName + "\t" + correctLineNum);
                    }
                    continue;
                }

                boolean matches1 = LOG_PATTERN1.matcher(line).find();
                boolean matches2 = LOG_PATTERN2.matcher(line).find();
                boolean matches3 = LOG_PATTERN3.matcher(line).find();

                // 肯定是保存,直接打印
                if (matches1 || matches2) {
                    total++;
                    Matcher matcher1 = LOG_PATTERN4.matcher(line);
                    if (matcher1.find()) {
                        System.out.println(table + "\t" + packageName + "." + fileName + "\t" + lineNum + "\t" + MAP.get(matcher1.group(1)));
                    } else {
                        // 不知道谁老喜欢换行.........  找不到, 找去下一行找. 否则放弃
                        correctLineNum = lineNum;
                        needNext1 = true;
                    }
                }

                if (matches3) {
                    // 判断是否是更新
                    if (line.contains("0")) {
                        total++;
                        System.out.println(table + "\t" + packageName + "." + fileName + "\t" + lineNum);
                    } else if (line.contains("1") || line.contains("2")) {
                        // 跳过
                    } else {
                        // 不知道谁老喜欢换行.........  找不到, 找去下一行找. 否则放弃
                        correctLineNum = lineNum;
                        needNext2 = true;
                    }
                }
            }
        }
    }

    public static void main(String[] args){
        String str1 = "OrderOperationInfo operationInfo = super.saveOrderOperationInfo(orderRelation.getRetailId(), StaticProperty.HANGTIAN,";
        System.out.println(LOG_PATTERN1.matcher(str1).find());
    }

    private static final Pattern LOG_PATTERN1  = Pattern.compile("\\.saveOrderOperationInfo\\(");
    private static final Pattern LOG_PATTERN2  = Pattern.compile("^[ \t]+saveOrderOperationInfo\\(");
    private static final Pattern LOG_PATTERN3  = Pattern.compile("\\.saveOrUpdata\\(operationInfo");

    private static final Pattern LOG_PATTERN4  = Pattern.compile("OperationType.(TYPE_[1-8])");
    /**
     * 行内匹配   包名
     */
    private static final Pattern LOG_PATTERN5  = Pattern.compile("package ([a-zA-Z.]+);");

    public enum OperationType {
        TYPE_1(1, "占位"),
        TYPE_2(2, "出票"),
        TYPE_3(3, "退票"),
        TYPE_4(4, "取消订单"),
        TYPE_5(5, "12306账号验证"),
        TYPE_6(6, "身份验证"),
        TYPE_7(7, "改签占位"),
        TYPE_8(8, "改签出票");

        OperationType(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        private int code;
        private String desc;

        public int getCode() {
            return code;
        }
        public String getDesc() {
            return desc;
        }
    }

}
