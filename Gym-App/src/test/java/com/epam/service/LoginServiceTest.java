package com.epam.service;

import com.epam.ObjectPreparation;
import com.epam.dtos.request.ChangePasswordDto;

import com.epam.entities.*;
import com.epam.exceptions.UserException;
import com.epam.repositories.*;
import com.epam.services.implementations.LoginServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;

@ExtendWith(MockitoExtension.class)
public class LoginServiceTest {
    @Mock
    UserRepository userRepository;

    @InjectMocks
    LoginServiceImpl loginService;


    @Test
    void login_Success() {
        String username = "john.doe@example.com";
        String password = "12345";
        User user = ObjectPreparation.createValidUser();

        Mockito.when(userRepository.findUserByUsernameAndPassword(username, password))
                .thenReturn(Optional.of(user));

        User result = loginService.login(username, password);

        assertNotNull(result);
        assertEquals(username, result.getUsername());
        assertEquals(password, result.getPassword());

        Mockito.verify(userRepository).findUserByUsernameAndPassword(eq(username), eq(password));
    }


    @Test
    void login_InvalidCredentials_ThrowsUserException() {
        String username = "john.doe@example.com";
        String password = "password123";

        Mockito.when(userRepository.findUserByUsernameAndPassword(username, password))
                .thenReturn(Optional.empty());

        assertThrows(UserException.class, () -> loginService.login(username, password));

        Mockito.verify(userRepository).findUserByUsernameAndPassword(eq(username), eq(password));
    }

    @Test
    void changePassword_Success() {
        String username = "john.doe@example.com";
        String password = "12345";
        String newPassword = "newpassword123";

        User user = ObjectPreparation.createValidUser();

        ChangePasswordDto changePasswordDto = new ChangePasswordDto(username, password, newPassword);

        Mockito.when(userRepository.findUserByUsernameAndPassword(username, password))
                .thenReturn(Optional.of(user));
        Mockito.when(userRepository.save(any()))
                .thenReturn(user);

        assertDoesNotThrow(() -> loginService.changePassword(changePasswordDto));

        Mockito.verify(userRepository).findUserByUsernameAndPassword(eq(username), eq(password));
        Mockito.verify(userRepository).save(any());
    }

    @Test
    void changePassword_InactiveUser_ThrowsUserException() {
        String username = "john.doe@example.com";
        String password = "12345";
        String newPassword = "newpassword123";

        User user = ObjectPreparation.createValidUser();
        user.setActive(false);

        ChangePasswordDto changePasswordDto = new ChangePasswordDto(username, password, newPassword);

        Mockito.when(userRepository.findUserByUsernameAndPassword(username, password))
                .thenReturn(Optional.of(user));

        assertThrows(UserException.class, () -> loginService.changePassword(changePasswordDto));

        Mockito.verify(userRepository).findUserByUsernameAndPassword(eq(username), eq(password));
        Mockito.verifyNoMoreInteractions(userRepository);
    }

}
