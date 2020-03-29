package com.xuxd.baishun.beans;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @Auther: 许晓东
 * @Date: 20-3-29 20:33
 * @Description:
 */
@Entity
@Table(name = "t_user_info_updated_log")
public class UserInfoOperationLog implements Serializable {

    private static final long serialVersionUID = -160217880445948477L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String userId;
    private String name;
    private String tel;
    //type ENUM('create', 'recharge', 'deduction', 'update')
    // 0, 1,2,3
    // -> OperationType
    @Enumerated(EnumType.STRING)
    private OperationType type;
    private String updateTime;
    private String log;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public OperationType getType() {
        return type;
    }

    public void setType(OperationType type) {
        this.type = type;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }


    @Override
    public String toString() {
        return "UserInfoOperationLog{" +
                "id=" + id +
                ", userId='" + userId + '\'' +
                ", name='" + name + '\'' +
                ", tel='" + tel + '\'' +
                ", type=" + type +
                ", updateTime='" + updateTime + '\'' +
                ", log='" + log + '\'' +
                '}';
    }
}
