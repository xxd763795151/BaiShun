package com.xuxd.baishun.controller;

import com.xuxd.baishun.beans.OutObject;
import com.xuxd.baishun.beans.VipUser;
import com.xuxd.baishun.service.IVIPUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

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
    public OutObject saveUser(@RequestBody VipUser user) {
        LOGGER.info("vip用户信息保存成功， user: " + user);
        return userService.saveUser(user);
    }

    @GetMapping("/all")
    public String findAll() {
        return success(userService.findAll(), "success");
    }

    @PostMapping("/money/update")
    public OutObject updateMoney(@RequestParam String id, @RequestParam BigDecimal money, @RequestParam boolean isRecharge) {

        return userService.updateMoney(id, money, isRecharge);
    }
}
