package com.explore.controller.manage;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/manage")
public class ManageController {

    @RequestMapping("/toLogin")
    public String toLogin(){
        return "manage/login";
    }

}
