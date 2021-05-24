package space.artway.artwayuser.service.mapper;

import space.artway.artwayuser.domain.User;
import space.artway.artwayuser.service.dto.UserDto;
import org.springframework.stereotype.Service;

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
        return user;
    }
}
