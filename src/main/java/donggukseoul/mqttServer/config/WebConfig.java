package donggukseoul.mqttServer.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
//                        .allowedOriginPatterns("*")
                        .allowedOrigins("*", "https://localhost:3000", "https://oneandonlyako.netlify.app");  // 모든 도메인 허용
//                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")  // 허용할 HTTP 메서드
//                        .allowedHeaders("*")  // 모든 헤더 허용
//                        .allowCredentials(true);  // 자격 증명 허용
            }
        };
    }
}
