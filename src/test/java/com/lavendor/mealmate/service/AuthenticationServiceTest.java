package com.lavendor.mealmate.service;

import com.lavendor.mealmate.MealmateApplication;
import com.lavendor.mealmate.security.AuthenticationService;
import com.lavendor.mealmate.security.PasswordEncoderService;
import com.lavendor.mealmate.user.JpaUserDetailsService;
import com.lavendor.mealmate.user.SecurityUser;
import com.lavendor.mealmate.user.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@SpringBootTest(classes = MealmateApplication.class)
public class AuthenticationServiceTest {

    @MockBean
    private PasswordEncoderService passwordEncoderService;

    @MockBean
    private JpaUserDetailsService jpaUserDetailsService;

    @Autowired
    private AuthenticationService authenticationService;

    @Test
    public void testAuthenticate_SuccessfulAuthentication() {
        String username = "testUser";
        String password = "testPassword";

        UserDetails userDetails = new SecurityUser(new User(username, password));

        when(jpaUserDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        when(passwordEncoderService.matchPassword(password, userDetails.getPassword())).thenReturn(true);

        Authentication authentication = new UsernamePasswordAuthenticationToken(username, password);

        Authentication result = authenticationService.authenticate(authentication);

        assertNotNull(result);
        assertTrue(result.isAuthenticated());
        assertEquals(username, result.getName());
    }

    @Test
    public void testAuthenticate_InvalidPassword() {
        String username = "testUser";
        String password = "invalidPassword";

        UserDetails userDetails = new SecurityUser(new User(username, "correctPassword"));

        when(jpaUserDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        when(passwordEncoderService.matchPassword(password, userDetails.getPassword())).thenReturn(false);

        Authentication authentication = new UsernamePasswordAuthenticationToken(username, password);

        Authentication result = authenticationService.authenticate(authentication);

        assertNull(result);
    }

    @Test
    public void testAuthenticate_UserNotFound() {
        String username = "nonExistingUser";
        String password = "testPassword";

        when(jpaUserDetailsService.loadUserByUsername(username)).thenThrow(new UsernameNotFoundException("User not found"));

        Authentication authentication = new UsernamePasswordAuthenticationToken(username, password);

        Authentication result = authenticationService.authenticate(authentication);

        assertNull(result);
    }

    @Test
    public void testSupports() {
        assertTrue(authenticationService.supports(UsernamePasswordAuthenticationToken.class));
    }

}
