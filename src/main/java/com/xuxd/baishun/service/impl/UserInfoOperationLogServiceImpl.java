package com.xuxd.baishun.service.impl;

import com.xuxd.baishun.beans.OutObject;
import com.xuxd.baishun.beans.UserInfoOperationLog;
import com.xuxd.baishun.dao.IUserInfoOperationLogDao;
import com.xuxd.baishun.service.IUserInfoOperationLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Auther: 许晓东
 * @Date: 20-3-29 21:04
 * @Description:
 */
@Service
public class UserInfoOperationLogServiceImpl implements IUserInfoOperationLogService {

    @Autowired
    private IUserInfoOperationLogDao userInfoOperationLogDao;

    @Override
    public OutObject saveLog(UserInfoOperationLog log) {
        userInfoOperationLogDao.saveLog(log.getUserId(), log.getType().ordinal(), log.getLog());
        return new OutObject().success();
    }

    @Override
    public List<UserInfoOperationLog> finaAllLog() {
        return userInfoOperationLogDao.findAllLog();
    }

    @Override
    public OutObject deleteLog(int intervalDays) {
        int count = userInfoOperationLogDao.deleteLog(intervalDays);
        return new OutObject().success().setRtnMessage(String.format("删除完成，本次删除%d条", count));
    }
}
