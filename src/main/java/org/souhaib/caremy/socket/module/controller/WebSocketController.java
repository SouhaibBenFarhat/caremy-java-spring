package org.souhaib.caremy.socket.module.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;

import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class WebSocketController {

    private final SimpMessagingTemplate template;

    @Autowired
    WebSocketController(SimpMessagingTemplate template){
        this.template = template;
    }

    @MessageMapping("/send/message")
    @SendTo("/chat")
    public void onReceivedMesage(String message, SimpMessageHeaderAccessor headerAccessor){
        System.out.println("session id:");
        System.out.println(headerAccessor.getSessionId());


        System.out.println("Authorization;");
        System.out.println(headerAccessor.getMessageHeaders().get("Authorization"));
        this.template.convertAndSend("/chat",  new SimpleDateFormat("HH:mm:ss").format(new Date())+"- "+message);
    }


}
