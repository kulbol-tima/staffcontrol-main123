package kg.mlsp.staffcontrol.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class RoleIdsWithActionDto {

    private Integer id;

    private  List<Integer> actionIds;
}
