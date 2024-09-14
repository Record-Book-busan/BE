package busim.kkilogbu.user.repository;

import busim.kkilogbu.user.entity.users.SocialLoginInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SocialLoginInfoRepository extends JpaRepository<SocialLoginInfo, Long> {
}
