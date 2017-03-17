package com.fable.outer.start;

import org.springframework.context.support.ClassPathXmlApplicationContext;



/**
 * 外交换服务启动类
 */
public class StartUpOuterService {

    public static void main(String[] args) throws Exception {
        // 加载RMI服务
        new ClassPathXmlApplicationContext("classpath*:applicationContext-rmi-server.xml");
    }
}
