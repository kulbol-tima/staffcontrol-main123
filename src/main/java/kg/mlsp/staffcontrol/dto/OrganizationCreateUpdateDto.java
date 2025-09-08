package kg.mlsp.staffcontrol.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO организации")
public class OrganizationCreateUpdateDto {
    private String nameRu;
    private String nameKy;
    private String code;
    private String pin;
    private Integer orderNumber;
    private Boolean isActive;
}
