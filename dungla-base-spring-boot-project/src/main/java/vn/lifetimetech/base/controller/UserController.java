package vn.lifetimetech.base.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import vn.lifetimetech.base.model.UsersEntity;
import vn.lifetimetech.base.service.UserService;
import vn.lifetimetech.base.util.Constant;

@Controller
@RequestMapping(value = "users")
public class UserController extends BaseController {

    @Autowired
    UserService userService;

    @RequestMapping(value = "{userId}", method = RequestMethod.GET)
    public String detail(@PathVariable("userId") int userId, Model model) {
        UsersEntity usersEntity = userService.getUserById(userId);
        model.addAttribute(Constant.USER_ENTITY, usersEntity);
        return "user/detail";
    }
}
