package team.compass.user.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import team.compass.user.dto.KakaoUserDto;
import team.compass.user.dto.TokenDto;
import team.compass.user.service.KakaoUserService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class KakaoUserController {
    private final KakaoUserService kakaoUserService;
    @GetMapping("/user/oauth")
    public ResponseEntity<?> kakaoLogin(
            @RequestParam String code,
            HttpServletResponse response
    ) throws IOException {
        TokenDto tokenDto = kakaoUserService.kakaoSignIn(code);

        response.addHeader("Authorization", tokenDto.getAccessToken());

        return ResponseEntity.ok().body(tokenDto);
    }
}
