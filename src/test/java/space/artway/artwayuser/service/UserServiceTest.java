package space.artway.artwayuser.service;

import com.google.common.collect.ImmutableSet;
import javassist.NotFoundException;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.jeasy.random.FieldPredicates;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import space.artway.artwayuser.controller.exceptions.NoPermissionException;
import space.artway.artwayuser.controller.exceptions.UserAlreadyExistException;
import space.artway.artwayuser.domain.Authority;
import space.artway.artwayuser.domain.User;
import space.artway.artwayuser.repository.UserRepository;
import space.artway.artwayuser.service.dto.AuthoritiesConstants;
import space.artway.artwayuser.service.dto.UserDto;
import space.artway.artwayuser.service.mapper.UserMapper;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;


class UserServiceTest {
    private final UserRepository userRepository = Mockito.mock(UserRepository.class);
    private final EmailService emailService = Mockito.mock(EmailService.class);
    private final UserService userService = new UserService(userRepository, new UserMapper(), emailService);

    @Test
    @DisplayName("Find user by ID")
    void findUserById() throws NotFoundException {
        long userId = 1234L;
        User user = new EasyRandom().nextObject(User.class);
        user.setAuthorities(ImmutableSet.of(new Authority("CREATOR")));
        when(userRepository.findById(eq(userId))).thenReturn(Optional.of(user));

        UserDto result = userService.findUserById(userId);
        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(user.getUsername(), result.getUsername()),
                () -> assertEquals(user.getFirstName(), result.getFirstName()),
                () -> assertEquals(user.getLastName(), result.getLastName()),
                () -> assertEquals(user.getEmail(), result.getEmail()),
                () -> assertEquals(user.getBirthday(), result.getBirthday()),
                () -> assertEquals(user.getPhone(), result.getPhone()),
                () -> assertEquals(user.isActivated(), result.isActivated()),
                () -> assertEquals(user.getCountry(), result.getCountry())
        );
    }

    @Test
    @DisplayName("Find user by ID user not found")
    void findUserById_UserNotFound() {
        when(userRepository.findById(eq(0L))).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> userService.findUserById(0L));
    }

    @Test
    @DisplayName("Create new user")
    void createUser() {
        UserDto userDto = new EasyRandom(new EasyRandomParameters()
                .randomize(FieldPredicates.named("authorities"), ()->ImmutableSet.of(AuthoritiesConstants.USER))
        ).nextObject(UserDto.class);
        User user = new EasyRandom().nextObject(User.class);

        ArgumentCaptor<User> capturedUser = ArgumentCaptor.forClass(User.class);
        when(userRepository.save(any(User.class))).thenReturn(user);

        userService.createUser(userDto, "SOME_PASSWORD_HASH");
        verify(userRepository).save(capturedUser.capture());
        verify(emailService, times(1)).sendVerifyEmail(anyString(), anyString(), any(Long.class));

        assertAll(
                () -> assertNotNull(capturedUser.getValue()),
                () -> assertEquals(userDto.getUsername(), capturedUser.getValue().getUsername()),
                () -> assertEquals(userDto.getFirstName(), capturedUser.getValue().getFirstName()),
                () -> assertEquals(userDto.getLastName(), capturedUser.getValue().getLastName()),
                () -> assertEquals(userDto.getEmail(), capturedUser.getValue().getEmail()),
                () -> assertEquals(userDto.getBirthday(), capturedUser.getValue().getBirthday()),
                () -> assertEquals(userDto.getPhone(), capturedUser.getValue().getPhone()),
                () -> assertFalse(capturedUser.getValue().isActivated()),
                () -> assertEquals(userDto.getCountry(), capturedUser.getValue().getCountry())
        );
    }

    @Test
    @DisplayName("Create existing user")
    void createExistsUser() {
        UserDto userDto = new UserDto();
        userDto.setUsername("username");
        userDto.setAuthorities(ImmutableSet.of(AuthoritiesConstants.CREATOR));

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(new User()));

        assertThrows(UserAlreadyExistException.class, () -> userService.createUser(userDto, "SOME_PASSWORD_HASH"));
    }

    @Test
    @DisplayName("Create user with role ADMIN")
    void createAdminUser(){
        UserDto userDto = new UserDto();
        userDto.setUsername("username");
        userDto.setAuthorities(ImmutableSet.of(AuthoritiesConstants.ADMIN));

        assertThrows(NoPermissionException.class, () -> userService.createUser(userDto, "SOME_PASSWORD_HASH"));
    }

    @Test
    void activate() {
    }

    @Test
    @DisplayName("Update user")
    void updateUser() throws NotFoundException {
        Long userId = 1L;
        User oldUser = new EasyRandom().nextObject(User.class);
        UserDto updatedUser = new UserDto();
        updatedUser.setEmail("newEmail@example.com");

        ArgumentCaptor<User> capturedUser = ArgumentCaptor.forClass(User.class);

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(oldUser));
        when(userRepository.save(any(User.class))).thenReturn(new User());
        userService.updateUser(userId, updatedUser);

        verify(userRepository).save(capturedUser.capture());
        assertAll(
                () -> assertEquals(capturedUser.getValue().getId(), oldUser.getId()),
                () -> assertEquals(capturedUser.getValue().getEmail(), updatedUser.getEmail()),
                () -> assertEquals(capturedUser.getValue().getUsername(), oldUser.getUsername())
        );
    }

    @Test
    @DisplayName("Update not existing user")
    void updateNotExistingUser(){
        UserDto updatedUser = new UserDto();
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.updateUser(1L, updatedUser));
    }
}