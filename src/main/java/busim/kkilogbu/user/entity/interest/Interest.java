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
}
