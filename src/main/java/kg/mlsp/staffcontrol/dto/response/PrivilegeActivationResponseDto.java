package kg.mlsp.staffcontrol.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PrivilegeActivationResponseDto {
    private int affected; // сколько строк обновили
}