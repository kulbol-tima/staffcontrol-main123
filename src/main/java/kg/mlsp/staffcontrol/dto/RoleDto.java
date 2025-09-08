package kg.mlsp.staffcontrol.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "DTO роли")
public class RoleDto {
    private Integer id;
    private String nameRu;
    private String nameKy;
    private String code;
    private Integer orderNumber;
    private Boolean isActive;
    private SubServiceDto subService;
}
