package com.im.muxin.service;

/**
 * @author Administrator
 */
public interface UserService {

    /**
     * 判断用户名是否存在
     * @param username
     * @return
     */
    public boolean queryUsernameIsExist(String username);
}
