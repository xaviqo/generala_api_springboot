package tech.xavi.generalabe.configuration.security;

import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class SecurityBeans {

    @Bean
    BCryptPasswordEncoder bcPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

}