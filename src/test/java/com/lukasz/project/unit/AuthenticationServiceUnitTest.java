package com.lukasz.project.unit;

import com.lukasz.project.database.auth.*;
import com.lukasz.project.database.config.JwtService;
import com.lukasz.project.model.Admin;
import com.lukasz.project.model.Recruiter;
import com.lukasz.project.model.RegisteredUser;
import com.lukasz.project.model.User;
import com.lukasz.project.repository.UserRepository;
import com.lukasz.project.token.Token;
import com.lukasz.project.token.TokenRepository;
import com.lukasz.project.validator.MyValidationException;
import com.lukasz.project.validator.ObjectValidatorImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceUnitTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private Extractor extractor;
    @Mock
    private JwtService jwtService;
    @Mock
    private TokenRepository tokenRepository;
    @Mock
    private ObjectValidatorImpl<RegisterRequest> validator;

    @InjectMocks
    private AuthenticationService authenticationService;


    @Test
    public void thisShouldRegisterUserSuccessfully() {
        // Arrange
        RegisterRequest request = new RegisterRequest("John", "Doe", "12345678901", "USA", "john@example.com", "john_doe", "password123");
        RegisteredUser user = new RegisteredUser();
        user.setEmail("john@example.com");
        when(extractor.createActorFromRequest(any(RegisterRequest.class), eq(RegisteredUser.class))).thenReturn(user);
        when(userRepository.save(any(RegisteredUser.class))).thenReturn(user);
        when(jwtService.generateToken(anyMap(), any(User.class))).thenReturn("jwtToken123");

        // Act
        AuthenticationResponse response = authenticationService.register(request);

        // Assert
        assertNotNull(response);
        assertEquals("jwtToken123", response.getToken());
        verify(userRepository).save(user);
        verify(jwtService).generateToken(anyMap(), eq(user));
    }

    @Test
    void thisShouldThrowExceptionWhileRegisteringUser() {
        // Arrange
        RegisterRequest request = new RegisterRequest(); // Set the required properties
        RegisteredUser user = new RegisteredUser(); // Set the required properties

        when(extractor.createActorFromRequest(any(), eq(RegisteredUser.class))).thenReturn(user);
        when(userRepository.save(any(RegisteredUser.class))).thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            authenticationService.register(request);
        });

        // Optionally verify the exception details
        assertTrue(exception.getMessage().contains("Error during registration"));
        assertTrue(exception.getCause() instanceof RuntimeException);
        assertTrue(exception.getCause().getMessage().contains("Database error"));

        // Verify interactions
        verify(userRepository).save(user);
        // Verify that other methods are not called after the exception is thrown
        verify(jwtService, never()).generateToken(anyMap(), any());
    }

    @Test
    void thisShouldRegisterAdminSuccessfully() {
        // Arrange
        RegisterRequest request = new RegisterRequest(); // Set the required properties
        Admin admin = new Admin(); // Set the required properties

        when(extractor.createActorFromRequest(any(), eq(Admin.class))).thenReturn(admin);
        when(userRepository.save(any(Admin.class))).thenReturn(admin);
        when(jwtService.generateToken(anyMap(), any(Admin.class))).thenReturn("jwtToken123");

        // Act
        AuthenticationResponse response = authenticationService.registerAdmin(request);

        // Assert
        assertNotNull(response);
        assertEquals("jwtToken123", response.getToken());

        // Verify interactions
        verify(userRepository).save(admin);
        verify(jwtService).generateToken(anyMap(), eq(admin));
    }

    @Test
    void thisShouldThrowExceptionWhileRegisteringAdmin() {
        // Arrange
        RegisterRequest request = new RegisterRequest(); // Set the required properties

        when(extractor.createActorFromRequest(any(), eq(Admin.class)))
                .thenThrow(new RuntimeException("Failed to create admin"));

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            authenticationService.registerAdmin(request);
        });

        // Assert exception details
        assertEquals("Error during admin registration", exception.getMessage());
        assertNotNull(exception.getCause());
        assertEquals("Failed to create admin", exception.getCause().getMessage());

        // Verify that no other interactions have occurred after the exception
        verify(userRepository, never()).save(any(Admin.class));
        verify(jwtService, never()).generateToken(anyMap(), any(Admin.class));
    }

    @Test
    void testRegisterRecruiterSuccessfully() {
        // Arrange
        RegisterRequest request = new RegisterRequest(); // Set up request data
        Recruiter recruiter = new Recruiter(); // Set up recruiter data
        Set<String> violations = new HashSet<>();

        when(validator.validate(request)).thenReturn(violations);
        when(extractor.createActorFromRequest(any(), eq(Recruiter.class))).thenReturn(recruiter);
        when(userRepository.save(any(Recruiter.class))).thenReturn(recruiter);
        when(jwtService.generateToken(anyMap(), any(Recruiter.class))).thenReturn("jwtToken123");

        // Act
        AuthenticationResponse response = authenticationService.registerRecruiter(request);

        // Assert
        assertNotNull(response);
        assertEquals("jwtToken123", response.getToken());

        // Verify interactions
        verify(validator).validate(request);
        verify(userRepository).save(recruiter);
        verify(jwtService).generateToken(anyMap(), eq(recruiter));
    }

    @Test
    void testRegisterRecruiterThrowsValidationException() {
        // Arrange
        RegisterRequest request = new RegisterRequest(); // Initialize with appropriate values
        Set<String> violations = new HashSet<>();
        violations.add("Invalid data"); // Add appropriate violation messages

        when(validator.validate(request)).thenReturn(violations);

        // Act & Assert
        MyValidationException exception = assertThrows(MyValidationException.class, () -> {
            authenticationService.registerRecruiter(request);
        });

        // Assert exception details
        assertEquals(violations, exception.getViolations(), "The violations in the exception should match those returned by the validator");

        // Verify interactions
        verify(validator).validate(request); // Verify that validate was called
        verify(extractor, never()).createActorFromRequest(any(), any()); // Ensure no user creation is attempted
        verify(userRepository, never()).save(any(Recruiter.class)); // Ensure no save operation is attempted
        verify(jwtService, never()).generateToken(anyMap(), any(Recruiter.class)); // Ensure no token generation is attempted
    }

    @Test
    void testAuthenticateSuccess() {
        // Arrange
        AuthenticationRequest request = new AuthenticationRequest("user@example.com", "password");
        User user = new User(); // set user properties, especially email and role
        String jwtToken = "jwtToken123";

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(user));
        when(jwtService.generateToken(anyMap(), eq(user))).thenReturn(jwtToken);

        // Act
        AuthenticationResponse response = authenticationService.authenticate(request);

        // Assert
        assertNotNull(response);
        assertEquals(jwtToken, response.getToken());
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtService).generateToken(anyMap(), eq(user));
        verify(tokenRepository).save(any(Token.class));
    }

    @Test
    void testAuthenticateUserNotFound() {
        // Arrange
        AuthenticationRequest request = new AuthenticationRequest("unknown@example.com", "password");

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> {
            authenticationService.authenticate(request);
        });
    }
}