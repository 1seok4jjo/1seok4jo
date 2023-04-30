package team.compass.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import team.compass.common.utils.ResponseUtils;
import team.compass.user.domain.User;
import team.compass.user.dto.*;
import team.compass.user.service.UserService;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@RequestMapping("/api/member")
@RestController
@CrossOrigin(origins = "*")
public class UserController {
    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(
            @RequestPart(name = "data") UserRequest.SignUp parameter,
            HttpServletRequest request
            ) {
        MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;

        User user = userService.signUp(parameter, multipartHttpServletRequest);

        if(ObjectUtils.isEmpty(user)) {
            return ResponseUtils.badRequest("회원가입에 실패하였습니다.");
        }

        return ResponseUtils.ok("회원가입을 완료하였습니다.", true);
    }

    @PostMapping("/sign-in")
    public ResponseEntity<?> signIn(
            @RequestBody UserRequest.SignIn parameter
    ) {
        UserResponse userResponse = userService.signIn(parameter);

        if(ObjectUtils.isEmpty(userResponse)) {
            return ResponseUtils.badRequest("로그인에 실패하였습니다.");
        }

        return ResponseEntity.ok().body(userResponse);
    }

    @PutMapping("/update")
    public ResponseEntity<?> update(
            @RequestBody UserUpdate parameter,
            HttpServletRequest request
    ) {
        MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;

        User user = userService.updateUser(parameter, request, multipartHttpServletRequest);

        if(ObjectUtils.isEmpty(user)) {
            return ResponseUtils.badRequest("해당 유저가 없습니다.");
        }

        UserResponse userResponse = UserResponse.to(user, "");

        return ResponseUtils.ok("회원정보 수정완료하였습니다.", userResponse);
    }


    @PostMapping("/logout")
    public ResponseEntity<?> logout(
            HttpServletRequest request
    ) {
        userService.logout(request);
        return ResponseUtils.ok("로그아웃 완료하였습니다.", true);
    }

    @DeleteMapping("/withdraw")
    public ResponseEntity<?> withdraw(
            HttpServletRequest request
    ){
        userService.withdraw(request);
        return ResponseUtils.ok("회원탈퇴를 완료하였습니다.", true);
    }

    @PutMapping("/password/update")
    public ResponseEntity<?> updatePassword(
            @RequestBody PasswordInitRequest parameter,
            HttpServletRequest request
    ) {
        UserResponse response = userService.updatePassword(parameter, request);

        if(!ObjectUtils.isEmpty(response)) {
            return ResponseUtils.ok("패스워드 변경에 성공했습니다.", response);
        } else {
            return ResponseUtils.badRequest("패스워드 변경에 실패하였습니다.");
        }
    }

    @PostMapping("/password/send")
    public ResponseEntity<?> resetPasswordSendMsg(
            @RequestBody UserPasswordResetMailRequest parameter
    ) {
        userService.resetPasswordSendMsg(parameter);

        return ResponseUtils.ok("회원 비밀번호 초기화 메일 전송에 성공하였습니다.", true);
    }

    @PostMapping("/password/reset")
    public ResponseEntity<?> resetPassword(
            @RequestBody PasswordResetRequest parameter
    ) {
        userService.resetPassword(parameter);

        return ResponseUtils.ok("회원 비밀번호 초기화에 성공하였습니다.", true);
    }


    @GetMapping("/post/{type}")
    public ResponseEntity<?> getByUserPostList(
            @PathVariable String type,
            @RequestBody UserPostRequest parameter,
            HttpServletRequest request
    ) {
        UserPostResponse response = userService.getUserByPost(request, type, parameter);

        return ResponseUtils.ok("회원 좋아요 글 목록 조회에 성공했습니다.", response);
    }

}
