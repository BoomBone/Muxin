package com.im.muxin.controller;


import com.im.muxin.pojo.Users;
import com.im.muxin.service.UserService;
import com.im.muxin.util.IMoocJSONResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("u")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/registerOrLogin")
    public IMoocJSONResult registerOrLogin(@RequestBody Users user) {
        //0.判断用户名和密码不能为空
        if (StringUtils.isBlank(user.getNickname()) ||
                StringUtils.isBlank(user.getPassword())) {
            return IMoocJSONResult.errorMsg("用户名或密码不能为空。。。");
        }

        // 1.判断用户名是否存在，如果存在就登陆，如果不存在则注册
        boolean usernameIsExist = userService.queryUsernameIsExist(user.getUsername());
        if (usernameIsExist) {
            //1.1登录
        } else {
            //1.2注册
        }

        return IMoocJSONResult.ok();
    }
}
