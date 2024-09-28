package busim.kkilogbu.user.entity.interest;

import busim.kkilogbu.user.entity.UserInterest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Interest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 관광지 카테고리 (문자열 리스트로 저장)
    @Builder.Default
    @ElementCollection
    @CollectionTable(name = "interest_tourist_categories", joinColumns = @JoinColumn(name = "interest_id"))
    @Column(name = "tourist_category")
    private List<String> touristCategories = new ArrayList<>();

    // 맛집 카테고리 (문자열 리스트로 저장)
    @Builder.Default
    @ElementCollection
    @CollectionTable(name = "interest_restaurant_categories", joinColumns = @JoinColumn(name = "interest_id"))
    @Column(name = "restaurant_category")
    private List<String> restaurantCategories = new ArrayList<>();

    // UserInterest와의 일대다 관계 설정
    @Builder.Default
    @OneToMany(mappedBy = "interest", cascade = CascadeType.ALL)
    private List<UserInterest> userInterests = new ArrayList<>();

    // 기존 카테고리 리스트를 업데이트하는 메서드
    public Interest updateCategories(List<String> newTouristCategories, List<String> newRestaurantCategories) {
        // 관광지 카테고리가 null이 아닌 경우에만 업데이트
        if (newTouristCategories != null) {
            this.touristCategories = new ArrayList<>(newTouristCategories);
        }

        // 레스토랑 카테고리가 null이 아닌 경우에만 업데이트
        if (newRestaurantCategories != null) {
            this.restaurantCategories = new ArrayList<>(newRestaurantCategories);
        }

        return this;
    }
}
