package grp.javatemplate;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class JavaTemplateApplication {

	// TODO: Add Swagger support with authentication
	// TODO: Add example of a custom SQL repository function.
	// TODO: What options are there for user authentication?
	// TODO: Should a basic code style be enforced?
	// TODO: Should flyway be used or leave DB schema generation to hibernate?
	public static void main(String[] args) {
		SpringApplication.run(JavaTemplateApplication.class, args);
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
