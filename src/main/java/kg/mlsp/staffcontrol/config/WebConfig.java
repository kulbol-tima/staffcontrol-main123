package kg.mlsp.staffcontrol.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(@NonNull CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Разрешить методы
                .allowedHeaders("Content-Type", "Authorization", "Custom-Header") // Разрешить заголовки
                .exposedHeaders("Custom-Exposed-Header") // Заголовки, доступные клиенту
                .allowCredentials(true) // Разрешить отправку куки
                .maxAge(3600); // Кэширование preflight-запросов (в секундах)
    }
}
