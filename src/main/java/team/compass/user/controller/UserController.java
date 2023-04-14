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

    @PostMapping("/signin")
    public ResponseEntity<?> signin(
            @RequestBody UserRequestDto.SignIn parameter
    ) {
        TokenDto tokenDto = userService.signIn(parameter);

        return ResponseEntity.ok().body(tokenDto);
    }
}
