	package busim.kkilogbu.global.config;

	import org.springframework.beans.factory.annotation.Value;
	import org.springframework.context.annotation.Bean;
	import org.springframework.context.annotation.Configuration;
	import org.springframework.data.redis.connection.RedisConnectionFactory;
	import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
	import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
	import org.springframework.data.redis.core.RedisTemplate;
	import org.springframework.data.redis.serializer.StringRedisSerializer;

	@Configuration
	public class RedisConfig {
		@Value("${spring.data.redis.host}")
		private String host;
		@Value("${spring.data.redis.port}")
		private int port;

		@Bean
		public RedisConnectionFactory redisConnectionFactory() {
			return new LettuceConnectionFactory (new RedisStandaloneConfiguration(host, port));
		}

		@Bean
		public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
			RedisTemplate<String, String> template = new RedisTemplate<>();
			template.setConnectionFactory(redisConnectionFactory());
			template.setKeySerializer(new StringRedisSerializer());
			template.setValueSerializer(new StringRedisSerializer());
			template.setHashKeySerializer(new StringRedisSerializer());
			template.setHashValueSerializer(new StringRedisSerializer());
			return template;
		}
	}
