package team.compass.user.service;

import org.springframework.stereotype.Service;
import team.compass.post.controller.response.PostResponse;
import team.compass.post.domain.Post;
import team.compass.user.domain.User;
import team.compass.user.dto.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public interface UserService {
    User signUp(UserRequest.SignUp parameter);
    TokenDto signIn(UserRequest.SignIn parameter);

    User updateUserInfo(UserUpdate parameter, HttpServletRequest request);
    void logout(HttpServletRequest request);
    void withdraw(HttpServletRequest request);


    UserPostResponse getUserByPost(HttpServletRequest request);

    UserPostResponse getUserLikeByPost(HttpServletRequest request);

    TokenDto reissue(TokenDto tokenRequestDto);
}
