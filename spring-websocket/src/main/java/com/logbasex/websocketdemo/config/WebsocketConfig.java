package com.logbasex.websocketdemo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebsocketConfig implements WebSocketMessageBrokerConfigurer {
	
	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		//https://stackoverflow.com/questions/52731686/websocket-vs-sockjs-object
		//https://www.slideshare.net/AhmedurRahmanShovon/websockets-and-sockjs-real-time-chatting
		//Enable STOMP support by register STOMP endpoint /ws
		registry.addEndpoint("/ws")
				//when client is set up outside this project.
				.setAllowedOriginPatterns("*")
//				.setAllowedOrigins("http://127.0.0.1:46809")
				.withSockJS();
	}
	
	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		registry.setApplicationDestinationPrefixes("/app");
		registry.enableSimpleBroker("/topic");
	}
}
