package com.xuxd.baishun.controller;

import com.xuxd.baishun.beans.VipUser;
import com.xuxd.baishun.service.IVIPUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Auther: 许晓东
 * @Date: 20-3-5 22:45
 * @Description:
 */
@RestController
@RequestMapping("/users/vip")
public class VIPUserController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(VIPUserController.class);
    @Autowired
    private IVIPUserService userService;


    @PostMapping("/save")
    public String saveUser(@RequestBody VipUser user) {
        LOGGER.info("vip用户信息保存成功， user: " + user);
        boolean success = userService.saveUser(user);
        if (!success) {
            LOGGER.error("新增vip用户失败，vip= {}", user);
            return fail("新增vip用户失败");
        }
        return success();
    }

    @GetMapping("/all")
    public String findAll() {
        return success(userService.findAll(), "success");
    }
}
