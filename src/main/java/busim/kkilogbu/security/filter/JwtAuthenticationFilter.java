package busim.kkilogbu.security.filter;


import busim.kkilogbu.security.util.JwtUtil;
import busim.kkilogbu.user.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;



    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        // 요청의 Authorization 헤더에서 JWT 추출
        String authorizationHeader = request.getHeader("Authorization");
        String token = null;
        String email = null;

        // Authorization 헤더가 존재하고 "Bearer "로 시작하는 경우 토큰 추출
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7);  // "Bearer " 이후의 토큰 추출
            // role 확인 (GUEST인지 USER인지)
            String role = jwtUtil.extractRole(token); // JWT에서 role 추출

            // GUEST 역할일 경우, 필터를 통과하되 인증 설정을 하지 않음
            if ("GUEST".equals(role)) {
                chain.doFilter(request, response);
                return;
            }

            // JWT가 GUEST가 아닌 경우, 사용자 정보 처리
            email = jwtUtil.extractEmail(token);
        }

        // JWT가 존재하고 SecurityContext에 인증 정보가 없는 경우
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // 데이터베이스 또는 기타 방법으로 사용자 정보 로드
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);

            // JWT 토큰이 유효한지 확인
            if (jwtUtil.validateToken(token, false)) {  // Access Token으로 유효성 검증
                // 사용자 인증 정보를 생성하고 SecurityContext에 저장
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // SecurityContextHolder에 인증 정보 설정
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        // 다음 필터로 요청을 넘김
        chain.doFilter(request, response);
    }


}
