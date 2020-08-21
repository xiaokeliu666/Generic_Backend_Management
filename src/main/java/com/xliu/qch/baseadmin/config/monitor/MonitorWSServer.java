package com.xliu.qch.baseadmin.config.monitor;


import com.xliu.qch.baseadmin.config.websocket.MyEndpointConfigure;
import com.xliu.qch.baseadmin.util.ErrorUtil;
import com.xliu.qch.baseadmin.util.SystemMonitorUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Using WebSocket to monitor the system and output to the web page
 */
@Slf4j
@Component
@ServerEndpoint(value = "/websocket/monitor", configurator = MyEndpointConfigure.class)
public class MonitorWSServer {

    @Value("${spring.application.name}")
    private String applicationName;

    /**
     * Collection of connections
     */
    private static Map<String, Session> sessionMap = new ConcurrentHashMap<String, Session>();

    /**
     * Method to be called after building connection successfully
     */
    @OnOpen
    public void onOpen(Session session) {
        // Add session into collection
        sessionMap.put(session.getId(), session);

        // Get the System monitor information
        new Thread(()->{
            log.info("MonitorWSServer Task start");
            ObjectMapper mapper = new ObjectMapper();
            // When Value is null or "", skip serialization to reduce the data transportation
            mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
            mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
            while (sessionMap.get(session.getId()) != null) {
                try {
                    // Get the system monitor information and send
                    send(session,  mapper.writeValueAsString(SystemMonitorUtil.getSysMonitor()));

                    // Sleep for 1 sec
                    Thread.sleep(1000);
                } catch (Exception e) {
                    log.error(ErrorUtil.errorInfoToString(e));
                }
            }
            log.info("MonitorWSServer Task done!");
        }).start();
    }

    /**
     * Close the method
     */
    @OnClose
    public void onClose(Session session) {
        // Remove session from the collection
        sessionMap.remove(session.getId());
    }

    /**
     * Called when error occurs
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error(ErrorUtil.errorInfoToString(error));
    }

    /**
     * When server receive the message from client
     */
    @OnMessage
    public void onMessage(String message, Session session) {

    }

    /**
     * Send message to the frontend
     */
    private void send(Session session, String message) {
        try {
            session.getBasicRemote().sendText(message);
        } catch (Exception e) {
            log.error(ErrorUtil.errorInfoToString(e));
        }
    }
}
