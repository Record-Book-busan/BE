package busim.kkilogbu.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import busim.kkilogbu.user.entity.users.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findById(Long id);
    Boolean existsByNickname(String nickname);
    Optional<User> findByUsername(String username);

    void deleteById(Long userId);

    // 애플 사용자 ID로 사용자 조회
    Optional<User> findByAppleUserId(String appleUserId);
    Optional<User> findByPhoneIdentificationNumber(String phoneIdentificationNumber);



}
