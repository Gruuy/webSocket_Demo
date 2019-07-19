package com.websocket.demo.service.serviceImpl;

import com.websocket.demo.service.TestService;
import org.springframework.stereotype.Service;

/**
 * @author: Gruuy
 * @remark:
 * @date: Create in 14:32 2019/7/19
 */
@Service
public class TestServiceImpl implements TestService {

    @Override
    public String test() {
        return "test";
    }
}
