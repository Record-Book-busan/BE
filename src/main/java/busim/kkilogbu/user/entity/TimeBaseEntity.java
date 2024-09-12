package busim.kkilogbu.user.entity;

import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
public abstract class TimeBaseEntity {

    private LocalDateTime createdAt;  // 생성일
    private LocalDateTime updatedAt;  // 수정일

    // 동의한 날짜 (특정한 경우에 사용)
    private LocalDateTime consentDate;

    // Entity가 생성될 때 자동으로 호출되도록 설정
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.consentDate = LocalDateTime.now();
    }

    // Entity가 수정될 때 자동으로 호출되도록 설정
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
