package dev.ryan.AgileBoardBackEndSpring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

@SpringBootApplication
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class AgileBoardBackEndSpringApplication {

    public static void main(String[] args) {
        SpringApplication.run(AgileBoardBackEndSpringApplication.class, args);
    }

}
