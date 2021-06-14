package space.artway.artwayuser.service.mapper;

import space.artway.artwayuser.domain.Authority;
import space.artway.artwayuser.domain.User;
import space.artway.artwayuser.service.dto.AuthoritiesConstants;
import space.artway.artwayuser.service.dto.UserDto;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserMapper {

    public User toEntity(UserDto dto) {
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        user.setBirthday(dto.getBirthday());
        user.setCountry(dto.getCountry());
        user.setAuthorities(authoritiesFromEnum(dto.getAuthorities()));
        return user;
    }

    public UserDto toDto(User entity) {
        UserDto user = new UserDto();
        user.setUsername(entity.getUsername());
        user.setFirstName(entity.getFirstName());
        user.setLastName(entity.getLastName());
        user.setBirthday(entity.getBirthday());
        user.setEmail(entity.getEmail());
        user.setPhone(entity.getPhone());
        user.setCountry(entity.getCountry());
        user.setActivated(entity.isActivated());
        user.setAuthorities(authoritiesFromEntity(entity.getAuthorities()));
        return user;
    }

    public User mapChangedFields(User oldUser, User newUser) {
        oldUser.setUsername(setIfNotNull(oldUser.getUsername(), newUser.getUsername()));
        oldUser.setEmail(setIfNotNull(oldUser.getEmail(), newUser.getEmail()));
        oldUser.setPhone(setIfNotNull(oldUser.getPhone(), newUser.getPhone()));
        return oldUser;
    }

    private <T> T setIfNotNull(T oldField, T newField) {
        if (newField != null) {
            return newField;
        }
        return oldField;
    }

    private Set<Authority> authoritiesFromEnum(Set<AuthoritiesConstants> authoritiesConstants) {
        Set<Authority> authorities = new HashSet<>();

        if (authoritiesConstants != null) {
            authorities = authoritiesConstants.stream()
                    .map(authorityName -> {
                        Authority auth = new Authority();
                        auth.setName(authorityName.name());
                        return auth;
                    })
                    .collect(Collectors.toSet());
        }
        return authorities;
    }

    private Set<AuthoritiesConstants> authoritiesFromEntity(Set<Authority> authorities) {
        Set<AuthoritiesConstants> authoritiesConstants = new HashSet<>();

        if (authorities != null) {
            authoritiesConstants = authorities.stream()
                    .map(authority -> AuthoritiesConstants.valueOf(authority.getName()))
                    .collect(Collectors.toSet());
        }
        return authoritiesConstants;
    }
}
