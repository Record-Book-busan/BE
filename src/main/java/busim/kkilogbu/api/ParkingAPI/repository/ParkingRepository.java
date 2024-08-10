package busim.kkilogbu.api.ParkingAPI.repository;

import busim.kkilogbu.api.ParkingAPI.domain.entity.ParkingData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParkingRepository extends JpaRepository<ParkingData, Long> {
}
