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

	public static void main(String[] args) {
		SpringApplication.run(JavaTemplateApplication.class, args);
	}
	// TODO: Add Github actions for build and testing.
	// https://docs.github.com/en/actions/automating-builds-and-tests/building-and-testing-java-with-gradle
	// Run on Pullrequests being opened and closed -  https://docs.github.com/en/actions/using-workflows/events-that-trigger-workflows#pull_request
	// Run only on master branch -  https://docs.github.com/en/actions/using-workflows/events-that-trigger-workflows#running-your-pull_request-workflow-based-on-the-head-or-base-branch-of-a-pull-request

	@Override
	public void run(String... args) {
		log.info("Swagger enabled at http://localhost:8880/swagger-ui/index.html#/");
		log.info("Keycloak hosted at http://localhost:9080/keycloak/");
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
