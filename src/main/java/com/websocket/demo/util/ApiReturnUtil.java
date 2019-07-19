package com.websocket.demo.util;

import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: Gruuy
 * @remark:
 * @date: Create in 14:23 2019/7/19
 */
public class ApiReturnUtil {

    public static HashMap<String,Object> error(String message){
        HashMap<String,Object> map=new HashMap<>();
        map.put("error_msg",message);
        return map;
    }

    public static HashMap<String,Object> success(String cid){
        HashMap<String,Object> map=new HashMap<>();
        map.put("success",cid);
        return map;
    }
}
