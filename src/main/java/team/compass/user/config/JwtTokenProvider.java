//package team.compass.user.config;
//
//import io.jsonwebtoken.*;
//import io.jsonwebtoken.io.Decoders;
//import io.jsonwebtoken.security.Keys;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Component;
//import org.springframework.util.StringUtils;
//
//import java.security.Key;
//import java.util.Arrays;
//import java.util.Collection;
//import java.util.Date;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Component
//public class JwtTokenProvider {
//
//    private static final long TOKEN_EXPIRE_TIME = 1000 * 60 * 60;
//    private static final String BEARER_TYPE = "bearer";
//    private static final String KEY_ROLES = "roles";
//
//    private final Key key;
//
//    // 키 암호화
//    public JwtTokenProvider(
//            @Value("${jwt.secret}") String secretKey
//    ) {
//        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
//        this.key = Keys.hmacShaKeyFor(keyBytes);
//    }
//
//    // 토큰 생성
//    public String generateToken(String username, List<String> roles) {
//        Claims claims = Jwts.claims().setSubject(username);
//        claims.put(KEY_ROLES, roles);
//
//        Date now = new Date();
//        Date expiredDate = new Date(now.getTime() + TOKEN_EXPIRE_TIME);
//
//        return Jwts.builder()
//                .setClaims(claims)
//                .setIssuedAt(now)
//                .setExpiration(expiredDate)
//                .signWith(key, SignatureAlgorithm.HS512)
//                .compact();
//    }
//
//    public Authentication getAuthentication(String token) {
//        Claims claims = parseClaims(token);
//
//        if(claims.get(KEY_ROLES) == null) {
//            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
//        }
//
//        Collection<? extends GrantedAuthority> authorities =
//                Arrays.stream(claims.get(KEY_ROLES).toString().split(","))
//                        .map(SimpleGrantedAuthority::new)
//                        .collect(Collectors.toList());
//        UserDetails principal = new User(claims.getSubject(), "", authorities);
//
//        return new UsernamePasswordAuthenticationToken(
//                principal,
//                "",
//                authorities
//        );
//    }
//
//    public boolean validateToken(String token) {
//        if(!StringUtils.hasText(token)) return false;
//
//        try {
//            Claims claims = parseClaims(token);
//            return !claims.getExpiration().before(new Date());
//        } catch (ExpiredJwtException e){
////            throw new RuntimeException("만료된 토큰입니다.");
//        } catch (UnsupportedJwtException e){
////            throw new RuntimeException("토큰이 잘못되었습니다.");
//        }
//
//        return false;
//    }
//
//    private Claims parseClaims(String token) {
//        return Jwts.parser().setSigningKey(key)
//                .parseClaimsJws(token).getBody();
//    }
//}
