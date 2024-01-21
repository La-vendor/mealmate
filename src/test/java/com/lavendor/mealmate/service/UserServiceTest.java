package com.lavendor.mealmate.service;

import com.lavendor.mealmate.MealmateApplication;
import com.lavendor.mealmate.model.User;
import com.lavendor.mealmate.model.UserDTO;
import com.lavendor.mealmate.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@SpringBootTest(classes = MealmateApplication.class)
public class UserServiceTest {

    private final String username = "test_username";
    private final String password = "test_password";
    private final Long userId = 1L;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Test
    public void testCreateUserWithNullUsername() {
        UserDTO mockUserDTO = new UserDTO(null, "password", "password");

        assertThrows(RuntimeException.class, () -> {
            userService.createUser(mockUserDTO);
        });
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void testCreateUser() {

        UserDTO mockUserDTO = new UserDTO(username, password, password);
        User expectedUser = new User(username,password);

        when(userRepository.save(any(User.class))).thenReturn(expectedUser);

        User savedUser = userService.createUser(mockUserDTO);

        assertNotNull(savedUser);
        verify(userRepository, times(1)).save(any(User.class));

        assertEquals(username, savedUser.getUsername());
    }

    @Test
    public void testCreateUserWithUsernameNotAvailable() {

        UserDTO mockUserDTO = new UserDTO(username, password, password);

        when(userRepository.save(any(User.class))).thenThrow(DataIntegrityViolationException.class);


        RuntimeException runtimeException = assertThrows(RuntimeException.class, ()->{
            userService.createUser(mockUserDTO);
        });
        verify(userRepository, times(1)).save(any(User.class));
        assertTrue(runtimeException.getMessage().contains("Username is not available"));
    }

    @Test
    public void testCheckIfPasswordIsEqual_PasswordsMatch() {
        String password = "testPassword";
        UserDTO userDTO = new UserDTO(password, password, password);

        boolean result = userService.checkIfPasswordIsEqual(userDTO);

        assertTrue(result, "Passwords should match");
    }

    @Test
    public void testCheckIfPasswordIsEqual_PasswordsDoNotMatch() {
        String password = "testPassword";
        String confirmPassword = "differentPassword";
        UserDTO userDTO = new UserDTO(password, confirmPassword, password);

        boolean result = userService.checkIfPasswordIsEqual(userDTO);

        assertFalse(result, "Passwords should not match");
    }

    @Test
    public void testGetUserByUsername() {
        User expectedUser = new User(username,password);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(expectedUser));

        Optional<User> result = userService.getUserByUsername(username);

        assertTrue(result.isPresent());
        assertEquals(expectedUser, result.get());
    }

    @Test
    public void testGetUserByUsername_NonExistingEntity() {

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        Optional<User> result = userService.getUserByUsername(username);

        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetUserById() {
        User expectedUser = new User();
        when(userRepository.findById(userId)).thenReturn(Optional.of(expectedUser));

        User result = userService.getUserById(userId);

        assertEquals(expectedUser, result);
    }

    @Test
    public void testGetUserById_NonExistingEntity() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> userService.getUserById(userId));

        assertTrue(exception.getMessage().contains("User not found"));
    }

    @Test
    public void testDeleteUser() {
        User userToDelete = new User();
        when(userRepository.findById(userId)).thenReturn(Optional.of(userToDelete));

        assertDoesNotThrow(() -> userService.deleteUser(userId));

        verify(userRepository, times(1)).delete(userToDelete);
    }

    @Test
    public void testDeleteUser_NonExistingEntity() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> userService.deleteUser(userId));

        assertTrue(exception.getMessage().contains("User not found"));
    }

    @Test
    public void testGetActiveUserId_Authenticated() {
        Authentication mockAuthentication = mock(Authentication.class);
        String username = "authenticatedUser";
        when(mockAuthentication.isAuthenticated()).thenReturn(true);
        when(mockAuthentication.getName()).thenReturn(username);

        User mockUser = new User();
        mockUser.setUserId(1L);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(mockUser));

        Long result = userService.getActiveUserId(mockAuthentication);

        assertNotNull(result);
        assertEquals(mockUser.getUserId(), result);
    }

    @Test
    public void testGetActiveUserId_NotAuthenticated() {
        Authentication mockAuthentication = mock(Authentication.class);
        when(mockAuthentication.isAuthenticated()).thenReturn(false);

        Long result = userService.getActiveUserId(mockAuthentication);

        assertNull(result);
    }

    @Test
    public void testGetActiveUserId_UserNotFound() {
        Authentication mockAuthentication = mock(Authentication.class);
        String username = "nonExistentUser";
        when(mockAuthentication.isAuthenticated()).thenReturn(true);
        when(mockAuthentication.getName()).thenReturn(username);
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        Long result = userService.getActiveUserId(mockAuthentication);

        assertNull(result);
    }
}
