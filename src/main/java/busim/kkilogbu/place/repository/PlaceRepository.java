package busim.kkilogbu.place.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import busim.kkilogbu.place.entity.Place;

@Repository
public interface PlaceRepository extends JpaRepository<Place, Long> {
}
