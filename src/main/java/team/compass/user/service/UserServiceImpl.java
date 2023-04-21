package team.compass.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import team.compass.common.config.JwtTokenProvider;
import team.compass.post.controller.response.PostResponse;
import team.compass.post.domain.Post;
import team.compass.post.repository.PostRepository;
import team.compass.user.domain.RefreshToken;
import team.compass.user.domain.User;
import team.compass.user.dto.*;
import team.compass.user.repository.RefreshTokenRepository;
import team.compass.user.repository.UserRepository;

import javax.servlet.http.HttpServletRequest;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository memberRepository;
    private final PostRepository postRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public User signUp(UserRequest.SignUp parameter) {
        boolean existsByEmail = memberRepository.existsByEmail(parameter.getEmail());

        if(existsByEmail) {
            throw new RuntimeException("회원이 존재합니다.");
        }

        parameter.setPassword(passwordEncoder.encode(parameter.getPassword()));
        parameter.setLoginType(UserSignUpType.NORMAL.getSignUpType());

        User user = memberRepository.save(UserRequest.SignUp.toEntity(parameter));

        return user;
    }

    @Override
    public TokenDto signIn(UserRequest.SignIn parameter) {
        User user = memberRepository.findByEmail(parameter.getEmail())
                .orElseThrow(() -> new RuntimeException("해당 회원이 존재하지 않습니다."));

        if(!passwordEncoder.matches(parameter.getPassword(), user.getPassword())) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }

        String accessToken =  jwtTokenProvider.createAccessToken(user.getEmail(), user.getRoles());
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getEmail(), user.getRoles());


        refreshTokenRepository.save(
                RefreshToken.builder()
                        .email(user.getEmail())
                        .refreshToken(refreshToken)
                        .build()
        );

        return jwtTokenProvider.createTokenDto(accessToken, refreshToken);
    }

    @Override
    public User updateUserInfo(UserUpdate parameter, HttpServletRequest request) {
        String accessToken = jwtTokenProvider.resolveToken(request);

        if(!StringUtils.hasText(accessToken)) {
            throw new RuntimeException("토큰 정보가 없습니다.");
        }

        Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);

        User user = memberRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("로그인 유저 정보가 없습니다."));

        String encodedPassword = passwordEncoder.encode(parameter.getPassword());

        User updateUser = User.builder()
                            .id(user.getId())
                            .email(user.getEmail())
                            .password(encodedPassword)
                            .build();

        memberRepository.save(updateUser);

        return updateUser;
    }

    @Override
    public void logout(
            HttpServletRequest request
    ) {
        String accessToken = jwtTokenProvider.resolveToken(request);

        Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);

        RefreshToken token = refreshTokenRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("해당 유저 토큰 정보가 없습니다."));

        refreshTokenRepository.delete(token);
        Long expiration = jwtTokenProvider.getExpiration(accessToken);

        refreshTokenRepository.setBlackList(accessToken, expiration);
    }

    @Override
    public void withdraw(HttpServletRequest request) {
        String accessToken = jwtTokenProvider.resolveToken(request);

        Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);

        RefreshToken refreshToken = refreshTokenRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("해당 유저 토큰 정보가 없습니다."));

        refreshTokenRepository.delete(refreshToken);

        Long expiration = jwtTokenProvider.getExpiration(accessToken);

        refreshTokenRepository.setBlackList(accessToken, expiration);

        User user = memberRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("해당 유저 정보가 없습니다."));

        memberRepository.delete(user);
    }


    @Transactional
    @Override
    public TokenDto reissue(TokenDto tokenRequestDto) {
        String originAccessToken = tokenRequestDto.getAccessToken();
        String originRefreshToken = tokenRequestDto.getRefreshToken();

        // refresh token validate
        int refreshTokenValid = jwtTokenProvider.validateToken(originRefreshToken);

        if(refreshTokenValid == -1) {
            throw new RuntimeException("토큰이 잘못되었습니다.");
        } else if(refreshTokenValid == 2) {
            throw new RuntimeException("만료된 토큰입니다.");
        }

        Authentication authentication = jwtTokenProvider.getAuthentication(originAccessToken);

        RefreshToken refreshToken = refreshTokenRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("해당 유저 토큰 정보가 없습니다."));

        // refresh token 검사
        if(!refreshToken.getRefreshToken().equals(originRefreshToken)) {
            throw new RuntimeException("토큰이 일치하지 않습니다.");
        }

        // 새로운 토큰 생성
        String email = jwtTokenProvider.getMemberEmailByToken(originAccessToken);

        User user = memberRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("해당 유저 정보가 없습니다."));

        String newAccessToken = jwtTokenProvider.createAccessToken(email, user.getRoles());
        String newRefreshToken = jwtTokenProvider.createRefreshToken(email, user.getRoles());

        TokenDto tokenDto = jwtTokenProvider.createTokenDto(newAccessToken, newRefreshToken);

        refreshTokenRepository.save(
                RefreshToken.builder()
                        .email(user.getEmail())
                        .refreshToken(newRefreshToken)
                        .build()
        );

        return tokenDto;
    }


    @Override
    public UserPostResponse getUserByPost(HttpServletRequest request) {
        Page<Post> postList = getPostList(request);
        return UserPostResponse.builder()
                .count(postList.getTotalElements())
                .userPostList(
                        postList.stream().map(item ->
                            PostResponse.builder()
                                    .id(item.getId())
                                    .title(item.getTitle())
                                    .detail(item.getDetail())
                                    .hashtag(item.getHashtag())
                                    .location(item.getLocation())
                                    .createdAt(item.getCreatedAt())
                                    .build()
                        ).collect(Collectors.toList())
                )
                .build();
    }


    @Override
    public UserPostResponse getUserLikeByPost(HttpServletRequest request) {
        Page<Post> postList = getLikePostList(request);

        return UserPostResponse.builder()
                .count(postList.getTotalElements())
                .userPostList(
                        postList.stream().map(item ->
                                PostResponse.builder()
                                        .id(item.getId())
                                        .title(item.getTitle())
                                        .detail(item.getDetail())
                                        .hashtag(item.getHashtag())
                                        .location(item.getLocation())
                                        .createdAt(item.getCreatedAt())
                                        .build()
                        ).collect(Collectors.toList())
                )
                .build();
    }

    private Page<Post> getPostList(HttpServletRequest request) {
        Pageable pageable = PageRequest.of(0, 10);

        String accessToken = jwtTokenProvider.resolveToken(request);


        Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);

        User user = memberRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("해당 유저가 없습니다."));

        Page<Post> postPage = postRepository.findAllByUser_Id(user.getId(), pageable)
                .orElse(null);

        return postPage;
    }

    private Page<Post> getLikePostList(HttpServletRequest request) {
        Pageable pageable = PageRequest.of(0, 10);

        String accessToken = jwtTokenProvider.resolveToken(request);
        Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);

        User user = memberRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("해당 유저가 없습니다."));

        Page<Post> postPage = postRepository.findAllByUser_IdAndLikes(user.getId(), pageable)
                .orElse(null);

        return postPage;
    }



}
