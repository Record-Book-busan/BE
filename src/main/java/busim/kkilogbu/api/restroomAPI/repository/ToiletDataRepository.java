package busim.kkilogbu.api.restroomAPI.repository;

import busim.kkilogbu.api.restroomAPI.domain.entity.ToiletData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ToiletDataRepository extends JpaRepository<ToiletData, Long> {


    @Query(value = "SELECT *, (6371 * acos(cos(radians(:latitude)) * cos(radians(latitude)) * cos(radians(longitude) - radians(:longitude)) + sin(radians(:latitude)) * sin(radians(latitude)))) AS distance " +
            "FROM toilets " +
            "HAVING distance <= 1 " +
            "ORDER BY distance", nativeQuery = true)
    List<ToiletData> findToiletsWithinRadius(@Param("latitude") double latitude, @Param("longitude") double longitude);
}
