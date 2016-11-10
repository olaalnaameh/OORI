package de.fhg.ids.app.ldcontainer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;

/**
 * This is a Spring Boot Application
 *
 * @see <a href="http://projects.spring.io/spring-boot/">Spring Boot</a>
 * @author <a href="mailto:ralf.nagel@isst.fraunhofer.de">Ralf Nagel</a>
 */
@Configuration
@SpringBootApplication
@EnableAutoConfiguration
public class Application {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Application.class, args);
    }

}