package com.xuxd.baishun.dao;

import com.xuxd.baishun.beans.UserInfoOperationLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

/**
 * @Auther: 许晓东
 * @Date: 20-3-29 20:33
 * @Description:
 */
public interface IUserInfoOperationLogDao extends JpaRepository<UserInfoOperationLog, Long> {

    @Transactional
    @Query(value = "insert into t_user_info_updated_log(user_id, type, log) values (:userId, :type, :log)", nativeQuery = true)
    @Modifying
    void saveLog(@Param("userId") String userId, @Param("type") int type, @Param("log") String log);

    @Query(value = "select log.id as id, log.user_id as userId, log.type as type, log.update_time as updateTime, log.log as log, user.name as name, user.tel as tel from t_user_info_updated_log log join t_vip_users user on log.user_id = user.id order by updateTime desc", nativeQuery = true)
    List<UserInfoOperationLog> findAllLog();

}
