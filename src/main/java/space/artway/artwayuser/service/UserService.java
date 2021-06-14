package space.artway.artwayuser.service;

import javassist.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import space.artway.artwayuser.controller.exceptions.NoPermissionException;
import space.artway.artwayuser.controller.exceptions.UserAlreadyExistException;
import space.artway.artwayuser.domain.User;
import space.artway.artwayuser.repository.UserRepository;
import space.artway.artwayuser.service.dto.AuthoritiesConstants;
import space.artway.artwayuser.service.dto.UserDto;
import space.artway.artwayuser.service.mapper.UserMapper;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper mapper;
    private final EmailService emailService;

    public UserService(UserRepository userRepository, UserMapper mapper, EmailService emailService) {
        this.userRepository = userRepository;
        this.mapper = mapper;
        this.emailService = emailService;
    }

    public UserDto findUserById(Long id) throws NotFoundException {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            return mapper.toDto(userOptional.get());
        }
        throw new NotFoundException("User not found");
    }

    public void createUser(UserDto userDto, String passwordHash) {
        if (userDto.getAuthorities().contains(AuthoritiesConstants.ADMIN)) {
            throw new NoPermissionException("Only admins can create users with role ADMIN");
        }

        if (userRepository.findByUsername(userDto.getUsername()).isPresent()) {
            throw new UserAlreadyExistException();
        }

        User user = mapper.toEntity(userDto);
        user.setPasswordHash(passwordHash);
        user.setActivationCode(UUID.randomUUID().getMostSignificantBits());

        User savedUser = userRepository.save(user);
        emailService.sendVerifyEmail(savedUser.getEmail(), savedUser.getFirstName() + " " + savedUser.getLastName(), savedUser.getActivationCode());
    }

    public void activate(Long code) {
        Optional<User> userByActivationCode = userRepository.findByActivationCode(code);
        if (userByActivationCode.isPresent()) {
            User user = userByActivationCode.get();
            user.setActivated(true);
            userRepository.save(user);
        }
    }

    @Transactional
    public UserDto updateUser(Long userId, UserDto updatedUser) throws NotFoundException {
        Optional<User> userOptional = userRepository.findById(userId);
        if (!userOptional.isPresent()) {
            throw new NotFoundException("User not found");
        }
        User oldUser = userOptional.get();
        oldUser = mapper.mapChangedFields(oldUser, mapper.toEntity(updatedUser));
        userRepository.save(oldUser);
        return new UserDto();
    }

}
