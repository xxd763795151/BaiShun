package com.xuxd.baishun.service.impl;

import com.xuxd.baishun.beans.VipUser;
import com.xuxd.baishun.dao.IVIPUserDao;
import com.xuxd.baishun.service.IVIPUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Auther: 许晓东
 * @Date: 20-3-13 16:31
 * @Description:
 */
@Service
public class VIPUserServiceImpl implements IVIPUserService {
    @Autowired
    private IVIPUserDao vipUserDao;

    @Override
    public boolean saveUser(VipUser user) {
        return vipUserDao.saveVipUser(user.getId(), user.getName(), user.getMoney(), user.getTel()) > 0;
    }

    @Override
    public List<VipUser> findAll() {
        return vipUserDao.findAllVipUsers();
    }
}
