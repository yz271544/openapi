package com.teradata.portal.admin.main.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by Evan on 2016/6/27.
 */
@Controller
//@RequestMapping(value = "/admin")
public class AdminMainController {

    /**
     * 控制台主控制器
     * @param request
     * @param httpSession
     * @return
     */
    @RequestMapping(value = "/main.htm")
    public ModelAndView adminMainIndex(HttpServletRequest request, HttpSession httpSession) {
        return new ModelAndView("admin/admin");
    }

    /**
     * OPEN API账号注册选择页面
     * @param request
     * @param httpSession
     * @return
     */
    @RequestMapping(value = "/selectRegist.htm")
    public ModelAndView selectRegistIndex(HttpServletRequest request, HttpSession httpSession){
        return new ModelAndView("admin/selectRegist");
    }

    /**
     * API使用者注册页面
     * @param request
     * @param httpSession
     * @return
     */
    @RequestMapping(value = "/apiUserRegistIndex.htm")
    public ModelAndView apiUserRegistIndex(HttpServletRequest request, HttpSession httpSession){
        return new ModelAndView("admin/apiUser");
    }

    /**
     * API开发者注册页面
     * @param request
     * @param httpSession
     * @return
     */
    @RequestMapping(value = "/apiDeveloperRegistIndex.htm")
    public ModelAndView apiDeveloperRegistIndex(HttpServletRequest request, HttpSession httpSession){
        return new ModelAndView("admin/apiDeveloper");
    }
}
