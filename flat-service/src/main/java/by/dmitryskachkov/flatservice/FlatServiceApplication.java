package by.dmitryskachkov.flatservice;

import by.dmitryskachkov.flatservice.config.properies.JWTProperty;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({JWTProperty.class})
public class FlatServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(FlatServiceApplication.class, args);
	}

}
