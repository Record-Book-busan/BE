package busim.kkilogbu.user.repository;

import busim.kkilogbu.user.entity.UserInterest;
import busim.kkilogbu.user.entity.users.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserInterestRepository extends JpaRepository<UserInterest, Long> {

    List<UserInterest> findByUsers(Users users);
}
