package kg.mlsp.staffcontrol.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StaffDto {
    private Integer id;
    private String pin;
    private String firstName;
    private String lastName;
    private String middleName;
    private LocalDate birthDate;
    private String phone;
    private OrganizationDto organization;
    private PositionDto position;
}
