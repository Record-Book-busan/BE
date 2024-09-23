package busim.kkilogbu.user.entity;

import busim.kkilogbu.user.entity.users.Users;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserConsent extends TimeBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "users_id", nullable = false)
    private Users users;

    private boolean termsAgreed;      // 이용약관 동의 여부
    private boolean privacyAgreed;    // 개인정보 처리 동의 여
}
