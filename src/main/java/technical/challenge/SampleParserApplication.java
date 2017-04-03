package technical.challenge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
/**
 * 
 * Spring boot start
 *
 */
@SpringBootApplication
@EnableOAuth2Sso
@EnableAuthorizationServer
public class SampleParserApplication {

	public static void main(String[] args) {
		SpringApplication.run(SampleParserApplication.class, args);
	}

}
