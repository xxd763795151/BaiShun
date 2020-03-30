package com.xuxd.baishun.common;

import com.xuxd.baishun.beans.OutObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.internet.MimeMessage;
import java.util.Map;

/**
 * @Auther: 许晓东
 * @Date: 20-3-30 21:47
 * @Description:
 */
@Component
public class SendMailClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(SendMailClient.class);

    @Autowired
    private JavaMailSender mailSender;

    public OutObject sendWithAttachment(String from, String to, String subject, String content, Map<String, String> name2paths) {
        OutObject outObject = new OutObject();
        MimeMessage mimeMessage = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content);
            for (Map.Entry<String, String> entry : name2paths.entrySet()) {
                helper.addAttachment(entry.getKey(), new FileSystemResource(entry.getValue()));
            }
            mailSender.send(mimeMessage);
            LOGGER.info(String.format("邮件发送成功： from=%s, to=%s, subject=%s, content=%s", from, to, subject, content));
            outObject.success().setRtnMessage("邮件发送成功");
        } catch (Exception e) {
            LOGGER.error(String.format("邮件发送异常： from=%s, to=%s, subject=%s, content=%s", from, to, subject, content), e);
            outObject.fail().setRtnMessage("邮件发送异常");
        }
        return outObject;
    }

    public OutObject send(String from, String to, String subject, String content) {
        OutObject outObject = new OutObject();
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(content);
        try {
            mailSender.send(message);
            LOGGER.info(String.format("邮件发送成功： from=%s, to=%s, subject=%s, content=%s", from, to, subject, content));
            outObject.success().setRtnMessage("邮件发送成功");
        } catch (Exception e) {
            LOGGER.error(String.format("邮件发送异常： from=%s, to=%s, subject=%s, content=%s", from, to, subject, content), e);
            outObject.fail().setRtnMessage("邮件发送异常");
        }
        return outObject;
    }

}
