package busim.kkilogbu.place.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import busim.kkilogbu.place.entity.Place;

import java.util.List;
import java.util.function.LongFunction;

@Repository
public interface PlaceRepository extends JpaRepository<Place, Long> {



}
