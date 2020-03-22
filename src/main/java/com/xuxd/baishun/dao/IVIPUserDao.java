package com.xuxd.baishun.dao;

import com.xuxd.baishun.beans.VipUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.math.BigDecimal;

/**
 * @Auther: 许晓东
 * @Date: 20-3-13 17:15
 * @Description: {@link https://www.baeldung.com/the-persistence-layer-with-spring-data-jpa}
 */
public interface IVIPUserDao extends JpaRepository<VipUser, String> {

/*    @Transactional
    @Query(value = "insert into t_vip_users(id, name, money, tel) values (:user.id, :user.name, :user.money, :user.tel)", nativeQuery = true)
    @Modifying
    //@Query(value = "INSERT INTO Users (name, age, email, status, active) VALUES (:name, :age, :email, :status, :active)", nativeQuery = true)
    int saveVipUser(@Param("user") VipUser user);*/

    @Transactional
    @Query(value = "insert into t_vip_users(id, name, money, tel) values (:id, :name, :money, :tel)", nativeQuery = true)
    @Modifying
        //@Query(value = "INSERT INTO Users (name, age, email, status, active) VALUES (:name, :age, :email, :status, :active)", nativeQuery = true)
    int saveVipUser(@Param("id") String id, @Param("name") String name, @Param("money") BigDecimal money, @Param("tel") String tel);
}
