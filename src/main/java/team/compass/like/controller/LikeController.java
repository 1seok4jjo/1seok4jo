package team.compass.like.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import team.compass.common.config.JwtTokenProvider;
import team.compass.common.utils.ResponseUtils;
import team.compass.like.service.LikeService;
import team.compass.user.domain.User;
import team.compass.user.repository.UserRepository;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    private final UserRepository userRepository;

    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    @PostMapping("/like/{postId}")
    public ResponseEntity<Object> addLike(
            HttpServletRequest request,
            @PathVariable Integer postId) {
        User user = validationGetUser(request);
        likeService.addLike(user.getId(), postId);
        return ResponseUtils.ok("좋아요 등록 성공", "ok");
    }

    @Transactional
    @DeleteMapping("/like/{postId}")
    public ResponseEntity<Object> cancelLike(
            HttpServletRequest request,
            @PathVariable Integer postId) {
        User user = validationGetUser(request);
        if (user != null) {
            likeService.cancelLikes(user, postId);
        }

        return ResponseUtils.ok("좋아요 취소 성공", "ok");

    }


    private User validationGetUser(HttpServletRequest request) {
        String accessToken = jwtTokenProvider.resolveToken(request);
        Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);
        String userEmail = authentication.getName();
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("유저 없음"));
        return user;
    }
}