package busim.kkilogbu.global.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info =  @Info(title = "부심 API",
                description = "끼록부: 부산 기행",
                version = "V1")
)
@RequiredArgsConstructor
@Configuration
public class SawggerConfig {

    @Bean
    public GroupedOpenApi groupedOpenApi() {

        // "/v1/**" 경로에 매칭되는 API를 그룹화하여 문서화한다.
        String[] paths = {"/kkilogbu/**"};

        return GroupedOpenApi.builder()
                .group("마이펫 API")  // 그룹 이름을 설정한다.
                .pathsToMatch(paths)     // 그룹에 속하는 경로 패턴을 지정한다.
                .build();
    }

}