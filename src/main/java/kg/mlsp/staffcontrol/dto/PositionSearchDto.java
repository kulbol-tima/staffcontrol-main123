package kg.mlsp.staffcontrol.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
public class PositionSearchDto {
    private String nameRu;
    private String nameKy;
    private String code;
    private Boolean isActive;
    
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime createdAtFrom;
    
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime createdAtTo;
    
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime updatedAtFrom;
    
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime updatedAtTo;

    public boolean isEmpty() {
        return (nameRu == null || nameRu.isBlank())
                && (nameKy == null || nameKy.isBlank())
                && (code == null || code.isBlank())
                && createdAtFrom == null
                && createdAtTo == null
                && updatedAtFrom == null
                && updatedAtTo == null;
    }
}
