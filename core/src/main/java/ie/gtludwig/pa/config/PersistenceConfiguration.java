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

//    private static final Logger logger = LoggerFactory.getLogger(PersistenceConfiguration.class);
//
//    @Bean
//    public LocalSessionFactoryBean localSessionFactoryBean() {
//        LocalSessionFactoryBean localSessionFactoryBean = new LocalSessionFactoryBean();
//        localSessionFactoryBean.setDataSource(dataSource());
//        localSessionFactoryBean.setPackagesToScan(new String[] {"ie.gtludwig.pa.model"});
//        localSessionFactoryBean.setHibernateProperties(hibernateProperties());
//        return localSessionFactoryBean;
//    }
//
//    @Bean
//    public Properties hibernateProperties() {
//        Properties hibernateProperties = new Properties();
//		hibernateProperties.put("spring.jpa.database-platform", environment.getProperty("spring.jpa.database-platform"));
//		hibernateProperties.put("spring.jpa.show_sql", environment.getProperty("spring.jpa.show_sql"));
//		hibernateProperties.put("spring.jpa.hibernate.ddl-auto", environment.getProperty("spring.jpa.hibernate.ddl-auto"));
//
//        return hibernateProperties;
//    }
}