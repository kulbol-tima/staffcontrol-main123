package kg.mlsp.staffcontrol.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

@Data
@Schema(description = "User creation data transfer object")
public class UserCreateDto {
    private String email;
    private Boolean isActive;

    private String firstName;
    private String lastName;
    private String middleName;
    private LocalDate birthDate;
    private String phone;
    private Integer organizationId;
    private Integer positionId;
}
