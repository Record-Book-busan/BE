package busim.kkilogbu.api.tourInfoAPI.domain.entity;

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
        private Long id;

        private String title;
        private String address;
        private String landLotAddress;
        private Double latitude;
        private Double longitude;
        private String phoneNumber;
        private String businessType;
        private String businessLicenseName;
        private String detailedInformation;

        @ElementCollection
        @CollectionTable(name = "restaurant_images", joinColumns = @JoinColumn(name = "restaurant_id"))
        @Column(name = "image_url")
        private List<String> imageUrls;

        private String restaurantName;

        private String category;


}
