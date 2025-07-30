package tr.shadowise_api.config;

import feign.Request;
import feign.RequestInterceptor;
import feign.codec.Encoder;
import feign.codec.ErrorDecoder;
import feign.form.spring.SpringFormEncoder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class FeignClientConfig {

    @Bean
    public Encoder feignFormEncoder(ObjectFactory<HttpMessageConverters> messageConverters) {
        return new SpringFormEncoder(new SpringEncoder(messageConverters));
    }

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            // Check if this is a multipart request by looking at the URL path
            String url = requestTemplate.url();
            boolean isMultipartRequest = url.contains("/upload-pdf") || url.contains("upload");
            
            if (!isMultipartRequest) {
                // İstek başlıkları ekle (sadece multipart olmayan istekler için)
                requestTemplate.header("Accept", "application/json");
                requestTemplate.header("Content-Type", "application/json");
            } else {
                // Multipart istekler için sadece Accept header'ı ekle
                // Content-Type'ı Feign otomatik olarak multipart/form-data yapacak
                requestTemplate.header("Accept", "application/json");
            }
            
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