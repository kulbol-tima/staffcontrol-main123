package kg.mlsp.staffcontrol.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kg.mlsp.staffcontrol.dto.LoggingEventDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
public class LoggingServiceImpl implements LoggingService {

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;
    
    public LoggingServiceImpl(RabbitTemplate rabbitTemplate, @Qualifier("rabbitObjectMapper") ObjectMapper objectMapper) {
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = objectMapper;
    }
    
    private static final String EXCHANGE_NAME = "mlsp-logging-exchange";
    private static final String ROUTING_KEY = "staffcontrol.logging";
    private static final String SOURCE_SYSTEM = "staffcontrol";

    @Override
    public void logCreate(String entityType, String entityId, Object newData, String userId, String username) {
        LoggingEventDto event = LoggingEventDto.builder()
                .requestBy(username != null ? username : "system")
                .sourceSystem(SOURCE_SYSTEM)
                .requestBody(serializeObject(newData))
                .objectType(entityType)
                .objectId(entityId)
                .action("CREATE")
                .timestamp(LocalDateTime.now())
                .build();
        
        sendMessage(event);
    }

    @Override
    public void logUpdate(String entityType, String entityId, Object oldData, Object newData, String userId, String username) {
        LoggingEventDto event = LoggingEventDto.builder()
                .requestBy(username != null ? username : "system")
                .sourceSystem(SOURCE_SYSTEM)
                .requestBody(serializeObject(newData))
                .objectType(entityType)
                .objectId(entityId)
                .action("UPDATE")
                .timestamp(LocalDateTime.now())
                .build();
        
        sendMessage(event);
    }

    @Override
    public void logDelete(String entityType, String entityId, Object oldData, String userId, String username) {
        LoggingEventDto event = LoggingEventDto.builder()
                .requestBy(username != null ? username : "system")
                .sourceSystem(SOURCE_SYSTEM)
                .requestBody(serializeObject(oldData))
                .objectType(entityType)
                .objectId(entityId)
                .action("DELETE")
                .timestamp(LocalDateTime.now())
                .build();
        
        sendMessage(event);
    }

    private String serializeObject(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error("Error serializing object", e);
            return "{}";
        }
    }

    private void sendMessage(LoggingEventDto event) {
        try {
            // Проверяем, что RabbitMQ доступен
            if (!isRabbitMQAvailable()) {
                log.warn("RabbitMQ is not available, skipping message: {} - {} - {}", 
                        event.getAction(), event.getObjectType(), event.getObjectId());
                return;
            }

            String message = objectMapper.writeValueAsString(event);
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, ROUTING_KEY, message);
            log.info("Sent logging event: {} - {} - {}", event.getAction(), event.getObjectType(), event.getObjectId());
        } catch (JsonProcessingException e) {
            log.error("Error serializing logging event", e);
        } catch (AmqpException e) {
            log.error("RabbitMQ error sending message: {} - {} - {}", 
                    event.getAction(), event.getObjectType(), event.getObjectId(), e);
        } catch (Exception e) {
            log.error("Unexpected error sending message to RabbitMQ: {} - {} - {}", 
                    event.getAction(), event.getObjectType(), event.getObjectId(), e);
        }
    }

    private boolean isRabbitMQAvailable() {
        try {
            // Простая проверка доступности RabbitMQ
            rabbitTemplate.execute(channel -> {
                // Проверяем, что канал активен
                if (channel.isOpen()) {
                    // Пытаемся выполнить простую операцию
                    try {
                        // Проверяем доступность обмена
                        channel.exchangeDeclarePassive(EXCHANGE_NAME);
                        return true;
                    } catch (Exception e) {
                        // Если обмен недоступен, логируем детали
                        log.debug("Exchange '{}' is not accessible: {}", EXCHANGE_NAME, e.getMessage());
                        return false;
                    }
                }
                return false;
            });
            return true;
        } catch (Exception e) {
            log.debug("RabbitMQ is not available: {}", e.getMessage());
            return false;
        }
    }

    @Override
    public void logEvent(String requestBy, String objectType, String objectId, String action, Object requestBody) {
        logEvent(requestBy, objectType, objectId, action, requestBody, SOURCE_SYSTEM);
    }

    @Override
    public void logEvent(String requestBy, String objectType, String objectId, String action, Object requestBody, String sourceSystem) {
        LoggingEventDto event = LoggingEventDto.builder()
                .requestBy(requestBy != null ? requestBy : "system")
                .sourceSystem(sourceSystem != null ? sourceSystem : SOURCE_SYSTEM)
                .requestBody(serializeObject(requestBody))
                .objectType(objectType)
                .objectId(objectId)
                .action(action)
                .timestamp(LocalDateTime.now())
                .build();
        
        sendMessage(event);
    }
}
