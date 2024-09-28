package busim.kkilogbu.user.service;


import busim.kkilogbu.user.entity.interest.Interest;
import busim.kkilogbu.user.entity.users.Users;
import busim.kkilogbu.user.entity.UserInterest;
import busim.kkilogbu.user.repository.InterestRepository;
import busim.kkilogbu.user.repository.UserInterestRepository;
import busim.kkilogbu.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class InterestService {

    private final UserRepository userRepository;
    private final InterestRepository interestRepository;
    private final UserInterestRepository userInterestRepository;

    @Transactional
    public void saveUserInterests(String socialUserId, List<String> touristCategoryNames, List<String> restaurantCategoryNames) {
        log.info("유저의 관심사를 저장하거나 업데이트합니다. 유저 ID: {}", socialUserId);

        // 유저 조회
        Users users = userRepository.findBySocialUserId(socialUserId)
                .orElseThrow(() -> {
                    log.error("유저를 찾을 수 없습니다. 유저 ID: {}", socialUserId);
                    return new EntityNotFoundException("회원 가입 안되었습니다.");
                });

        // 유저의 기존 관심사 조회
        List<UserInterest> existingUserInterests = userInterestRepository.findByUsers(users);

        List<String> touristCategories = touristCategoryNames != null ? new ArrayList<>(touristCategoryNames) : new ArrayList<>();
        List<String> restaurantCategories = restaurantCategoryNames != null ? new ArrayList<>(restaurantCategoryNames) : new ArrayList<>();

        log.debug("관광 카테고리 리스트: {}", touristCategories);
        log.debug("레스토랑 카테고리 리스트: {}", restaurantCategories);

        if (existingUserInterests != null && !existingUserInterests.isEmpty()) {
            // 기존 관심사가 있으면 각 관심사를 업데이트
            for (UserInterest userInterest : existingUserInterests) {
                Interest existingInterest = userInterest.getInterest();

                // 동일하지 않은 경우에만 업데이트
                if (!existingInterest.getTouristCategories().equals(touristCategories) ||
                        !existingInterest.getRestaurantCategories().equals(restaurantCategories)) {
                    existingInterest.updateCategories(touristCategories, restaurantCategories);
                    interestRepository.save(existingInterest);  // 업데이트된 Interest 저장
                    log.info("기존 Interest가 업데이트되었습니다. Interest ID: {}", existingInterest.getId());
                }
            }
        } else {
            // 관심사가 없으면 새로 생성
            Interest newInterest = Interest.builder()
                    .touristCategories(touristCategories)
                    .restaurantCategories(restaurantCategories)
                    .build();

            interestRepository.save(newInterest);
            log.info("새로운 Interest가 생성되었습니다. Interest ID: {}", newInterest.getId());

            // 새로운 UserInterest를 생성하여 연결
            UserInterest newUserInterest = new UserInterest(users, newInterest);
            userInterestRepository.save(newUserInterest);  // 새로운 UserInterest 저장
            log.info("새로운 UserInterest가 생성되었습니다. 유저 ID: {}, Interest ID: {}", users.getId(), newInterest.getId());
        }
    }


    @Transactional(readOnly = true)
    public Map<String, Object> getUserInterests(String socialUserId) {
        // 1. 유저 조회
        Users users = userRepository.findBySocialUserId(socialUserId)
                .orElseThrow(() -> new EntityNotFoundException("회원 가입 안되었습니다."));

        // 2. 관광지 및 맛집 카테고리 리스트 반환
        List<String> touristCategories = users.getUserInterests().stream()
                .flatMap(userInterest -> userInterest.getInterest().getTouristCategories().stream())
                .collect(Collectors.toList());

        List<String> restaurantCategories = users.getUserInterests().stream()
                .flatMap(userInterest -> userInterest.getInterest().getRestaurantCategories().stream())
                .collect(Collectors.toList());

        // 3. 카테고리별로 반환 (Map<String, Object>)
        Map<String, Object> categories = new HashMap<>();
        categories.put("touristCategories", touristCategories);
        categories.put("restaurantCategories", restaurantCategories);

        return categories;
    }

}