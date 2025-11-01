package com.example.chatserver.WebsocketConfigure;

import com.example.chatserver.security.JwtAuthenticationFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.JWSObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.converter.*;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import java.util.Collections;
import java.util.List;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // Enable broker for topics/queues; user destinations will translate to
        // actual "/queue" destinations internally.
        registry.enableSimpleBroker("/topic", "/queue");
        registry.setApplicationDestinationPrefixes(("/app"));
        registry.setUserDestinationPrefix("/user");
    }

    @Override
    public boolean configureMessageConverters(List<MessageConverter> messageConverters) {
        DefaultContentTypeResolver resolver = new DefaultContentTypeResolver();
        resolver.setDefaultMimeType(APPLICATION_JSON);
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setObjectMapper(new ObjectMapper());
        converter.setContentTypeResolver(resolver);
        messageConverters.add(converter);
        messageConverters.add(new StringMessageConverter());
        messageConverters.add(new ByteArrayMessageConverter());

        return false;
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor =
                        MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

                if (StompCommand.CONNECT.equals(accessor.getCommand())) {

                    String authHeader = accessor.getFirstNativeHeader("Authorization");
                    String token = null;
                    if (authHeader != null && authHeader.startsWith("Bearer ")) {
                        token = authHeader.substring(7);
                    }

                    try {
                        if (token != null && jwtAuthenticationFilter.introspectToken(token)) {
                            JWSObject jwsObject = JWSObject.parse(token);
                            String username = (String) jwsObject.getPayload().toJSONObject().get("sub");

                            // Use the username String as the Principal name so user-destinations match
                            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                    username,
                                    null,
                                    Collections.singletonList(new SimpleGrantedAuthority("USER"))
                            );

                            accessor.setUser(authentication);
                        }
                    } catch (Exception e) {
                        System.err.println("Lỗi xác thực STOMP token: " + e.getMessage());
                    }
                }
                return message;
            }
        });
    }
}
