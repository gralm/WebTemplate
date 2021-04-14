package se.hillerstadhill.backend.config;

import org.jasypt.util.text.AES256TextEncryptor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

/**
 Remove user, password... from application.properties and put it here instead.
 Use the first application argument, 'password' in this case, to decrypt the username and password
 Makes commits to github safer?
 */
@Configuration
public class DatabaseConfig {

    private static final String ENCRYPTED_USERNAME = "Te4VfQ5brP3fy/41r7o2l5OS/atV00umrLQVaQgLzpJAPBEEVl3ov8pBHy2q1gLh";
    private static final String ENCRYPTED_PASSWORD = "g0D6GtoD3RHntKLorOMyY+Jv+wU5e4nmhBXQTDofl+ER4Y6Y5EXhJsCpFBGb2Dtk";


    @Bean
    public DataSource dataSource(ApplicationArguments arguments) {
        AES256TextEncryptor encryptor = new AES256TextEncryptor();
        //encryptor.setPassword("password");
        encryptor.setPassword(arguments.getSourceArgs()[0]);

        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/test");
        //dataSource.setUsername("root");
        dataSource.setUsername(encryptor.decrypt(ENCRYPTED_USERNAME));
        //dataSource.setPassword("password");
        dataSource.setPassword(encryptor.decrypt(ENCRYPTED_PASSWORD));
        return dataSource;
    }
}
