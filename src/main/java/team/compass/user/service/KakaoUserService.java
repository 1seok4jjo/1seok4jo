package team.compass.user.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Service;
import team.compass.user.domain.User;
import team.compass.user.dto.KakaoUserDto;
import team.compass.user.dto.TokenDto;

import java.io.IOException;

@Service
public interface KakaoUserService {
    TokenDto kakaoSignIn(String code) throws IOException;

    User signUpKakaoUser(String accessToken) throws IOException;

    JsonNode getKakaoUserInfo(String accessToken) throws JsonProcessingException;
}
