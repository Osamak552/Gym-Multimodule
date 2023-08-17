package com.epam.securityservice.service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;


@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {
    @Mock
    private JWTService jwtService;
    @InjectMocks
    private AuthService authService;


    @Test
    public void testGenerateToken() {
        String username = "testUser";
        String expectedToken = "dummyToken";

        Mockito.when(jwtService.generateToken(username)).thenReturn(expectedToken);

        assertDoesNotThrow(() -> authService.generateToken(username));

        verify(jwtService, times(1)).generateToken(username);
    }

    @Test
    public void testValidateToken() {
        String token = "dummyToken";


        doNothing().when(jwtService).validateToken(token);

        assertDoesNotThrow(() -> authService.validateToken(token));
    }
}
