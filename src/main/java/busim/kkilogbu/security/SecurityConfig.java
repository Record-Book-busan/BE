package busim.kkilogbu.security;

import busim.kkilogbu.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity(debug = false)
@RequiredArgsConstructor
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(AbstractHttpConfigurer::disable) // CSRF 보호 비활성화
                .formLogin(AbstractHttpConfigurer::disable) // 기본 폼 로그인 비활성화
                .httpBasic(AbstractHttpConfigurer::disable) // 기본 HTTP Basic 인증 비활성화
                .cors(AbstractHttpConfigurer::disable) // CORS 비활성화
                .logout(AbstractHttpConfigurer::disable) // 기본 로그아웃 비활성화
                .headers(c -> c.frameOptions(
                        HeadersConfigurer.FrameOptionsConfig::disable).disable()) // X-Frame-Options 비활성화 (예: H2 콘솔용)
                .sessionManagement(c ->
                        c.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 세션을 사용하지 않음

                .authorizeHttpRequests(authorize -> authorize
                        // 특정 요청 경로를 허용
                        .requestMatchers(
                                "/auth/**", // 인증 관련 경로 허용
                                "/swagger-ui/**", // Swagger UI 허용
                                "/api-docs/**", // API 문서 허용
                                "/error/**" // 에러 페이지 허용
                        ).permitAll()
                        .anyRequest().authenticated() // 나머지 요청은 인증 필요
                );
              //  .exceptionHandling(exception -> exception
                       // .authenticationEntryPoint(jwtAuthenticationEntryPoint) // JWT 인증 실패 시 처리
            //    );

      //  http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class); // JWT 필터 추가
        return http.build();
    }
}