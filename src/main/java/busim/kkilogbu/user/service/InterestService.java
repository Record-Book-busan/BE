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
        log.info("유저의 관심사를 저장합니다. 유저 ID: {}", socialUserId);

        // 유저 조회
        Users users = userRepository.findBySocialUserId(socialUserId)
                .orElseThrow(() -> {
                    log.error("유저를 찾을 수 없습니다. 유저 ID: {}", socialUserId);
                    return new EntityNotFoundException("회원 가입 안되었습니다.");
                });

        log.info("유저를 찾았습니다. 유저 ID: {}", users.getId());

        // 그대로 문자열 리스트를 저장
        List<String> touristCategories = new ArrayList<>(touristCategoryNames);
        List<String> restaurantCategories = new ArrayList<>(restaurantCategoryNames);

        log.debug("관광 카테고리 리스트: {}", touristCategories);
        log.debug("레스토랑 카테고리 리스트: {}", restaurantCategories);

        // 새로운 Interest 생성 및 저장
        Interest interest = Interest.builder()
                .touristCategories(touristCategories)
                .restaurantCategories(restaurantCategories)
                .build();

        interestRepository.save(interest);
        log.info("Interest가 성공적으로 저장되었습니다. Interest ID: {}", interest.getId());


        UserInterest userInterest = new UserInterest(users, interest);

        userInterestRepository.save(userInterest);
        log.info("UserInterest가 성공적으로 저장되었습니다. 유저 ID: {}, Interest ID: {}", users.getId(), interest.getId());
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