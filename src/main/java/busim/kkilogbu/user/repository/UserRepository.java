package busim.kkilogbu.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import busim.kkilogbu.user.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
