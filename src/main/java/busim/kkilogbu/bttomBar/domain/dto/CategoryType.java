package busim.kkilogbu.bttomBar.domain.dto;

public enum CategoryType {

    OCEAN_VIEW("(SNS)포장마차"),                  // 오션뷰
    BLUE_RIBBON("블루리본"),      // 블루리본
    GOOD_RESTAURANT("착한업소"),            // 착한식당
    TOURISM_RECOMMENDED("관광공사추천"), // 관광공사 추천
    BUSAN_RECOMMENDED("부산시추천"),   // 부산시 추천
    FAMOUS_SOUP("국밥"),              // 국밥맛집
    CELEBRITY_FAVORITE("연예인 추천 맛집"), // 연예인 맛집
    ALL("모든카테고리");                   // 전체

    private final String description;

    // 생성자
    CategoryType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
