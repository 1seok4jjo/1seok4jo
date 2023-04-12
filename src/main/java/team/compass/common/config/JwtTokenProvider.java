package team.compass.common.config;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import team.compass.member.dto.TokenDto;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {

    private final long ACCESS_TOKEN_EXPIRE_TIME;
    private final long REFRESH_TOKEN_EXPIRE_TIME;
    private static final String BEARER_TYPE = "bearer";
    private static final String KEY_ROLES = "auth";

    private final Key key;

    // 키 암호화
    public JwtTokenProvider(
            @Value("${jwt.secret}") String secretKey,
            @Value("${jwt.access-token-expire-time}") long accessTime,
            @Value("${jwt.refresh-token-expire-time}") long refreshTime
    ) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.ACCESS_TOKEN_EXPIRE_TIME = accessTime;
        this.REFRESH_TOKEN_EXPIRE_TIME = refreshTime;
    }

    // 토큰 생성

    /**
     * @param username (email)
     * @param roles
     */
    public String generateToken(String username, List<String> roles, long tokenValidTime) {
        Claims claims = Jwts.claims().setSubject(username);
        claims.put(KEY_ROLES, roles);

        Date now = new Date();
        Date expiredDate = new Date(now.getTime() + tokenValidTime);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiredDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * @param username (email)
     * @param roles
     * @return access-token 생성
     */
    public String createAccessToken(String username, List<String> roles) {
        return this.generateToken(username, roles, ACCESS_TOKEN_EXPIRE_TIME);
    }

    /**
     * @param username (email)
     * @param roles
     * @return refresh-token 생성
     */
    public String createRefreshToken(String username, List<String> roles) {
        return this.generateToken(username, roles, REFRESH_TOKEN_EXPIRE_TIME);
    }

    /**
     *
     * @param token
     * @return token 정보에 있는 email 값 리턴
     */
    public String getMemberEmailByToken(String token) {
        return this.parseClaims(token).getSubject();
    }

    public TokenDto createTokenDto(String accessToken, String refreshToken) {
        return TokenDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .grantType(BEARER_TYPE)
                .build();
    }

    public Authentication getAuthentication(String token) {
        Claims claims = parseClaims(token);

        if(claims.get(KEY_ROLES) == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(KEY_ROLES).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());
        UserDetails principal = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(
                principal,
                "",
                authorities
        );
    }

    public int validateToken(String token) {
        if(!StringUtils.hasText(token)) return -1;

        try {
            Claims claims = parseClaims(token);
            return 1;
        } catch (ExpiredJwtException e){
//            throw new RuntimeException("만료된 토큰입니다.");
            return 2;
        } catch (UnsupportedJwtException e){
//            throw new RuntimeException("토큰이 잘못되었습니다.");
            return -1;
        }
    }

    private Claims parseClaims(String token) {
        return Jwts.parser().setSigningKey(key)
                .parseClaimsJws(token).getBody();
    }
}
