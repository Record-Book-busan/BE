package busim.kkilogbu.api.touristAPI.repository;

import busim.kkilogbu.api.touristAPI.domain.entity.Tourist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TouristRepository extends JpaRepository<Tourist, Long> {


    // 장소명, 중분류, 소분류로 검색 (OR 조건) + 페이징 처리
    @Query("SELECT t FROM Tourist t WHERE " +
            "LOWER(t.name) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(t.categoryMedium) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(t.categorySmall) LIKE LOWER(CONCAT('%', :query, '%'))")
    Page<Tourist> findByMultipleFields(@Param("query") String query, Pageable pageable);

    @Query("SELECT t FROM Tourist t WHERE " +
            "LOWER(t.categoryLarge) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(t.categoryMedium) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(t.categorySmall) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(t.name) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(t.location) LIKE LOWER(CONCAT('%', :query, '%'))")
    Page<Tourist> findByMultiplesFields(@Param("query") String query, Pageable pageable);


}
