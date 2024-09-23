package busim.kkilogbu.user.repository;

import busim.kkilogbu.user.entity.users.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {

    Optional<Users> findById(Long id);
    Boolean existsByNickname(String nickname);

    void deleteById(Long userId);

    Optional<Users> findBySocialUserId(String socialUserId);

    Optional<Users> findByEmail(String email);




}
