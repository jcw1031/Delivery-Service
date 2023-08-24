package org.delivery.storeadmin.domain.sse.connection.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.delivery.storeadmin.domain.sse.connection.ifs.ConnectionPool;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@ToString
@EqualsAndHashCode
@Getter
public class UserSseConnection {

    private final String uniqueKey;
    private final SseEmitter sseEmitter;
    private final ConnectionPool<String, UserSseConnection> connectionPool;
    private final ObjectMapper objectMapper;

    private UserSseConnection(String uniqueKey, ConnectionPool<String, UserSseConnection> connectionPool, ObjectMapper objectMapper) {
        this.uniqueKey = uniqueKey;
        this.connectionPool = connectionPool;
        this.objectMapper = objectMapper;
        sseEmitter = new SseEmitter(60 * 1_000L);

        sseEmitter.onCompletion(() -> connectionPool.onCompletionCallback(this));

        sseEmitter.onTimeout(sseEmitter::complete);

        sendMessage("onopen", "connect");
    }

    public static UserSseConnection connect(String uniqueKey, ConnectionPool<String, UserSseConnection> connectionPool, ObjectMapper objectMapper) {
        return new UserSseConnection(uniqueKey, connectionPool, objectMapper);
    }

    public void sendMessage(Object data) {
        try {
            String jsonData = objectMapper.writeValueAsString(data);
            SseEmitter.SseEventBuilder event = SseEmitter.event()
                    .data(jsonData);
            sseEmitter.send(event);
        } catch (IOException e) {
            this.sseEmitter.completeWithError(e);
        }
    }

    public void sendMessage(String eventName, Object data) {
        try {
            String jsonData = objectMapper.writeValueAsString(data);
            SseEmitter.SseEventBuilder event = SseEmitter.event()
                    .name(eventName)
                    .data(jsonData);
            sseEmitter.send(event);
        } catch (IOException e) {
            this.sseEmitter.completeWithError(e);
        }
    }
}
