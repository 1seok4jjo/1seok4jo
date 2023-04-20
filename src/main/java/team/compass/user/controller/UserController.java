package team.compass.user.controller;

import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.ResponseUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import team.compass.common.utils.ResponseUtils;
import team.compass.like.repository.LikeRepository;
import team.compass.like.service.LikeService;
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
    private final LikeService likeService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(
            @RequestPart(value = "data") UserRequest.SignUp parameter,
            MultipartHttpServletRequest request
            ) {
        Map<String, MultipartFile> multipartFileMap = request.getFileMap();
        User user = userService.signUp(parameter, multipartFileMap);

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
        User user = userService.updateUser(parameter, request);

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

    @DeleteMapping("/withdraw")
    public ResponseEntity<?> withdraw(
            HttpServletRequest request
    ){
        userService.withdraw(request);
        return ResponseUtils.ok("회원탈퇴를 완료하였습니다.", true);
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
}
