package kg.mlsp.staffcontrol.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class SubServiceIdsWithRolesDto {
    private Integer id;
    private List<RoleIdsWithActionDto> roleIds;
}
