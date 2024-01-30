package by.skachkovdmitry.personal_account.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignClientConfiguration {

//    @Value("${your.token.value}")
//    private String token;
    @Bean
    public RequestInterceptor requestInterceptor() {
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate template) {
//                // Добавление токена в хэдер запроса
//                template.header("Authorization", "Bearer " + token);
            }
        };
    }
}

