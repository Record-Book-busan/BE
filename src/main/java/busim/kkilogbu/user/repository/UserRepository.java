package busim.kkilogbu.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import busim.kkilogbu.user.entity.users.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findById(Long id);
    Boolean existsByNickname(String nickname);

    void deleteById(Long userId);

    Optional<User> findBySocialUserId(String socialUserId);

    Optional<User> findByEmail(String email);




}
