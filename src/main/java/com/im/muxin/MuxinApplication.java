package com.im.muxin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan(basePackages = "com.im.muxin.mapper")
@ComponentScan(basePackages = {"com.im", "org.n3r.idworker"})
public class MuxinApplication {

    public static void main(String[] args) {
        SpringApplication.run(MuxinApplication.class, args);
    }
}
