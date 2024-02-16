package com.lavendor.mealmate.security;

import com.lavendor.mealmate.user.JpaUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class AuthenticationService  implements AuthenticationProvider {

    @Autowired
    private  PasswordEncoderService passwordEncoderService;

    @Autowired
    private JpaUserDetailsService jpaUserDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        try{
            UserDetails userDetails = jpaUserDetailsService.loadUserByUsername(username);

            if (passwordEncoderService.matchPassword(password, userDetails.getPassword())) {
                return new UsernamePasswordAuthenticationToken(username, password, new ArrayList<>());
            }
        }catch(UsernameNotFoundException ignore){
        }
        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
