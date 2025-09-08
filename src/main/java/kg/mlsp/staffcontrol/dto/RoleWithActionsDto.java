package kg.mlsp.staffcontrol.dto;

import lombok.Data;

import java.util.List;

@Data
public class RoleWithActionsDto {
    private Integer id;
    private String nameRu;
    private String nameKy;
    private String code;
    private Integer orderNumber;

    private  List<ActionDto> actions;

    public RoleWithActionsDto(Integer id, String nameRu, String nameKy, String code, Integer orderNumber, List<ActionDto> actions) {
        this.id = id;
        this.nameRu = nameRu;
        this.nameKy = nameKy;
        this.code = code;
        this.orderNumber = orderNumber;
        this.actions = actions;
    }
}
