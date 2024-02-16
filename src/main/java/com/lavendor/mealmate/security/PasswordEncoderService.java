package com.lavendor.mealmate.security;

import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PasswordEncoderService {

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public String encodePassword(String password) {
        return bCryptPasswordEncoder().encode(password);
    }

    public boolean matchPassword(String password, String encodedPassword) {
        return bCryptPasswordEncoder().matches(password, encodedPassword);
    }
}
