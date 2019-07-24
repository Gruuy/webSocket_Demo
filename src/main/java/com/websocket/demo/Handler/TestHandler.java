package com.websocket.demo.Handler;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author: Gruuy
 * @remark:
 * @date: Create in 15:04 2019/7/19
 */
public class TestHandler implements InvocationHandler {
    private Object object;

    public TestHandler(Object object) {
        this.object = object;
    }

    /** 执行的方法 */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object result=null;
        doBefore();
        result=method.invoke(object,args);
        doAfter();
        return result;
    }

    private void doBefore(){
        System.out.println("Before");
    }

    private void doAfter(){
        System.out.println("After");
    }
}
