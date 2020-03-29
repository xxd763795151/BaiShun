package com.xuxd.baishun.service;

import com.xuxd.baishun.beans.OutObject;
import com.xuxd.baishun.beans.VipUser;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @Auther: 许晓东
 * @Date: 20-3-13 16:30
 * @Description:
 */
public interface IVIPUserService {

    OutObject saveUser(VipUser user);

    List<VipUser> findAll();

    OutObject updateMoney(String id, BigDecimal diffMoney, Boolean isRecharge);

    OutObject updateNameOrTelById(String id, String name, String tel);
}
