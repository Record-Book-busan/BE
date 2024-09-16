package busim.kkilogbu.security.domain;

import busim.kkilogbu.user.entity.users.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;


@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {

    private final User user;


    @Override
    public boolean isAccountNonExpired() {
        // 계정이 만료되지 않았다고 가정 (true 반환)
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // 계정이 잠겨있지 않다고 가정 (true 반환)
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // 자격 증명이 만료되지 않았다고 가정 (true 반환)
        return true;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // User의 role을 GrantedAuthority로 변환하여 반환
        return List.of(new SimpleGrantedAuthority(user.getRole()));
    }

    @Override
    public String getPassword() {
        // 소셜 로그인 사용자일 경우 비밀번호가 없으므로 null 반환 가능
        return null;
    }

    @Override
    public String getUsername() {
        // email을 username으로 사용
        return user.getEmail();
    }
}
