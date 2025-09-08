package kg.mlsp.staffcontrol.dto;

import kg.mlsp.staffcontrol.dto.request.SubServiceIdsWithRolesDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserSubServiceRoleActionCreateDto {
    private UUID userId;

    private List<SubServiceIdsWithRolesDto> subServiceId;
}
