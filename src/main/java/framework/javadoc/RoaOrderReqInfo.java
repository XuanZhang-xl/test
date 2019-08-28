package framework.javadoc;

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
public class RoaOrderReqInfo {

    private static String PREFIX = "REQ_TYPE_";


    private static Map<String, String> MAP = new HashMap<>();

    static {
        try {
            CompilationUnit cu = JavaParser.parse(new File("C:\\Users\\zhangxuan9\\IdeaProjects\\test\\src\\main\\java\\framework.javadoc\\StaticProperty.java"));
            List<Comment> comments = cu.getAllContainedComments();
            for (Comment comment : comments) {
                String content = comment.getContent();
                content = content.substring(5);
                String[] split = content.split(" ");
                MAP.put(PREFIX + split[0], split[1].trim());
            }
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    int total = 0;

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
            boolean needNext = false;

            while ((line = reader.readLine()) != null) {
                lineNum++;

                Matcher matcher5 = LOG_PATTERN5.matcher(line);
                if (matcher5.matches()) {
                    packageName = matcher5.group(1);
                    continue;
                }

                if (needNext) {
                    needNext = false;
                    Matcher matcher4 = LOG_PATTERN4.matcher(line);
                    if (matcher4.find()) {
                        System.out.println("order_req_info\t" + packageName + "." + fileName + "\t" + correctLineNum + "\t" + MAP.get(matcher4.group()));
                    } else {
                        System.out.println("order_req_info\t" + packageName + "." + fileName + "\t" + correctLineNum);
                    }
                    continue;
                }

                boolean matches1 = LOG_PATTERN1.matcher(line).find();
                boolean matches2 = LOG_PATTERN2.matcher(line).find();
                boolean matches3 = LOG_PATTERN3.matcher(line).find();

                if (matches1 || matches2 || matches3) {
                    total++;
                    Matcher matcher4 = LOG_PATTERN4.matcher(line);
                    if (matcher4.find()) {
                        System.out.println("order_req_info\t" + packageName + "." + fileName + "\t" + lineNum + "\t" + MAP.get(matcher4.group()));
                    } else {
                        // 不知道谁老喜欢换行.........  找不到, 找去下一行找. 否则放弃
                        correctLineNum = lineNum;
                        needNext = true;
                    }
                }
            }
        }
    }

    @Test
    public void testPattern() {
        String str1 = ".saveOrderReqInfo()";
        String str2 = "\tsaveOrderReqInfo(JsonUtil.toString(commonAsynRsp), StaticProperty.REQ_TYPE_53, orderReqInfo);";
        String str3 = "\tprotected OrderReqInfo saveOrderReqInfo(";
        System.out.println(LOG_PATTERN1.matcher(str1).find());
        System.out.println(LOG_PATTERN2.matcher(str2).find());
        System.out.println(LOG_PATTERN3.matcher(str3).find());

        System.out.println(LOG_PATTERN1.matcher(str2).find());
        System.out.println(LOG_PATTERN2.matcher(str3).find());
        System.out.println(LOG_PATTERN3.matcher(str1).find());

        System.out.println(LOG_PATTERN1.matcher(str3).find());
        System.out.println(LOG_PATTERN2.matcher(str1).find());
        System.out.println(LOG_PATTERN3.matcher(str2).find());

        String str5 = "package framework.javadoc.as.dw;";
        Matcher matcher5 = LOG_PATTERN5.matcher(str5);
        if (matcher5.matches()) {
            System.out.println(matcher5.group(1));
        }
    }

    /**
     * 行内匹配  .saveOrderReqInfo()方法在其他类中的调用或this调用
     */
    private static final Pattern LOG_PATTERN1  = Pattern.compile("\\.saveOrderReqInfo\\(");

    /**
     * 行内匹配   saveOrderReqInfo()方法的直接调用
     */
    private static final Pattern LOG_PATTERN2  = Pattern.compile("^[ \t]+saveOrderReqInfo\\(");

    /**
     * 行内匹配   saveOrderReqInfo()方法
     */
    private static final Pattern LOG_PATTERN3  = Pattern.compile("\\S+[ \t]+saveOrderReqInfo\\(");

    /**
     * 行内匹配   同一行写了类型
     */
    private static final Pattern LOG_PATTERN4  = Pattern.compile("REQ_TYPE_\\d+");

    /**
     * 行内匹配   包名
     */
    private static final Pattern LOG_PATTERN5  = Pattern.compile("package ([a-zA-Z.]+);");

}
