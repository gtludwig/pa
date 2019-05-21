package ie.gtludwig.pa.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
public class PersistenceConfiguration {

    @Autowired
    Environment environment;

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
        driverManagerDataSource.setDriverClassName(environment.getRequiredProperty("spring.datasource.driverClassName"));
        driverManagerDataSource.setUrl(environment.getRequiredProperty("spring.datasource.url"));
        driverManagerDataSource.setUsername(environment.getRequiredProperty("spring.datasource.username"));
        driverManagerDataSource.setPassword(environment.getRequiredProperty("spring.datasource.password"));

        return driverManagerDataSource;
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}