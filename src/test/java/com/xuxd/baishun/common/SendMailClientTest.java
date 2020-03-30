package com.xuxd.baishun.common;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;

import static org.junit.Assert.*;

/**
 * @Auther: 许晓东
 * @Date: 20-3-30 22:03
 * @Description:
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SendMailClientTest {

    @Autowired
    private Environment environment;

    @Autowired
    private SendMailClient client;

    @Test
    public void sendWithAttachment() {
        System.out.println(client.sendWithAttachment(environment.getProperty("mail.from"), environment.getProperty("mail.to"), "测试邮件发送", "不用回复", new HashMap<String, String>(){{
            put("bs.mv.db", "/home/xuxiaodong/tmp/tmpcode/baishun/h2/baishundb.mv.db");
        }}));
    }

    @Test
    public void send() {
        System.out.println(client.send(environment.getProperty("mail.from"), environment.getProperty("mail.to"), "测试邮件发送", "不用回复"));
    }
}