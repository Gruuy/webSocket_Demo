package com.websocket.demo.webserver;

import com.websocket.demo.util.ApiReturnUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.HashMap;

/**
 * @author: Gruuy
 * @remark: 用于外部调用群发信息
 * @date: Create in 14:18 2019/7/19
 */
@Controller
@RequestMapping("/checkcenter")
public class CheckCenterController {

    /** 推送数据接口 */
    @ResponseBody
    @RequestMapping("/socket/push/{cid}")
    public HashMap<String, Object> pushToWeb(@PathVariable String cid, String message) {
        try {
            TestWebSocketServer.sendInfo(message,cid);
        } catch (IOException e) {
            e.printStackTrace();
            return ApiReturnUtil.error(cid+"#"+e.getMessage());
        }
        return ApiReturnUtil.success(cid);
    }

}
