package kg.mlsp.staffcontrol.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO роли")
public class RoleCreateUpdateDto {
    private String nameRu;
    private String nameKy;
    private String code;
    private Boolean isActive;
    private Integer orderNumber;
    private Integer subServiceId;
}
