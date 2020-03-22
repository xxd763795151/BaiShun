package com.xuxd.baishun.beans;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Auther: 许晓东
 * @Date: 20-3-6 10:07
 * @Description:
 */
@Entity
//@Table(name = "t_vip_users")
public class VipUser implements Serializable {

    private static final long serialVersionUID = -2663252230706657072L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    String id;
    String name;
    BigDecimal money;
    String tel;
    String updateTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "VipUser{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", money=" + money +
                ", tel='" + tel + '\'' +
                ", updateTime='" + updateTime + '\'' +
                '}';
    }
}
