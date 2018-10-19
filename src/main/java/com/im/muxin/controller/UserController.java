package com.im.muxin.controller;


import com.google.gson.Gson;
import com.im.muxin.pojo.Users;
import com.im.muxin.pojo.bo.UserBO;
import com.im.muxin.pojo.vo.UsersVO;
import com.im.muxin.service.UserService;
import com.im.muxin.util.FastDFSClient;
import com.im.muxin.util.FileUtils;
import com.im.muxin.util.IMoocJSONResult;
import com.im.muxin.util.MD5Utils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("u")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private FastDFSClient fastDFSClient;

    Gson gson = new Gson();

    @PostMapping("/registerOrLogin")
    public IMoocJSONResult registerOrLogin(@RequestBody Users user) throws Exception {
        //0.判断用户名和密码不能为空
        if (StringUtils.isBlank(user.getUsername()) ||
                StringUtils.isBlank(user.getPassword())) {
            return IMoocJSONResult.errorMsg("用户名或密码不能为空。。。");
        }

        // 1.判断用户名是否存在，如果存在就登陆，如果不存在则注册
        boolean usernameIsExist = userService.queryUsernameIsExist(user.getUsername());
        Users userResult = null;
        if (usernameIsExist) {
            //1.1登录
            userResult = userService.queryUserForLogin(user.getUsername(),
                    MD5Utils.getMD5Str(user.getPassword()));
            if (userResult == null) {
                return IMoocJSONResult.errorMsg("用户名或密码不正确。。。");
            }
        } else {
            //1.2注册
            user.setNickname((user.getUsername()));
            user.setFaceImage("");
            user.setFaceImageBig("");
            user.setPassword(MD5Utils.getMD5Str(user.getPassword()));
            userResult = userService.saveUser(user);
        }

        UsersVO usersVO = new UsersVO();
        BeanUtils.copyProperties(userResult, usersVO);
        String usersVoJson = gson.toJson(usersVO);
        return IMoocJSONResult.ok(usersVoJson);
    }

    /**
     * @Description: 上传用户头像
     */
    @PostMapping("/uploadFaceBase64")
    public IMoocJSONResult uploadFaceBase64(HttpServletRequest request) throws Exception {

        MultipartFile file = ((MultipartHttpServletRequest) request).getFile("file");
        String url = fastDFSClient.uploadBase64(file);
        System.out.println(url);

//		"dhawuidhwaiuh3u89u98432.png"
//		"dhawuidhwaiuh3u89u98432_80x80.png"

        // 获取缩略图的url
        String thump = "_80x80.";
        String arr[] = url.split("\\.");
        String thumpImgUrl = arr[0] + thump + arr[1];

        // 更细用户头像
        Users user = new Users();
        user.setId(getUserId(request));
        user.setFaceImage(thumpImgUrl);
        user.setFaceImageBig(url);

        Users result = userService.updateUserInfo(user);
        String userJson = gson.toJson(result);
        return IMoocJSONResult.ok(userJson);
    }

    private String getUserId(HttpServletRequest request) throws Exception {
        String userId = request.getParameter("userId");
        System.out.println("userId=" + userId);
        return userId;
    }

    /**
     * @Description: 获取用户头像地址
     */
    @PostMapping("/getFaceImage")
    public IMoocJSONResult getUserPicUrl(String userId) throws Exception {
        Users users = userService.queryUserById(userId);
        return IMoocJSONResult.ok(users.getFaceImageBig());
    }
}
