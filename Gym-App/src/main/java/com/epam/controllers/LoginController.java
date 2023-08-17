package com.epam.controllers;

import com.epam.dtos.request.ChangePasswordDto;
import com.epam.dtos.request.LoginCredential;
import com.epam.exceptions.UserException;
import com.epam.services.LoginService;
import com.epam.util.Constants;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/home/login")
@Slf4j
public class LoginController {

    @Autowired
    private LoginService loginService;


    @PostMapping("")
    @ResponseStatus(code = HttpStatus.OK)
    public void login(@RequestBody @Valid LoginCredential loginCredential){
        log.info("{}:: login {}  ",this.getClass().getName(), Constants.EXECUTION_STARTED);
        if(!loginService.login(loginCredential.getUsername(),loginCredential.getPassword()).isActive())
        {
            throw new UserException("User is inactive");
        }


        log.info("{}:: login {}  ",this.getClass().getName(), Constants.EXECUTION_ENDED);
    }

    @PutMapping("/change-password")
    @ResponseStatus(code = HttpStatus.OK)
    public void changePassword(@RequestBody @Valid ChangePasswordDto changePasswordDto){
        loginService.changePassword(changePasswordDto);
    }
}
