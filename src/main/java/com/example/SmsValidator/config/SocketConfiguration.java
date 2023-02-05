package com.example.SmsValidator.config;

import com.example.SmsValidator.socket.ModemSocketHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class SocketConfiguration implements WebSocketConfigurer {
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(getSocketHandler(), "/socket").setAllowedOrigins("*");
    }

    @Bean
    public ModemSocketHandler getSocketHandler() {
        return new ModemSocketHandler();
    }

}
