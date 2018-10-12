package com.im.muxin.service.impl;

import com.im.muxin.mapper.UsersMapper;
import com.im.muxin.pojo.Users;
import com.im.muxin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Administrator
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UsersMapper usersMapper;

    /**
     * 判断用户名是否存在
     *
     * @param username
     * @return
     */
    @Override
    public boolean queryUsernameIsExist(String username) {
        Users users = new Users();
        users.setUsername(username);
        Users result = usersMapper.selectOne(users);
        return result != null;
    }
}
