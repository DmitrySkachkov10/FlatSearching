package by.dmitryskachkov.flatservice;

import by.dmitryskachkov.flatservice.config.properies.JWTProperty;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients()
@EnableConfigurationProperties({JWTProperty.class})
public class FlatServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(FlatServiceApplication.class, args);
	}

}
