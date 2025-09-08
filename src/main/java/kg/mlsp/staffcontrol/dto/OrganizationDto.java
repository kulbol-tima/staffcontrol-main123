package kg.mlsp.staffcontrol.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO для организации")
public class OrganizationDto {
    private Integer id;
    private String nameRu;
    private String nameKy;
    private Boolean isActive;
    private Integer orderNumber;
    private String code;
    private String pin;
}