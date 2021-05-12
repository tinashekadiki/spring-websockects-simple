package com.dot.simplews.handlers;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.ArrayList;
import java.util.List;

public class ChatWebSocketHandler extends TextWebSocketHandler {
    
    private final List<WebSocketSession> webSocketSessionList = new ArrayList<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        webSocketSessionList.add(session);
        TextMessage connectionMessage = new TextMessage(session.getId()+" connected");
        broadCastMessage(session, connectionMessage);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        broadCastMessage(session, message);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        webSocketSessionList.remove(session);
        TextMessage disconnectionMessage = new TextMessage(session.getId()+" disconnected");
        broadCastMessage(session, disconnectionMessage);
    }

    public void broadCastMessage(WebSocketSession session, TextMessage message) throws Exception {
        for (WebSocketSession websocketSession: webSocketSessionList) {
            if(!session.getId().equals(websocketSession.getId())){
                websocketSession.sendMessage(message);
            }
        }
    }
}
