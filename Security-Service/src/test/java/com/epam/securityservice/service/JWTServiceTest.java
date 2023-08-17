package com.epam.securityservice.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Value;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Mockito.*;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.security.Key;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class JWTServiceTest {
    @InjectMocks
    private JWTService jwtService;

    @Mock
    private Keys keys;



    @Mock
    private Decoders decoders;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setField(jwtService, "SECRET", "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970"); // Set the SECRET value here
    }


    @Test
    public void testGenerateToken() {
        String userName = "testUser";
        String token = jwtService.generateToken(userName);
        assertNotNull(token);
    }



}
