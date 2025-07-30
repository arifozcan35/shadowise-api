package tr.shadowise_api.config;

import feign.Request;
import feign.RequestInterceptor;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class FeignClientConfig {

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            // İstek başlıkları ekle
            requestTemplate.header("Accept", "application/json");
            requestTemplate.header("Content-Type", "application/json");
            
            // Örnek olarak kimlik doğrulama eklenebilir
            // requestTemplate.header("Authorization", "Bearer " + jwtToken);
        };
    }

    @Bean
    public Request.Options requestOptions() {
        // Zaman aşımı sürelerini ayarlama: 5 saniye bağlantı, 15 saniye okuma
        return new Request.Options(5, TimeUnit.SECONDS, 15, TimeUnit.SECONDS, true);
    }

    @Bean
    public ErrorDecoder errorDecoder() {
        return (methodKey, response) -> {
            // Hata yönetimini özelleştirme
            int status = response.status();
            
            if (status >= 400 && status < 500) {
                return new RuntimeException("Client error when calling Python API: " + status);
            } else if (status >= 500) {
                return new RuntimeException("Server error when calling Python API: " + status);
            }
            
            return new RuntimeException("Unexpected error when calling Python API");
        };
    }
}