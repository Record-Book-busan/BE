package busim.kkilogbu.global;

import lombok.Getter;

@Getter
public enum ZoomLevel {
	LEVEL_1(0.1),
	LEVEL_2(0.2),
	LEVEL_3(0.3),
	LEVEL_4(0.5),
	LEVEL_5(1),
	LEVEL_6(2.5),
	LEVEL_7(5),
	LEVEL_8(10),
	LEVEL_9(20),
	LEVEL_10(40),
	LEVEL_11(80),
	LEVEL_12(160),
	LEVEL_13(320),
	LEVEL_14(640),
	LEVEL_15(1280)
	;

	private final double kilometer;

	ZoomLevel(double kilometer) {
		this.kilometer = kilometer;
	}

	// 줌 레벨이 낮은지 판단하는 메서드 (예: LEVEL_5 이하가 낮은 줌 레벨로 간주)
	public boolean isLowZoom() {
		// 여기서는 LEVEL_5 이하를 "낮은 줌 레벨"로 정의
		return this.ordinal() <= LEVEL_5.ordinal();
	}
}
