package busim.kkilogbu.test;


import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RedisTestController {


    private final StringRedisTemplate stringRedisTemplate;

    @GetMapping("/redis-test")
    public String redisTest() {
        stringRedisTemplate.opsForValue().set("test-key", "test-value");
        String value = stringRedisTemplate.opsForValue().get("test-key");
        return value;
    }
}
