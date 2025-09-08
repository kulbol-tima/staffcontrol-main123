package kg.mlsp.staffcontrol.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "User update data transfer object")
public class UserUpdateDto {
    private String email;
    private Boolean isActive;

    private String phone;
    private Integer organizationId;
    private Integer positionId;
}