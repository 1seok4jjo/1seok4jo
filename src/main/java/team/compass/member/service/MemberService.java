package team.compass.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.compass.member.config.JwtTokenProvider;
import team.compass.member.domain.User;
import team.compass.member.dto.MemberRequestDto;
import team.compass.member.dto.TokenDto;
import team.compass.member.domain.RefreshToken;
import team.compass.member.repository.MemberRepository;
import team.compass.member.repository.RefreshTokenRepository;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

//    @Transactional
    public User signUp(MemberRequestDto.SignUp parameter) {
        boolean existsByEmail = memberRepository.existsByEmail(parameter.getEmail());

        if(existsByEmail) {
            throw new RuntimeException("회원이 존재합니다.");
        }

        parameter.setPassword(passwordEncoder.encode(parameter.getPassword()));

        User user = memberRepository.save(MemberRequestDto.SignUp.toEntity(parameter));

        return user;
    }

//    @Transactional
//    public User signIn(MemberRequestDto.SignIn parameter) {
    public TokenDto signIn(MemberRequestDto.SignIn parameter) {
        User user = memberRepository.findByEmail(parameter.getEmail())
                .orElseThrow(() -> new RuntimeException("해당 회원이 존재하지 않습니다."));

        if(!passwordEncoder.matches(parameter.getPassword(), user.getPassword())) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }

        String accessToken =  jwtTokenProvider.createAccessToken(user.getEmail(), user.getRoles());
        String refreshToken = jwtTokenProvider.createAccessToken(user.getEmail(), user.getRoles());


        refreshTokenRepository.save(
                RefreshToken.builder()
                        .email(user.getEmail())
                        .refreshToken(refreshToken)
                        .build()
        );

        return jwtTokenProvider.createTokenDto(accessToken, refreshToken);
    }


    @Transactional
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

        refreshToken.updateValue(newRefreshToken);

        return tokenDto;
    }
}
