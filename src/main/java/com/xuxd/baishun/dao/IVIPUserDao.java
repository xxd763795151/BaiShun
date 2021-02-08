package com.xuxd.baishun.dao;

import com.xuxd.baishun.beans.VipUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * @Auther: 许晓东
 * @Date: 20-3-13 17:15
 * @Description: {@link https://www.baeldung.com/the-persistence-layer-with-spring-data-jpa}
 */
public interface IVIPUserDao extends JpaRepository<VipUser, String> {

    @Transactional
    @Query(value = "insert into t_vip_users(id, name, money, tel, type, remarks) values (:id, :name, :money, :tel, :type, :remarks)", nativeQuery = true)
    @Modifying
    int saveVipUser(@Param("id") String id,
                    @Param("name") String name,
                    @Param("money") BigDecimal money,
                    @Param("tel") String tel,
                    @Param("type") int type,
                    @Param("remarks") String remarks);

    @Query(value = "select id, name, money, tel, update_time, type, remarks from t_vip_users order by update_time desc", nativeQuery = true)
    List<VipUser> findAllVipUsers();

    @Transactional
    @Modifying
    @Query(value = "update t_vip_users set money = :money, update_time=current_timestamp() where id = :id", nativeQuery = true)
    int updateMoneyById(@Param("id") String id, @Param("money") BigDecimal money);

    Optional<VipUser> findById(String id);

    @Transactional
    @Modifying
    @Query(value = "update t_vip_users set name = ?2 , tel = ?3, type = ?4, remarks = ?5, update_time = current_timestamp() where id = ?1", nativeQuery = true)
    int updateNameOrTelById(@Param("id") String id, @Param("name") String name, @Param("tel") String tel, @Param("type") int type, @Param("remarks") String remarks);
}
