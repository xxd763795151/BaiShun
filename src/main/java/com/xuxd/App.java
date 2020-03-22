package com.xuxd;

import com.xuxd.baishun.beans.VipUser;
import com.xuxd.baishun.dao.IVIPUserDao;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Hello world!
 */
@SpringBootApplication
@EnableJpaRepositories(basePackageClasses = {IVIPUserDao.class})
@EntityScan(basePackageClasses = {VipUser.class})
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
