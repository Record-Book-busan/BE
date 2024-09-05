package busim.kkilogbu.api.restaurantAPI.repository;

import busim.kkilogbu.api.restaurantAPI.domain.entity.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    // 장소명, 상세 정보, 업종 유형으로 검색 (OR 조건) + 페이징 처리
    @Query("SELECT r FROM Restaurant r WHERE " +
            "LOWER(r.name) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(r.detailedInformation) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(r.businessType) LIKE LOWER(CONCAT('%', :query, '%'))")
    Page<Restaurant> findByMultipleFields(@Param("query") String query, Pageable pageable);

}
