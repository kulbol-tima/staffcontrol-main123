package kg.mlsp.staffcontrol.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserSubServiceRoleActionSearchDto {
    private Integer actionId;
    private Integer roleId;
    private Integer subServiceId;
    private UUID userId;
}
