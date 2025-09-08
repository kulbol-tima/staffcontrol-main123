package kg.mlsp.staffcontrol.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class UserDto {
    private UUID id;
    private String username;
    private String email;
    private Boolean isActive;
    private StaffDto staff;

}