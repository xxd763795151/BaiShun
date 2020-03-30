package com.xuxd.baishun.common;

import com.xuxd.baishun.beans.OutObject;
import com.xuxd.baishun.service.IUserInfoOperationLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

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

    @Override
    public void afterSingletonsInstantiated() {
        LOGGER.info("启动定时任务");
        EXECUTOR_SERVICE.scheduleAtFixedRate(() -> {
            int intervalDays = Integer.valueOf(environment.getProperty(Key.INTERVAL_DAYS, "60"));
            OutObject outObject = logService.deleteLog(intervalDays);
            LOGGER.info("删除60天前的操作记录信息: " + outObject);
        }, 0, Integer.valueOf(environment.getProperty(Key.DETECT_PERIOD, "24")), TimeUnit.HOURS);
    }

    interface Key {
        String INTERVAL_DAYS = "operation.log.save.interval.days";
        String DETECT_PERIOD = "operation.log.del.detect.period";
    }
}
