package busim.kkilogbu.api.tourInfoAPI.repository;

import busim.kkilogbu.api.tourInfoAPI.domain.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {


}
