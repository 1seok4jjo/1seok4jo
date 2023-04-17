package team.compass.user.controller;

import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.ResponseUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import team.compass.common.utils.ResponseUtils;
import team.compass.user.domain.User;
import team.compass.user.dto.TokenDto;
import team.compass.user.dto.UserRequest;
import team.compass.user.dto.UserResponse;
import team.compass.user.dto.UserUpdate;
import team.compass.user.service.UserService;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@RequestMapping("/api/member")
@RestController
public class UserController {
    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(
            @RequestBody UserRequest.SignUp parameter
    ) {
        User user = userService.signUp(parameter);

        if(ObjectUtils.isEmpty(user)) {
            return ResponseUtils.badRequest("회원가입에 실패하였습니다.");
        }

        return ResponseUtils.ok("회원가입을 완료하였습니다.", true);
    }

    @PostMapping("/sign-in")
    public ResponseEntity<?> signIn(
            @RequestBody UserRequest.SignIn parameter
    ) {
        TokenDto tokenDto = userService.signIn(parameter);

        if(ObjectUtils.isEmpty(tokenDto)) {
            return ResponseUtils.badRequest("로그인에 실패하였습니다.");
        }

        return ResponseUtils.ok("로그인 성공하였습니다.", tokenDto);
    }

    @PutMapping("/update")
    public ResponseEntity<?> update(
            @RequestBody UserUpdate parameter,
            HttpServletRequest request
    ) {
        User user = userService.updateUserInfo(parameter, request);

        if(ObjectUtils.isEmpty(user)) {
            return ResponseUtils.badRequest("해당 유저가 없습니다.");
        }

        UserResponse userResponse = UserResponse.to(user);

        return ResponseUtils.ok("회원정보 수정완료하였습니다.", userResponse);
    }


    @PostMapping("/logout")
    public ResponseEntity<?> logout(
            HttpServletRequest request
    ) {
        userService.logout(request);
        return ResponseUtils.ok("로그아웃 완료하였습니다.", true);
    }
}
