package team.compass.user.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import team.compass.user.domain.User;
import team.compass.user.dto.TokenDto;
import team.compass.user.dto.UserPostResponse;
import team.compass.user.dto.UserRequest;
import team.compass.user.dto.UserUpdate;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Service
public interface UserService {
    User signUp(UserRequest.SignUp parameter, Map<String, MultipartFile> multipartFileMap);
    TokenDto signIn(UserRequest.SignIn parameter);

    User updateUser(UserUpdate parameter, HttpServletRequest request);
    void logout(HttpServletRequest request);
    void withdraw(HttpServletRequest request);


    UserPostResponse getUserByPost(HttpServletRequest request);

    UserPostResponse getUserByLikePost(HttpServletRequest request);

    TokenDto reissue(TokenDto tokenRequestDto);
}
