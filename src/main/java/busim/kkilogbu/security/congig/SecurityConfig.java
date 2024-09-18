package busim.kkilogbu.security.congig;

import busim.kkilogbu.security.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)  // CSRF 비활성화 (JWT 사용)
                .authorizeHttpRequests(auth -> auth
                        // 로그인 경로는 인증 없이 접근 가능
                        .requestMatchers("/kkilogbu/user/signin/**").permitAll()
                        // Swagger 및 정적 리소스 경로 접근 허용
                        .requestMatchers("/swagger-ui/**", "/api-docs/**", "/static/**", "/css/**", "/image/**", "/favicon.ico/**").permitAll()
                        // 회원 전용 경로 설정
                        .requestMatchers("/kkilogbu/record/auth/**").hasAuthority("USER")
                        .requestMatchers("/kkilogbu/**").hasAuthority("GUEST")
                       // .requestMatchers("/kkilogbu/**").hasAuthority("USER")
                        // 그 외 나머지 경로는 인증 필요
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))  // 세션을 사용하지 않음 (JWT 사용)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);  // JWT 필터 추가

        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .requestMatchers("/error/**", "/favicon.ico", "/swagger-ui/**", "/static/**", "/css/**", "/images/**", "/favicon.ico/**");
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
