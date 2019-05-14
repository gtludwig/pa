package ie.gtludwig.pa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
//@EnableJpaRepositories(value = "ie.gtludwig.pa.dao")
public class PaApplication {

    public static void main(String[] args) {
        SpringApplication.run(PaApplication.class, args);
    }
}
