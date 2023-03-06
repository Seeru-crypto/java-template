package grp.javatemplate;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@Slf4j
public class JavaTemplateApplication implements CommandLineRunner {

	// TODO: What options are there for user authentication?
	public static void main(String[] args) {
		SpringApplication.run(JavaTemplateApplication.class, args);
	}

	@Override
	public void run(String... args) {
		log.info("Swagger enabled at http://localhost:8880/swagger-ui/index.html#/");
	}

	@Value("${corsAllowedOrigin}")
	private String corsAllowedOrigin;

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings( CorsRegistry registry ) {
				registry.addMapping("/**").allowedOrigins(corsAllowedOrigin).allowedMethods("*");
			}
		};
	}
}
