package com.xuxd.baishun.service.impl;

import com.xuxd.baishun.beans.OperationType;
import com.xuxd.baishun.beans.OutObject;
import com.xuxd.baishun.beans.VipUser;
import com.xuxd.baishun.common.OperationLog;
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
    @OperationLog(id = "#{id}", type = OperationType.create,
            content = "新建用户，id: #{id}, 姓名: #{name}，电话: #{tel}，充值: #{money}，类型: #{type}")
    public OutObject saveUser(VipUser user) {
        OutObject outObject = new OutObject();
        Optional<VipUser> optional = vipUserDao.findById(user.getId());
        boolean hasExist = optional.isPresent();
        int save = 0;
        if (!hasExist) {
            try {
                save = vipUserDao.saveVipUser(user.getId(), user.getName(), user.getMoney(), user.getTel(), user.getType(), user.getRemarks());
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
    @OperationLog(id = "#{1}", type = OperationType.recharge,
            content = "特殊处理，此处不处理")
    public OutObject updateMoney(String id, BigDecimal diffMoney, Boolean isRecharge) {
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
    @OperationLog(id = "#{id}", type = OperationType.update, content = "id为#{id}的用户信息发生变更，变更前姓名: #{name}，电话: #{tel}, 会员类型： #{type}, 备注: #{remarks}")
    public OutObject updateNameOrTelById(VipUser user) {
        OutObject outObject = new OutObject();
        Optional<VipUser> optional = vipUserDao.findById(user.getId());
        if (!optional.isPresent()) {
            return outObject.setRtnMessage("更新失败，查询不到该账号信息");
        }
        outObject = vipUserDao.updateNameOrTelById(user.getId(), user.getName(), user.getTel(), user.getType(), user.getRemarks()) > 0 ? outObject.success() : outObject.fail().setRtnMessage("更新失败");
        // 为了记录日志 ，额外做的一步操作，保存更新前的信息
        VipUser oldUser = optional.get();
        user.setName(oldUser.getName());
        user.setTel(oldUser.getTel());
        user.setType(oldUser.getType());
        user.setRemarks(oldUser.getRemarks() != null ? oldUser.getRemarks() : " ");
        return outObject;
    }
}
