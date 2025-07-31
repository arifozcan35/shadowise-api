package tr.shadowise_api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.StringHttpMessageConverter;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

@Configuration
public class MarkdownConverterConfig {

    @Bean
    public StringHttpMessageConverter stringHttpMessageConverter() {
        StringHttpMessageConverter converter = new StringHttpMessageConverter(StandardCharsets.UTF_8);
        converter.setSupportedMediaTypes(Arrays.asList(
                MediaType.TEXT_PLAIN,
                MediaType.TEXT_HTML,
                MediaType.APPLICATION_JSON,
                new MediaType("text", "markdown")
        ));
        return converter;
    }
}