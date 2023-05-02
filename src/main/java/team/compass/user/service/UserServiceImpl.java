package team.compass.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import team.compass.common.config.JwtTokenProvider;
import team.compass.common.utils.MailUtils;
import team.compass.photo.repository.PhotoRepository;
import team.compass.photo.service.FileUploadService;
import team.compass.post.domain.Post;
import team.compass.post.dto.PhotoDto;
import team.compass.post.repository.PostRepository;
import team.compass.user.domain.RefreshToken;
import team.compass.user.domain.User;
import team.compass.user.dto.*;
import team.compass.user.repository.RefreshTokenRepository;
import team.compass.user.repository.UserRepository;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository memberRepository;
    private final PhotoRepository photoRepository;
    private final FileUploadService fileUploadService;
    private final PostRepository postRepository;


    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    private final MailUtils mailUtils;

    @Override
    @Transactional
    public User signUp(
            UserRequest.SignUp parameter,
            MultipartHttpServletRequest request
    ) {
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
    @Transactional
    public UserResponse signIn(UserRequest.SignIn parameter) {
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


        return UserResponse.to(user, accessToken);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public User updateUser(
            UserUpdate parameter,
            HttpServletRequest request,
            MultipartHttpServletRequest multipartHttpServletRequest
    ) {
        String accessToken = jwtTokenProvider.resolveToken(request);
        User user = findUserByAccessToken(accessToken);

        if(!passwordEncoder.matches(parameter.getPassword(), user.getPassword())) {
            throw new RuntimeException("현재 비밀번호가 틀립니다.");
        }

        if(!ObjectUtils.isEmpty(multipartHttpServletRequest.getFileMap())) {
            Map<String, MultipartFile> multipartFileMap = multipartHttpServletRequest.getFileMap();

            if(multipartFileMap.containsKey("profileImg")) {
                parameter.setUserProfileImgUrl(savePhotos(multipartFileMap.get("profileImg")));
            }

            if(multipartFileMap.containsKey("bannerImg")) {
                parameter.setUserBannerImgUrl(savePhotos(multipartFileMap.get("bannerImg")));
            }
        }

        String encodedPassword = passwordEncoder.encode(parameter.getPassword());

        User updateUser = User.builder()
                            .id(user.getId())
                            .email(user.getEmail())
                            .password(encodedPassword)
                            .nickName(user.getNickName())
                            .introduction(parameter.getIntroduction())
                            .userBannerImgUrl(parameter.getUserBannerImgUrl())
                            .profileImageUrl(parameter.getUserProfileImgUrl())
                            .emailAuthKey(user.getEmailAuthKey())
                            .resetPasswordKey(user.getResetPasswordKey())
                            .kakaoId(user.getKakaoId())
                            .loginType(user.getLoginType())
                            .roles(user.getRoles())
                            .build();
        memberRepository.save(updateUser);

        return updateUser;
    }

    @Override
    @Transactional
    public void logout(
            HttpServletRequest request
    ) {
        String accessToken = jwtTokenProvider.resolveToken(request);

        User user = findUserByAccessToken(accessToken);

        updateUserByTokenBlackList(accessToken);
    }

    @Override
    public void resetPassword(PasswordResetRequest parameter) {
        User user = memberRepository.findByEmail(parameter.getEmail())
                .orElseThrow(() -> new RuntimeException("해당 유저가 없습니다."));

        if(!parameter.getUuid().equals(user.getResetPasswordKey()) ) {
            throw new RuntimeException("비밀번호 초기화 코드 오류입니다.");
        }

        String password = passwordEncoder.encode(parameter.getPassword());
        user.setPassword(password);

        memberRepository.save(user);
    }

    @Override
    public void resetPasswordSendMsg(UserPasswordResetMailRequest parameter) {
        User user = memberRepository.findByEmail(parameter.getEmail())
                .orElseThrow(() -> new RuntimeException("해당 유저가 없습니다."));

        String uuid = UUID.randomUUID().toString();
        user.setResetPasswordKey(uuid);

        memberRepository.save(user);
        mailUtils.sendMessage(user);
    }

    @Override
    @Transactional
    public void withdraw(HttpServletRequest request) {
        String accessToken = jwtTokenProvider.resolveToken(request);

        User user = findUserByAccessToken(accessToken);

        updateUserByTokenBlackList(accessToken);

        memberRepository.delete(user);
    }



    @Override
    @Transactional
    public UserResponse updatePassword(PasswordInitRequest parameter, HttpServletRequest request) {
        String accessToken = jwtTokenProvider.resolveToken(request);

        User user = findUserByAccessToken(accessToken);

        if(!passwordEncoder.matches(parameter.getPassword(), user.getPassword())) {
            throw new RuntimeException("현재 비밀번호가 틀립니다.");
        }

        String encodedPassword = passwordEncoder.encode(parameter.getNewPassword());

        user.setPassword(encodedPassword);
        memberRepository.save(user);

        return UserResponse.to(user, accessToken);
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

    private User findUserByAccessToken(String accessToken) {
        String email = jwtTokenProvider.getMemberEmailByToken(accessToken);
        User user = memberRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("해당 유저 정보가 없습니다."));

        return user;
    }

    private void updateUserByTokenBlackList(String accessToken) {
        String email = jwtTokenProvider.getMemberEmailByToken(accessToken);

        RefreshToken refreshToken = refreshTokenRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("해당 유저 토큰 정보가 없습니다."));

        Long expiration = jwtTokenProvider.getExpiration(accessToken);

        refreshTokenRepository.setBlackList(accessToken, expiration);
        refreshTokenRepository.delete(refreshToken);
    }


    @Override
    public UserPostResponse getUserByPost(HttpServletRequest request, String type) {
        List<Post> postList = getPostList(request, type);

        return UserPostResponse.build(postList);
    }


    private List<Post> getPostList(HttpServletRequest request, String type) {
        List<Post> postList = null;
        String accessToken = jwtTokenProvider.resolveToken(request);

        Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);

        User user = memberRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("해당 유저가 없습니다."));

        if(type.equals("list")) {
            postList = postRepository.findAllByUser_Id(user.getId())
                    .orElse(null);
        } else if(type.equals("like")) {
            postList = postRepository.findAllByUserIdAndLikes(user.getId())
                    .orElse(null);
        }

        return postList;
    }



    /**
     * 사진 저장
     * @param img form-data img upload
     * @return img path 경로
     */
    private String savePhotos(MultipartFile img) {
        PhotoDto photoDto = fileUploadService.save(img);
        return photoDto.getStoreFileUrl();
    }
}
