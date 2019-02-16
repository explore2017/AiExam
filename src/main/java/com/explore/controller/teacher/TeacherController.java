package com.explore.controller.teacher;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/teacher")
public class TeacherController {

    @RequestMapping("/toLogin")
    public String toLogin(){
        return "teacher/login";
    }

}
