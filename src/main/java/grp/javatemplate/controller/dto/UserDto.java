package grp.javatemplate.controller.dto;

import grp.javatemplate.model.enums.UserRole.Roles;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.Instant;

import static grp.javatemplate.exception.UserException.INVALID_USER_EMAIL;
import static grp.javatemplate.exception.UserException.INVALID_USER_PHONE_NUMBER;
import static grp.javatemplate.model.User.*;

@Data
public class UserDto {
    private Long id;

    @NotNull
    @Size(min = 2)
    @Size(max = 200)
    private String name;

    private Roles role;

    private Instant dob;

    @Size(max = MAX_EMAIL_LEN, message = "Email is too long")
    @Email(message = INVALID_USER_EMAIL, regexp = EMAIL_REGEX)
    private String email;

    @Pattern(regexp = PHONE_NR_REGEX, message = INVALID_USER_PHONE_NUMBER)
    private String phoneNumber;
}
