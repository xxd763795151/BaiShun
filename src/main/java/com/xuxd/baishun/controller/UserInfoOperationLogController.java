package com.xuxd.baishun.controller;

import com.xuxd.baishun.service.IUserInfoOperationLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Auther: 许晓东
 * @Date: 20-3-29 23:53
 * @Description:
 */
@RestController
@RequestMapping("/user/info/log")
public class UserInfoOperationLogController extends BaseController {

    @Autowired
    private IUserInfoOperationLogService logService;

    @GetMapping("/list")
    public String findAllLog() {
        return success(logService.finaAllLog(), "success");
    }
}
