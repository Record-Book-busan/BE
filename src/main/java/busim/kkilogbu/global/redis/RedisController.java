package busim.kkilogbu.global.redis;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class RedisController {

	private final RedisService redisService;

	@GetMapping("/redis")
	public String  redis() {
		redisService.dailyRedisUpdateAt4AM();
		return "ok";
	}
}
