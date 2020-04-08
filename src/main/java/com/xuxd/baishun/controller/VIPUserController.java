package com.xuxd.baishun.controller;

import com.xuxd.baishun.beans.OutObject;
import com.xuxd.baishun.beans.VipUser;
import com.xuxd.baishun.service.IVIPUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Objects;

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

    @PostMapping("/name/tel/update")
    public OutObject updateNameOrTelById(@RequestBody VipUser user) {
        try{
            Objects.requireNonNull(user.getId());
            Objects.requireNonNull(user.getName());
            Objects.requireNonNull(user.getTel());
        }catch (NullPointerException e) {
            LOGGER.error("更新信息不能为空： " + user, e);
            return new OutObject().fail().setRtnMessage("信息不能为空");
        }
        return userService.updateNameOrTelById(user);
    }

    @GetMapping("/test")
    public OutObject test() {
//        for (int i = 0; i < 1000; i++) {
//            VipUser user = new VipUser();
//            user.setId("" + System.currentTimeMillis() + i);
//            user.setName("xu" + i);
//            user.setMoney(new BigDecimal(100+ i));
//            user.setTel("187" + i);
//            userService.saveUser(user);
//        }
        return new OutObject().success();
    }
}
