package com.epam.services;

import com.epam.dtos.request.ChangePasswordDto;
import com.epam.entities.User;

public interface LoginService {
    User login(String username,String password);
    void changePassword(ChangePasswordDto changePasswordDto);

}
