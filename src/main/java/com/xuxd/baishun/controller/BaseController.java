package com.xuxd.baishun.controller;

import com.alibaba.fastjson.JSONObject;

/**
 * @Auther: 许晓东
 * @Date: 20-3-6 10:07
 * @Description:
 */
public class BaseController extends JSONObject {

    private static final String SUCCESS_CODE = "0";
    private static final String FAIL_CODE = "-9999";
    private static final String RETURN_CODE = "rtnCode";
    private static final String RETURN_MESSAGE = "rtnMessage";

    protected String success() {
        return success("success");
    }

    protected String success(String returnMessage) {
        return success(null, returnMessage);
    }

    protected String success(Object data, String returnMessage) {
        JSONObject ret = new JSONObject();
        ret.put("data", data);
        ret.put(RETURN_CODE, SUCCESS_CODE);
        ret.put(RETURN_MESSAGE, returnMessage);
        return ret.toJSONString();
    }

    protected String fail(String returnMessage) {
        JSONObject ret = new JSONObject();
        ret.put(RETURN_CODE, FAIL_CODE);
        ret.put(RETURN_MESSAGE, returnMessage);
        return ret.toJSONString();
    }

}
