package tr.shadowise_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ShadowiseapiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShadowiseapiApplication.class, args);
	}

}
