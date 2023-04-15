package team.compass.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team.compass.user.dto.TokenDto;
import team.compass.user.domain.User;
import team.compass.user.dto.UserRequestDto;
import team.compass.user.service.UserService;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@RequestMapping("/api/member")
@RestController
public class UserController {
    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(
            @RequestBody UserRequestDto.SignUp parameter
    ) {
        User user = userService.signUp(parameter);

        if(ObjectUtils.isEmpty(user)) {
            return ResponseEntity.badRequest().body("회원가입 실패");
        }

        return ResponseEntity.ok().body("회원가입 완료");
    }

    @PostMapping("/sign-in")
    public ResponseEntity<?> signin(
            @RequestBody UserRequestDto.SignIn parameter
    ) {
        TokenDto tokenDto = userService.signIn(parameter);

        return ResponseEntity.ok().body(tokenDto);
    }


    @PostMapping("/logout")
    public ResponseEntity<?> logout(
            HttpServletRequest request
    ) {
        userService.logout(request);
        return ResponseEntity.ok().body("로그아웃 완료");
    }
}
