package space.artway.artwayuser.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.util.Date;

@Data
public class UserDto {
    @Size(min = 1, max = 64)
    @JsonProperty(required = true)
    @JsonPropertyDescription("User's username")
    private String username;

    @JsonPropertyDescription("First name")
    private String firstName;

    @JsonPropertyDescription("Last name")
    private String lastName;

    @Email
    @JsonProperty(required = true)
    @JsonPropertyDescription("User's email")
    private String email;

    @JsonPropertyDescription("User's phone")
    private String phone;

    @JsonProperty(required = true)
    @JsonPropertyDescription("Birthday of user")
    private Date birthday;

    @JsonPropertyDescription("Flag of activation. False if account is inactive")
    private boolean activated;

    @JsonPropertyDescription("Country of user")
    private String country;

    private AuthoritiesEnum authority;
}
