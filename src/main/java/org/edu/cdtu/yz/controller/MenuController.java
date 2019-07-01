package org.edu.cdtu.yz.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.edu.cdtu.yz.bean.User;
import org.edu.cdtu.yz.service.IMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/menu")
public class MenuController {
    @Autowired
    IMenuService menuService;

    @RequestMapping("/list")
    public List<Map<String, Object>> getMenu() {
        Subject subject = SecurityUtils.getSubject();
        User user = (User) subject.getPrincipal();
        System.out.println(user.getUsername());
        List<Map<String, Object>> parentList = menuService.getMenu(user.getUsername());
        return parentList;
    }
}
