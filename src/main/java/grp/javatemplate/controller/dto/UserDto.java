package grp.javatemplate.controller.dto;

import grp.javatemplate.model.enums.UserRole;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.Instant;

@Data
public class UserDto {
    private Long id;

    @NotNull
    @Size(min = 2)
    @Size(max = 200)
    private String name;

    private UserRole role;

    private Instant dob;
}
