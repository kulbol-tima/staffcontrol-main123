package kg.mlsp.staffcontrol.service;

public interface LoggingService {
    void logCreate(String entityType, String entityId, Object newData, String userId, String username);
    void logUpdate(String entityType, String entityId, Object oldData, Object newData, String userId, String username);
    void logDelete(String entityType, String entityId, Object oldData, String userId, String username);
    
    // Дополнительные методы для более гибкого логирования
    void logEvent(String requestBy, String objectType, String objectId, String action, Object requestBody);
    void logEvent(String requestBy, String objectType, String objectId, String action, Object requestBody, String sourceSystem);
}
