package kg.mlsp.staffcontrol.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
public class UserSearchDto {
    private String firstName;
    private String lastName;
    private String middleName;
    private String phone;
    private String pin;
    
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime createdAtFrom;
    
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime createdAtTo;
    
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime updatedAtFrom;
    
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime updatedAtTo;

    public boolean isEmpty() {
        return (firstName == null || firstName.isBlank())
                && (lastName == null || lastName.isBlank())
                && (middleName == null || middleName.isBlank())
                && (phone == null || phone.isBlank())
                && (pin == null || pin.isBlank())
                && createdAtFrom == null
                && createdAtTo == null
                && updatedAtFrom == null
                && updatedAtTo == null;
    }
}
