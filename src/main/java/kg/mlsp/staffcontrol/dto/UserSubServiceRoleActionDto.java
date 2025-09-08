package kg.mlsp.staffcontrol.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserSubServiceRoleActionDto {
    private UserDto user;
    private List<SubServiceWithRolesDto> subServiceId;
}
