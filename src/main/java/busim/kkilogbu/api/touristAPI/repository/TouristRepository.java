package busim.kkilogbu.api.touristAPI.repository;

import busim.kkilogbu.api.touristAPI.domain.entity.Tourist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TouristRepository extends JpaRepository<Tourist, Long> {
}
