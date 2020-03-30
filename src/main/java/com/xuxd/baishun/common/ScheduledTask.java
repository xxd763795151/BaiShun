package com.xuxd.baishun.common;

import com.xuxd.baishun.beans.OutObject;
import com.xuxd.baishun.service.IUserInfoOperationLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @Auther: 许晓东
 * @Date: 20-3-30 14:29
 * @Description:
 */
@Component
public class ScheduledTask implements SmartInitializingSingleton {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduledTask.class);

    private static final ScheduledExecutorService EXECUTOR_SERVICE = Executors.newScheduledThreadPool(1);

    @Autowired
    private Environment environment;
    @Autowired
    private IUserInfoOperationLogService logService;

    @Autowired
    private SendMailClient mailClient;

    @Override
    public void afterSingletonsInstantiated() {
        LOGGER.info("启动定时任务");
        LOGGER.info("开启清除过期操作记录任务");
        EXECUTOR_SERVICE.scheduleAtFixedRate(() -> {
            int intervalDays = Integer.valueOf(environment.getProperty(Key.INTERVAL_DAYS, "60"));
            OutObject outObject = logService.deleteLog(intervalDays);
            LOGGER.info("删除60天前的操作记录信息: " + outObject);
        }, 0, Integer.valueOf(environment.getProperty(Key.DETECT_PERIOD, "24")), TimeUnit.HOURS);

        if (Boolean.valueOf(environment.getProperty(Key.BACKUP_ENABLE, "true"))) {
            LOGGER.info("开启数据定时备份任务");
            EXECUTOR_SERVICE.scheduleAtFixedRate(() -> {
                String subject = "数据备份，7天有效，请及时下载";
                String content = "附件为备份的数据，只会保存7天，及时下载";
                Map<String, String> name2paths = new HashMap<>();
                name2paths.put(environment.getProperty(Key.BACKUP_NAME), environment.getProperty(Key.BACKUP_PATH));
                mailClient.sendWithAttachment(environment.getProperty(Key.MAIL_FROM), environment.getProperty(Key.MAIL_TO), subject, content, name2paths);
                LOGGER.info("发送备份数据");
            }, 0, Integer.valueOf(environment.getProperty(Key.BACKUP_INTERVAL, "12")), TimeUnit.HOURS);

        }
    }

    interface Key {
        String INTERVAL_DAYS = "operation.log.save.interval.days";
        String DETECT_PERIOD = "operation.log.del.detect.period";
        String BACKUP_INTERVAL = "backup.interval";
        String BACKUP_NAME = "backup.name";
        String BACKUP_PATH = "backup.path";
        String MAIL_FROM = "mail.from";
        String MAIL_TO = "mail.to";
        String BACKUP_ENABLE = "backup.enable";
    }
}
