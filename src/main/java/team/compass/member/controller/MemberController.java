package team.compass.member.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team.compass.member.dto.MemberRequestDto;
import team.compass.member.dto.TokenDto;
import team.compass.member.domain.User;
import team.compass.member.service.MemberService;

@RequiredArgsConstructor
@RequestMapping("/api/member")
@RestController
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(
            @RequestBody MemberRequestDto.SignUp parameter
    ) {
        User user = memberService.signUp(parameter);

        if(ObjectUtils.isEmpty(user)) {
            return ResponseEntity.badRequest().body("회원가입 실패");
        }

        return ResponseEntity.ok().body("회원가입 완료");
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signin(
            @RequestBody MemberRequestDto.SignIn parameter
    ) {
        TokenDto tokenDto = memberService.signIn(parameter);

        return ResponseEntity.ok().body(tokenDto);
    }
}
