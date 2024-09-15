package busim.kkilogbu.api.restaurantAPI.domain.entity;

import busim.kkilogbu.place.dto.RestaurantCategory;
import busim.kkilogbu.place.entity.Place;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Restaurant {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id; //

        @Column(length = 10000)
        private String title; // 제목

        @Column(length = 30000)
        private String address; // 도로명 주소

        @Column(length = 30000)
        private String landLotAddress; // 지번 주소

        @Column(length = 30000)
        private Double latitude; // 위도

        @Column(length = 30000)
        private Double longitude; // 경도

        @Column(length = 30000)
        private String phoneNumber; // 전화번호

        @Column(length = 30000)
        private String businessType; // 업종 유형

        @Column(length = 30000)
        private String detailedInformation; // 상세 정보

        @ElementCollection
        @CollectionTable(name = "restaurant_images", joinColumns = @JoinColumn(name = "restaurant_id"))
        @Column(name = "image_url")
        private List<String> imageUrls; // 식당 이미지 URL 리스트

        private String restaurantName; // 식당 이름

        @ElementCollection
        @CollectionTable(name = "restaurant_categories", joinColumns = @JoinColumn(name = "restaurant_id"))
        @Column(name = "category")
        private List<String> categories; // 추천하는 회사 리스트 ( 추천 레스토랑 그룹 등)


        private String type; // 식당 유형을 나타내는 필드 (1: 특정 맛집, 0: 그냥 맛집)


}
