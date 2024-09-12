package busim.kkilogbu.user.repository;

import busim.kkilogbu.user.entity.interest.Interest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InterestRepository extends JpaRepository<Interest, Long> {
}
