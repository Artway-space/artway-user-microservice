package space.artway.artwayuser.service.dto;

import lombok.Data;
import java.util.Date;

@Data
public class UserDto {
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private Date birthday;
    private String password_hash;
    private boolean activated;
    private String country;
    private AuthoritiesEnum authority;
}
