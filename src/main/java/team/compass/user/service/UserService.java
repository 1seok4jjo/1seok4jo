package team.compass.user.service;

import org.springframework.stereotype.Service;
import team.compass.user.domain.User;
import team.compass.user.dto.TokenDto;
import team.compass.user.dto.UserRequest;
import team.compass.user.dto.UserResponse;
import team.compass.user.dto.UserUpdate;

import javax.servlet.http.HttpServletRequest;

@Service
public interface UserService {
    User signUp(UserRequest.SignUp parameter);
    UserResponse signIn(UserRequest.SignIn parameter);

    User updateUserInfo(UserUpdate parameter, HttpServletRequest request);


    void logout(HttpServletRequest request);

    TokenDto reissue(TokenDto tokenRequestDto);
}
