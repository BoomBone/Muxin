package com.im.muxin.service;

import com.im.muxin.pojo.Users;

/**
 * @author Administrator
 */
public interface UserService {

    /**
     * 判断用户名是否存在
     *
     * @param username
     * @return
     */
    boolean queryUsernameIsExist(String username);

    /**
     * 查询用户是否存在
     * @param username
     * @param password
     * @return
     */
    Users queryUserForLogin(String username, String password);

    /**
     * 用户注册
     * @param users
     * @return
     */
    Users saveUser(Users users);

    /**
     *修改用户记录
     */
    Users updateUserInfo(Users user);

    Users queryUserById(String userId);
}
