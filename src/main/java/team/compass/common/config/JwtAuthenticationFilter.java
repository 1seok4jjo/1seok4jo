package team.compass.common.config;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";
    private final JwtTokenProvider jwtTokenProvider;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = resolveToken(request);

        if(StringUtils.hasText(token) && jwtTokenProvider.validateToken(token) == 1) {
            Authentication authentication = jwtTokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);

//        if(request.getServletPath().startsWith("/api/member")) {
//            filterChain.doFilter(request, response);
//        } else {
//            String token = resolveToken(request);
//
//            if(StringUtils.hasText(token)) {
//                int validToken = jwtTokenProvider.validateToken(token);
//
//                if(validToken == 1) {
//                    // 유효 토큰
//                    this.setAuthentication(token);
//                } else if(validToken == 2) {
//                    // 만료
//                    response.setContentType("application/json");
//                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
//                    response.setCharacterEncoding("UTF-8");
//                    PrintWriter out = response.getWriter();
//                    out.println("{\"error\": \"ACCESS_TOKEN_EXPIRED\", \"message\" : \"엑세스토큰이 만료되었습니다.\"}");
//                } else {
//                    response.setContentType("application/json");
//                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
//                    response.setCharacterEncoding("UTF-8");
//                    PrintWriter out = response.getWriter();
//                    out.println("{\"error\": \"BAD_TOKEN\", \"message\" : \"잘못된 토큰 값입니다.\"}");
//                }
//            } else {
//                response.setContentType("application/json");
//                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
//                response.setCharacterEncoding("UTF-8");
//                PrintWriter out = response.getWriter();
//                out.println("{\"error\": \"EMPTY_TOKEN\", \"message\" : \"토큰 값이 비어있습니다.\"}");
//            }
//        }

    }

    private void setAuthentication(String token) {
        Authentication authentication = jwtTokenProvider.getAuthentication(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private String resolveToken(HttpServletRequest request) {
        String token = request.getHeader(AUTHORIZATION_HEADER);

        if(StringUtils.hasText(token) && token.startsWith(BEARER_PREFIX)) {
            return token.substring(7);
        }

        return null;
    }

}
