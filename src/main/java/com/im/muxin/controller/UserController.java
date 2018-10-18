package com.im.muxin.controller;


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

@RestController
@RequestMapping("u")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private FastDFSClient fastDFSClient;

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

        return IMoocJSONResult.ok(usersVO);
    }

    /**
     * 上传头像
     *
     * @return
     */
    @PostMapping("/uploadFaceBase64")
    public IMoocJSONResult uploadFaceBase64(@RequestBody UserBO userBo) throws Exception {
        //获取前端传来的base64字符串，然后转换为文件对象再上传

        String base64Data = userBo.getFaceData();
        String userFacePath = "C:\\" + userBo.getUserId() + "userface64.png";
        boolean toFile = FileUtils.base64ToFile(userFacePath, base64Data);

        System.out.println("base64转换file="+toFile);
//        System.out.println("base64="+base64Data);

        //上传文件到fastdfs
        MultipartFile multipartFile = FileUtils.fileToMultipart(userFacePath);
        String url = fastDFSClient.uploadBase64(multipartFile);
        System.out.println(url);

        //获取缩略图的url
        String thump = "_80x80.";
        String arr[] = url.split("\\.");
        String thumpImgUrl = arr[0] + thump + arr[1];

        //更新用户头像
        Users user = new Users();
        user.setId(userBo.getUserId());
        user.setFaceImage(thumpImgUrl);
        user.setFaceImageBig(url);
        userService.updateUserInfo(user);

        return IMoocJSONResult.ok(user);
    }
}
