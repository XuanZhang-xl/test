package xl.test.framework.springmvc;

import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletException;

/**
 * created by XUAN on 2020/1/15
 */
public class DispatcherServletBootstrap {
    
    public static void main(String[] args) throws ServletException {
        DispatcherServlet dispatcherServlet = new DispatcherServlet();
        dispatcherServlet.init();
    }
    
}
