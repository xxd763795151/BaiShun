package com.xuxd.baishun.service;

import com.xuxd.baishun.beans.VipUser;

import java.util.List;

/**
 * @Auther: 许晓东
 * @Date: 20-3-13 16:30
 * @Description:
 */
public interface IVIPUserService {

    boolean saveUser(VipUser user);

    List<VipUser> findAll();
}
