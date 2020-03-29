package com.xuxd.baishun.service;

import com.xuxd.baishun.beans.OutObject;
import com.xuxd.baishun.beans.UserInfoOperationLog;

import java.util.List;

/**
 * @Auther: 许晓东
 * @Date: 20-3-29 20:56
 * @Description:
 */
public interface IUserInfoOperationLogService {
    OutObject saveLog(UserInfoOperationLog log);

    List<UserInfoOperationLog> finaAllLog();
}
