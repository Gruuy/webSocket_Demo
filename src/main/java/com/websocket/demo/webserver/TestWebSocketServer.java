package com.websocket.demo.webserver;

import com.websocket.demo.service.TestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author: Gruuy
 * @remark: 长连接接口
 * @date: Create in 11:31 2019/7/19
 */
@ServerEndpoint("/websocket/{sid}")
@Component
public class TestWebSocketServer {
    private static Logger log= LoggerFactory.getLogger(TestWebSocketServer.class);

    /** 静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。*/
    private static int onlineCount=0;

    /** concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。*/
    private static CopyOnWriteArraySet<TestWebSocketServer> webSocketSet = new CopyOnWriteArraySet<TestWebSocketServer>();

    /** 与某个客户端的连接会话，需要通过它来给客户端发送数据*/
    private Session session;

    /** 接收sid */
    private String sid="";

    @Autowired
    private TestService testService;

    /**
     * 连接成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session,@PathParam("sid") String sid){
        //设置当前连接
        this.session=session;
        //添加到线程安全集中
        webSocketSet.add(this);
        //在线+1
        synchronized(this) {
            onlineCount++;
        }
        log.info("在线人数+1 当前在线{}",onlineCount);
        this.sid=sid;
    }

    /**
     * 关闭连接调用的方法
     */
    @OnClose
    public void onClose(){
        //删除当前集合的对象
        webSocketSet.remove(this);
        //减一
        synchronized(this) {
            onlineCount--;
        }
        //打印
        log.info("有人下线了！");
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息*/
    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("收到来自窗口"+sid+"的信息:"+message);
        //群发消息
        for (TestWebSocketServer item : webSocketSet) {
            try {
                item.sendMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        log.error("发生错误");
        error.printStackTrace();
    }

    /**
     * 实现服务器主动推送
     */
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }

    /**
     * 群发自定义消息
     * */
    public static void sendInfo(String message,@PathParam("sid") String sid) throws IOException {
        log.info("推送消息到窗口"+sid+"，推送内容:"+message);
        for (TestWebSocketServer item : webSocketSet) {
            try {
                //这里可以设定只推送给这个sid的，为null则全部推送
                if(sid==null) {
                    item.sendMessage(message);
                }else if(item.sid.equals(sid)){
                    item.sendMessage(message);
                }
            } catch (IOException e) {
                continue;
            }
        }
    }
}
