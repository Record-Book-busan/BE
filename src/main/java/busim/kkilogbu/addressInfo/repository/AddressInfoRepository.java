package busim.kkilogbu.addressInfo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import busim.kkilogbu.addressInfo.entity.AddressInfo;

@Repository
public interface AddressInfoRepository extends JpaRepository<AddressInfo, Long> {
	Optional<AddressInfo> findByLatitudeAndLongitude(double latitude, double longitude);
	List<AddressInfo> findByLatitudeBetweenAndLongitudeBetween(double minLat, double maxLat, double minLng, double maxLng, Pageable pageable);

}
