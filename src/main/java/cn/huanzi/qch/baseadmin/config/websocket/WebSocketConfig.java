package cn.huanzi.qch.baseadmin.config.websocket;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * WebSocket
 */
@Configuration
public class WebSocketConfig {


    /**
     * Scan the register all beans annotated with @ServerEndpoint. @ServerEndpoint("/websocket")
     */
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

    /**
     * Support inject to other Class
     */
    @Bean
    public MyEndpointConfigure newMyEndpointConfigure (){
        return new MyEndpointConfigure ();
    }
}
