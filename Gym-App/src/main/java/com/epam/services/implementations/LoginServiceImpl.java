package com.epam.services.implementations;

import com.epam.repositories.UserRepository;
import com.epam.dtos.request.ChangePasswordDto;
import com.epam.entities.User;
import com.epam.exceptions.UserException;
import com.epam.services.LoginService;
import com.epam.util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class LoginServiceImpl implements LoginService {

    @Autowired
    private UserRepository userRepository;

    public User login(String username,String password)
    {
        log.info("{}:: login {}",this.getClass().getName(), Constants.EXECUTION_STARTED);
        Optional<User> user = userRepository.findUserByUsernameAndPassword(username,password);
        log.info("{}:: login {}",this.getClass().getName(), Constants.EXECUTION_ENDED);
        return user.orElseThrow(() -> new UserException("Username or password are incorrect"));

    }

    public void changePassword(ChangePasswordDto changePasswordDto)
    {
        log.info("{}:: changePassword {}",this.getClass().getName(), Constants.EXECUTION_STARTED);
        User user = login(changePasswordDto.getUsername(), changePasswordDto.getPassword());
        if(user.isActive())
        {
            user.setPassword(changePasswordDto.getNewPassword());
            userRepository.save(user);
        }
        else{
            throw new UserException("User is inactive");
        }
        log.info("{}:: changePassword {}",this.getClass().getName(), Constants.EXECUTION_ENDED);
    }

}
