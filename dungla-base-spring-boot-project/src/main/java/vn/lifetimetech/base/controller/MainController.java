package vn.lifetimetech.base.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import vn.lifetimetech.base.service.UserService;

@Controller
public class MainController extends BaseController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/login")
    public String login() {
        return "/login";
    }

    @RequestMapping(value = "/seed")
    public String seed() {
        userService.seedAdminAccount();
        return "/login";
    }
}
