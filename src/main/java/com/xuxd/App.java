package com.xuxd;

import com.xuxd.baishun.beans.VipUser;
import com.xuxd.baishun.common.ScheduledTask;
import com.xuxd.baishun.dao.IVIPUserDao;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

/**
 * Hello world!
 */
@SpringBootApplication
@EnableJpaRepositories(basePackageClasses = {IVIPUserDao.class})
@EntityScan(basePackageClasses = {VipUser.class})
@EnableAspectJAutoProxy
public class App {
    private static final Logger LOGGER = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        // h2数据文件在初始化后就被锁定了，所以先在这里复制一个临时备份文件
        backupH2();
        SpringApplication.run(App.class, args);
    }

    private static void backupH2() {
        Properties properties = new Properties();
        try {
            properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("application.properties"));
        } catch (IOException e) {
            LOGGER.error("加载application.properties异常", e);
            return;
        }
        //
        String bakPath = properties.getProperty(ScheduledTask.Key.BACKUP_PATH);
        bakPath = bakPath.replace("${user.dir}", System.getProperty("user.dir")).replace("${" + ScheduledTask.Key.BACKUP_NAME + "}", properties.getProperty(ScheduledTask.Key.BACKUP_NAME));
        String bakPathTmp = bakPath + ".bak";
        File src = new File(bakPath), dst = new File(bakPathTmp);
        if (!src.exists()) {
            LOGGER.error("文件不存在，无法备份，路径：" + bakPath);
            return;
        }
        if (dst.exists()) {
            try {
                FileUtils.forceDelete(dst);
            } catch (IOException e) {
                LOGGER.error("删除临时备份文件异常: " + dst, e);
            }
        }
        try {
            FileUtils.copyFile(src, dst);
            LOGGER.info("备份h2数据到本地");
        } catch (IOException e) {
            LOGGER.error("复制备份文件异常，无法备份", e);
            return;
        }

    }
}
