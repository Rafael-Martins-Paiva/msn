package dev.oldschool.msn.websocket;

import dev.oldschool.msn.model.Mensagem;
import dev.oldschool.msn.service.MensagemService;
import dev.oldschool.msn.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component; // Make it a Spring component
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

@Component // Mark as a Spring component
@RequiredArgsConstructor // For constructor injection
public class ChatWebSocketHandler extends TextWebSocketHandler {

    private final MensagemService mensagemService;
    private final RoomService roomService;

    // Map to store sessions per room
    private final Map<Long, Set<WebSocketSession>> roomSessions = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        Long roomId = extractRoomId(session);
        if (roomId == null) {
            session.close(CloseStatus.BAD_DATA.withReason("Room ID not provided in WebSocket URI"));
            return;
        }

        // Check if the room exists
        if (roomService.getRoomById(roomId).isEmpty()) {
            session.close(CloseStatus.NOT_ACCEPTABLE.withReason("Room does not exist"));
            return;
        }

        roomSessions.computeIfAbsent(roomId, k -> new CopyOnWriteArraySet<>()).add(session);
        System.out.println("Session " + session.getId() + " connected to room " + roomId);
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        Long roomId = extractRoomId(session);
        if (roomId == null) {
            session.close(CloseStatus.BAD_DATA.withReason("Room ID not associated with session"));
            return;
        }

        // Parse message content (assuming simple text for now, can be JSON later)
        String payload = message.getPayload();
        // For simplicity, let's assume the payload is just the content and author is session ID for now
        // In a real app, the client would send JSON with author and content
        String author = session.getId(); // Placeholder for author

        Mensagem newMensagem = Mensagem.builder()
                .autor(author)
                .conteudo(payload)
                .roomId(roomId)
                .build();

        // Save message to database
        try {
            mensagemService.salvar(newMensagem);
        } catch (IllegalArgumentException e) {
            session.sendMessage(new TextMessage("Error saving message: " + e.getMessage()));
            return;
        }


        // Broadcast message to all sessions in the same room
        Set<WebSocketSession> sessionsInRoom = roomSessions.get(roomId);
        if (sessionsInRoom != null) {
            for (WebSocketSession s : sessionsInRoom) {
                if (s.isOpen()) {
                    s.sendMessage(message); // Send the original message payload
                }
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        Long roomId = extractRoomId(session);
        if (roomId != null) {
            Set<WebSocketSession> sessionsInRoom = roomSessions.get(roomId);
            if (sessionsInRoom != null) {
                sessionsInRoom.remove(session);
                if (sessionsInRoom.isEmpty()) {
                    roomSessions.remove(roomId); // Remove room entry if no sessions left
                }
            }
        }
        System.out.println("Session " + session.getId() + " disconnected from room " + roomId);
    }

    private Long extractRoomId(WebSocketSession session) {
        // Assuming URI is like /ws/chat/{roomId}
        String path = session.getUri().getPath();
        String[] parts = path.split("/");
        if (parts.length > 0) {
            try {
                // The last part should be the roomId
                return Long.parseLong(parts[parts.length - 1]);
            } catch (NumberFormatException e) {
                // Log error or handle invalid room ID in path
                return null;
            }
        }
        return null;
    }
}
