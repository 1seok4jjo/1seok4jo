package team.compass.user.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import team.compass.common.config.JwtTokenProvider;
import team.compass.user.domain.User;
import team.compass.user.dto.KakaoUserDto;
import team.compass.user.dto.TokenDto;
import team.compass.user.dto.UserRequest;
import team.compass.user.repository.UserRepository;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class KakaoUserServiceImpl implements KakaoUserService {
    @Value("${kakao.client-id}")
    private String clientId;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    @Override
    public TokenDto kakaoSignIn(String code) throws IOException {
        String accessToken = getAccessToken(code, "http://localhost:8080/user/oauth");

        User kakaoUser = signUpKakaoUser(accessToken);

        String userAccessToken = jwtTokenProvider.createAccessToken(kakaoUser.getEmail(), kakaoUser.getRoles());
        String userRefreshToken = jwtTokenProvider.createRefreshToken(kakaoUser.getEmail(), kakaoUser.getRoles());


        return jwtTokenProvider.createTokenDto(userAccessToken, userRefreshToken);
    }


    @Override
    public User signUpKakaoUser(String accessToken) throws IOException {
        JsonNode jsonNode = getKakaoUserInfo(accessToken);

        String kakaoId = String.valueOf(jsonNode.get("id"));
        String kakaoUserAccountJson = String.valueOf(jsonNode.get("kakao_account"));

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode accountValue = objectMapper.readTree(kakaoUserAccountJson);

        String kakaoUserEmail = String.valueOf(accountValue.get("email"));
        User kakaoUser = userRepository.findByEmail(kakaoId).orElse(null);

        if(kakaoUser == null) {
            String kakaoNickname = jsonNode.get("properties").get("nickname").asText();

            // random 패스워드
            String password = UUID.randomUUID().toString();
            String encodedPassword = passwordEncoder.encode(password);

            UserRequest.SignUp parameter = UserRequest.SignUp.builder()
                    .email(kakaoUserEmail)
                    .password(encodedPassword)
                    .nickname(kakaoNickname)
                    .build();

            kakaoUser = userRepository.save(UserRequest.SignUp.toEntity(parameter));
        }

        return kakaoUser;
    }

    @Override
    public JsonNode getKakaoUserInfo(String accessToken) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<MultiValueMap<String, String>> kakaoUserInfoRequest = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoUserInfoRequest,
                String.class
        );

        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readTree(responseBody);
    }

    private String getAccessToken(String code, String redirectUri) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();

        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", clientId);
        body.add("redirect_uri", redirectUri);
        body.add("code", code);


        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(body, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);

        return jsonNode.get("access_token").asText();
    }
}
