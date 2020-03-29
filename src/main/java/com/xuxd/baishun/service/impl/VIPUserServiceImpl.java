package com.xuxd.baishun.service.impl;

import com.xuxd.baishun.beans.OutObject;
import com.xuxd.baishun.beans.VipUser;
import com.xuxd.baishun.dao.IVIPUserDao;
import com.xuxd.baishun.service.IVIPUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.Optional;

/**
 * @Auther: 许晓东
 * @Date: 20-3-13 16:31
 * @Description:
 */
@Service
public class VIPUserServiceImpl implements IVIPUserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(VIPUserServiceImpl.class);

    @Autowired
    private IVIPUserDao vipUserDao;

    @Override
    public OutObject saveUser(VipUser user) {
        OutObject outObject = new OutObject();
        Optional<VipUser> optional = vipUserDao.findById(user.getId());
        boolean hasExist = optional.isPresent();
        int save = 0;
        if (!hasExist) {
            try {
                save = vipUserDao.saveVipUser(user.getId(), user.getName(), user.getMoney(), user.getTel());
            } catch (Exception ex) {
                LOGGER.error("save vip user exception: " + ex);
                hasExist = ex instanceof SQLIntegrityConstraintViolationException;
            }
        }
        if (hasExist) {
            return outObject.fail().setRtnMessage("该卡号已存在");
        }
        return save > 0 ? outObject.success() : outObject.fail().setRtnMessage("保存失败： 新增人数为0");
    }

    @Override
    public List<VipUser> findAll() {
        return vipUserDao.findAllVipUsers();
    }

    @Override
    public OutObject updateMoney(String id, BigDecimal diffMoney, boolean isRecharge) {
        OutObject outObject = new OutObject();
        Optional<VipUser> optional = vipUserDao.findById(id);
        if (!optional.isPresent()) {
            return outObject.fail().setRtnMessage("更新失败：查无此人");
        }
        VipUser user = optional.get();
        BigDecimal afterUpdateMoney = null;
        if (isRecharge) {
            afterUpdateMoney = user.getMoney().add(diffMoney);
        } else {
            if (diffMoney.compareTo(user.getMoney()) > 0) {
                return outObject.fail().setRtnMessage("更新失败：余额不足");
            }
            afterUpdateMoney = user.getMoney().subtract(diffMoney);
        }
        vipUserDao.updateMoneyById(id, afterUpdateMoney);
        return outObject.success();
    }

    @Override
    public OutObject updateNameOrTelById(String id, String name, String tel) {
        OutObject outObject = new OutObject();
        return vipUserDao.updateNameOrTelById(id, name, tel) > 0 ? outObject.success() : outObject.fail().setRtnMessage("更新失败");
    }
}