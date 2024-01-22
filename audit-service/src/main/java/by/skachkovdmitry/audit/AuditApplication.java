package by.skachkovdmitry.audit;

import by.skachkovdmitry.audit.config.properies.JWTProperty;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({JWTProperty.class})
public class AuditApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuditApplication.class, args);
	}

}
