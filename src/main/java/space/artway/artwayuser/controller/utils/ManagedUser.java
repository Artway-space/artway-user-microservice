package space.artway.artwayuser.controller.utils;

import lombok.Data;
import space.artway.artwayuser.service.dto.UserDto;

import javax.validation.constraints.Size;

@Data
public class ManagedUser extends UserDto {

    public static final int PASSWORD_MIN_LENGTH = 4;
    public static final int PASSWORD_MAX_LENGTH = 64;

    @Size(min = PASSWORD_MIN_LENGTH, max = PASSWORD_MAX_LENGTH)
    private String password;
}
