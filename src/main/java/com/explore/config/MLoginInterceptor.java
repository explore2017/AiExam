package com.explore.config;

import com.explore.common.Const;
import com.explore.common.ServerResponse;
import com.explore.pojo.Manager;
import com.explore.pojo.Teacher;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;

@Component
public class MLoginInterceptor implements HandlerInterceptor {


    //这个方法是在访问接口之前执行的，我们只需要在这里写验证登陆状态的业务逻辑，就可以在用户调用指定接口之前验证登陆状态了
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler ) throws Exception {
        if(request.getMethod().equals("OPTIONS")){
            return  true;     //      不拦截OPTIONS类型的请求
        }
        //每一个项目对于登陆的实现逻辑都有所区别，我这里使用最简单的Session提取User来验证登陆。
        HttpSession session = request.getSession();
        //这里的User是登陆时放入session的
        Object obj = session.getAttribute(Const.CURRENT_USER);
        //如果session中没有user，表示没登陆
        if (obj instanceof Manager){
            return true;    //如果session里有user，表示该用户已经登陆，放行，用户即可继续调用自己需要的接口
        }else {
            response.sendError(403,"need_login");
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json; charset=utf-8");
            response.setStatus(403);
            return  false;
            //这个方法返回false表示忽略当前请求，如果一个用户调用了需要登陆才能使用的接口，如果他没有登陆这里会直接忽略掉
            //当然你可以利用response给用户返回一些提示信息，告诉他没登陆
        }
    }

}
