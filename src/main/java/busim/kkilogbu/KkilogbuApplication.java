package busim.kkilogbu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;


@SpringBootApplication
@EnableFeignClients(basePackages = "busim.kkilogbu.user.appple")
public class KkilogbuApplication {

	public static void main(String[] args) {
		SpringApplication.run(KkilogbuApplication.class, args);
	}


}


