package file;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseException;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.comments.Comment;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * created by zhangxuan9 on 2019/2/19
 */
public class JavaDoc {
    @Test
    public void getJavaDoc () throws IOException, ParseException {
        CompilationUnit cu = JavaParser.parse(new File("C:\\Users\\zhangxuan9\\IdeaProjects\\test\\src\\main\\java\\file\\StaticProperty.java"));

        List<Comment> comments = cu.getAllContainedComments();
        for (Comment comment : comments) {
            String content = comment.getContent();
            content = content.substring(5, content.length());
        }
    }

}
