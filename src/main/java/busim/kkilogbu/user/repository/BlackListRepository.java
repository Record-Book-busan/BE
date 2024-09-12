package busim.kkilogbu.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import busim.kkilogbu.user.entity.BlackList;
import busim.kkilogbu.user.entity.users.User;

@Repository
public interface BlackListRepository extends JpaRepository<BlackList, Long> {
	boolean existsByUserAndReportedUser(User listener, User speaker);
}
