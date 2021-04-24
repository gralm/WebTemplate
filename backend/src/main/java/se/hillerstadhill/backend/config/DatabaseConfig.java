package se.hillerstadhill.backend.config;

import lombok.extern.slf4j.Slf4j;
import org.jasypt.util.text.AES256TextEncryptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

/**
 Remove user, password... from application.properties and put it here instead.
 Use the first application argument, 'password' in this case, to decrypt the username and password
 Makes commits to github safer?
 */
@Configuration
@Slf4j
public class DatabaseConfig {

    private static final String ENCRYPTED_USERNAME = "Te4VfQ5brP3fy/41r7o2l5OS/atV00umrLQVaQgLzpJAPBEEVl3ov8pBHy2q1gLh";
    private static final String ENCRYPTED_PASSWORD = "g0D6GtoD3RHntKLorOMyY+Jv+wU5e4nmhBXQTDofl+ER4Y6Y5EXhJsCpFBGb2Dtk";

    @Value("${example.firstProperty}")
    private String firstProperty;

    @Bean
    @Profile("!test")
    public DataSource dataSource(ApplicationArguments arguments) {
        log.info("first Property: " + firstProperty);

        AES256TextEncryptor encryptor = new AES256TextEncryptor();
        //encryptor.setPassword("password");
        log.info("first arg: " + arguments.getSourceArgs()[0]);
        encryptor.setPassword(arguments.getSourceArgs()[0]);

        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/test");
        //dataSource.setUsername("root");
        log.info("username: " + encryptor.decrypt(ENCRYPTED_USERNAME));
        dataSource.setUsername(encryptor.decrypt(ENCRYPTED_USERNAME));
        //dataSource.setPassword("password");
        log.info("password: " + encryptor.decrypt(ENCRYPTED_PASSWORD));
        dataSource.setPassword(encryptor.decrypt(ENCRYPTED_PASSWORD));
        return dataSource;
    }
}
