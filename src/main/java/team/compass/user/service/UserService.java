package team.compass.user.service;

import org.springframework.stereotype.Service;
import team.compass.user.domain.User;
import team.compass.user.dto.TokenDto;
import team.compass.user.dto.UserRequest;

import javax.servlet.http.HttpServletRequest;

@Service
public interface UserService {
    User signUp(UserRequest.SignUp parameter);
    TokenDto signIn(UserRequest.SignIn parameter);


    void logout(HttpServletRequest request);

    TokenDto reissue(TokenDto tokenRequestDto);
}
