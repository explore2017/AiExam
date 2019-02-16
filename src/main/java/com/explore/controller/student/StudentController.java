package com.explore.controller.student;

import com.explore.common.Const;
import com.explore.common.ServerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/student")
public class StudentController {


    /**
     * 登录页面跳转
     * @return
     */
    @RequestMapping("toLogin")
    public String toLogin(){
        return "login";
    }


}