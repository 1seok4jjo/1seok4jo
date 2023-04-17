package team.compass.user.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import team.compass.common.utils.ResponseUtils;
import team.compass.user.dto.KakaoUserDto;
import team.compass.user.dto.TokenDto;
import team.compass.user.service.KakaoUserService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class KakaoUserController {
    private final KakaoUserService kakaoUserService;
    @GetMapping("/api/member/oauth")
    public ResponseEntity<?> kakaoLogin(
            @RequestParam String code,
            HttpServletResponse response
    ) throws IOException {
        TokenDto tokenDto = kakaoUserService.kakaoSignIn(code);

        if(ObjectUtils.isEmpty(tokenDto)) {
            return ResponseUtils.badRequest("카카오 소셜 로그인에 실패하였습니다.");
        }

        response.addHeader("Authorization", tokenDto.getAccessToken());

        return ResponseUtils.ok("카카오 소셜 로그인 되었습니다.", tokenDto);
    }
}
