package kg.mlsp.staffcontrol.controller;

import kg.mlsp.staffcontrol.service.LoggingService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/staffcontrol/test-logging")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class TestLoggingController {

    private final LoggingService loggingService;
    private final RabbitTemplate rabbitTemplate;

    @PostMapping("/test-event")
    public String testLoggingEvent(@RequestBody Map<String, Object> testData) {
        String requestBy = (String) testData.getOrDefault("requestBy", "test-user");
        String objectType = (String) testData.getOrDefault("objectType", "TestObject");
        String objectId = (String) testData.getOrDefault("objectId", "test-123");
        String action = (String) testData.getOrDefault("action", "TEST");
        Object requestBody = testData.getOrDefault("requestBody", testData);

        loggingService.logEvent(requestBy, objectType, objectId, action, requestBody);
        
        return "Test event logged successfully";
    }

    @GetMapping("/test-simple")
    public String testSimpleLogging() {
        Map<String, Object> testData = new HashMap<>();
        testData.put("message", "This is a test message");
        testData.put("timestamp", System.currentTimeMillis());
        
        loggingService.logEvent("test-user", "TestObject", "test-456", "CREATE", testData);
        
        return "Simple test event logged successfully";
    }

    @GetMapping("/rabbitmq-status")
    public Map<String, Object> checkRabbitMQStatus() {
        Map<String, Object> status = new HashMap<>();
        
        try {
            Boolean connectionResult = rabbitTemplate.execute(channel -> {
                // Проверяем, что канал активен
                if (channel.isOpen()) {
                    // Пытаемся выполнить простую операцию для проверки соединения
                    try {
                        // Проверяем доступность обмена
                        channel.exchangeDeclarePassive("mlsp-logging-exchange");
                        return true;
                    } catch (Exception e) {
                        // Если обмен недоступен, логируем детали
                        status.put("exchangeError", e.getMessage());
                        return false;
                    }
                }
                return false;
            });
            
            boolean isConnected = connectionResult != null ? connectionResult : false;
            
            status.put("connected", isConnected);
            status.put("status", isConnected ? "OK" : "DISCONNECTED");
            status.put("message", isConnected ? "RabbitMQ connection is active" : "RabbitMQ connection failed");
            
        } catch (Exception e) {
            status.put("connected", false);
            status.put("status", "ERROR");
            status.put("message", "Error checking RabbitMQ status: " + e.getMessage());
            status.put("error", e.getClass().getSimpleName());
        }
        
        return status;
    }
}
