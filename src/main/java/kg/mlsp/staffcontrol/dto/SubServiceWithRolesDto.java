package kg.mlsp.staffcontrol.dto;

import lombok.Data;

import java.util.List;

@Data
public class SubServiceWithRolesDto {
    private Integer id;
    private String nameRu;
    private String nameKy;
    private Boolean isActive;
    private Integer orderNumber;
    private String code;
    private List<RoleWithActionsDto> roles;

    public SubServiceWithRolesDto(Integer id, String nameRu, String nameKy, Boolean isActive, Integer orderNumber, String code, List<RoleWithActionsDto> roles) {
        this.id = id;
        this.nameRu = nameRu;
        this.nameKy = nameKy;
        this.isActive = isActive;
        this.orderNumber = orderNumber;
        this.code = code;
        this.roles = roles;
    }
}
