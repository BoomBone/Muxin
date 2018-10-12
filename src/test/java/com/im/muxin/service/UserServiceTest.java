package com.im.muxin.service;

import com.im.muxin.mapper.UsersMapper;
import com.im.muxin.pojo.Users;
import com.im.muxin.pojo.vo.UsersVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UsersMapper usersMapper;

    @Test
    public void queryUsernameIsExist() {
        Users users = new Users();
        users.setUsername("hahahaha");
        Users result = usersMapper.selectOne(users);
    }

    @Test
    public void queryUserForLogin() {
    }

    @Test
    public void saveUser() {
        Users users = new Users();
        users.setNickname("asdfadfs");
        users.setUsername("pppppp");
        users.setPassword("123456");
        users.setId("asdfasdfasdf");
        users.setFaceImageBig("");
        users.setFaceImage("");
        users.setCid("");
        users.setQrcode("");
        int insert = usersMapper.insert(users);
    }
}