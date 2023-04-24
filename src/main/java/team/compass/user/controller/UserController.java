package team.compass.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import team.compass.common.utils.ResponseUtils;
import team.compass.user.domain.User;
import team.compass.user.dto.*;
import team.compass.user.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RequiredArgsConstructor
@RequestMapping("/api/member")
@RestController
public class UserController {
    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(
            @RequestPart(name = "data") UserRequest.SignUp parameter,
            @RequestPart(name = "files", required = false) MultipartFile[] multiFiles,
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
        User user = userService.updateUser(parameter, request);

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
            HttpServletRequest request
    ) {
        userService.resetPasswordSendMsg(request);

        return ResponseUtils.ok("회원 비밀번호 초기화 메일 전송에 성공하였습니다.", true);
    }

    @PostMapping("/password/reset")
    public ResponseEntity<?> resetPassword(
            HttpServletRequest request,
            @RequestBody PasswordResetRequest parameter
    ) {
        userService.resetPassword(request, parameter);

        return ResponseUtils.ok("회원 비밀번호 초기화에 성공하였습니다.", true);
    }


    @GetMapping("/post/{type}")
    public ResponseEntity<?> getByUserPostList(
            @PathVariable String type,
            HttpServletRequest request
    ) {
        UserPostResponse response = null;

        if(type.equals("like")){
            response = userService.getUserByLikePost(request);
        }

        return ResponseUtils.ok("회원 좋아요 글 목록 조회에 성공했습니다.", response);
    }


    // 해당 유저 작성 글 조회
    @GetMapping("/post")
    public ResponseEntity<?> getUserByPost(HttpServletRequest request) {
        UserPostResponse postResponse = userService.getUserByPost(request);

        if(ObjectUtils.isEmpty(postResponse)) {
            return ResponseUtils.badRequest("작성 게시글 조회에 실패했습니다.");
        }
        return ResponseUtils.ok("해당 유저가 작성한 게시글 조회에 성공했습니다.", postResponse);
    }

    @GetMapping("/like-post")
    public ResponseEntity<?> getUserLikeByPost(HttpServletRequest request) {
        UserPostResponse postResponse = userService.getUserByLikePost(request);

        return ResponseUtils.ok("해당 유저가 좋아요한 게시글 조회에 성공했습니다.", postResponse);
    }
}
