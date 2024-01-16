package com.lukasz.project.unit;

import com.lukasz.project.database.config.JwtAuthenticationFilter;
import com.lukasz.project.database.config.JwtService;
import com.lukasz.project.token.Token;
import com.lukasz.project.token.TokenRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterUnitTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private TokenRepository tokenRepository;

    @InjectMocks
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Helper method to create a mock UserDetails
    private UserDetails mockUserDetails() {
        return new User("user@example.com", "password", new ArrayList<>());
    }

    @Test
    public void testDoFilterInternal_NoAuthorizationHeader() throws ServletException, IOException {
        // Stub the getServletPath() method to return a non-null value
        when(request.getServletPath()).thenReturn("/some/path");

        // Simulate no Authorization header
        when(request.getHeader("Authorization")).thenReturn(null);

        // Call doFilterInternal
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);


        // Verify filterChain.doFilter is called
        verify(filterChain).doFilter(request, response);
        // Verify no other interactions
        verifyNoMoreInteractions(jwtService, userDetailsService, tokenRepository);
    }

    @Test
    public void testDoFilterInternal_InvalidTokenFormat() throws Exception {
        // Stub the getServletPath() method to return a non-null value
        when(request.getServletPath()).thenReturn("/some/path");

        // Setting up an Authorization header that does not start with "Bearer "
        when(request.getHeader("Authorization")).thenReturn("InvalidTokenFormat");

        // Call doFilterInternal
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Verify that filterChain.doFilter() is called
        verify(filterChain).doFilter(request, response);

        // Verify that no other methods are called on jwtService, userDetailsService, or tokenRepository
        verifyNoInteractions(jwtService, userDetailsService, tokenRepository);
    }

    @Test
    void doFilterInternal_whenTokenIsValid() throws Exception {
        // Arrange
        String validToken = "Bearer validToken";
        UserDetails mockUserDetails = new User("user", "password", Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
        Token token = new Token(); // Assuming Token is a custom class representing a token

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", validToken);
        MockHttpServletResponse response = new MockHttpServletResponse();

        // Act
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(filterChain).doFilter(request, response);
        // Additional asserts can be added to verify the behavior of the SecurityContext
    }

    @Test
    public void testDoFilterInternal_WithApiAuthPath() throws ServletException, IOException {

        // Setup the request to have a servlet path that contains "/api/auth"
        when(request.getServletPath()).thenReturn("/api/auth");

        // Call doFilterInternal
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Verify that filterChain.doFilter is called
        verify(filterChain).doFilter(request, response);

        // Verify that jwtService, userDetailsService, and tokenRepository are not interacted with
        verifyNoInteractions(jwtService, userDetailsService, tokenRepository);

        // Optionally, you can also verify that SecurityContextHolder is not modified
        Assertions.assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

//    @Test
//    public void testDoFilterInternal_WithValidTokenAndNoExistingAuthentication() throws Exception {
//        String validToken = "Bearer validtokenstring";
//        String tokenWithoutBearer = validToken.substring(7);
//        String userEmail = "user@example.com";
//
//        when(request.getHeader("Authorization")).thenReturn(validToken);
//        when(jwtService.extractUsername(tokenWithoutBearer)).thenReturn(userEmail);
//        when(SecurityContextHolder.getContext().getAuthentication()).thenReturn(null);
//
//        UserDetails mockUserDetails = new User(userEmail, "password", Collections.emptyList());
//        when(userDetailsService.loadUserByUsername(userEmail)).thenReturn(mockUserDetails);
//
//        Token mockToken = new Token(); // Assuming Token is a valid entity
//        when(tokenRepository.findByToken(tokenWithoutBearer)).thenReturn(Optional.of(mockToken));
//        when(jwtService.isTokenValid(tokenWithoutBearer, mockUserDetails)).thenReturn(true);
//
//        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);
//
//        verify(userDetailsService).loadUserByUsername(userEmail);
//        verify(tokenRepository).findByToken(tokenWithoutBearer);
//        verify(jwtService).isTokenValid(tokenWithoutBearer, mockUserDetails);
//
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        Assertions.assertNotNull(authentication);
//        assertTrue(authentication instanceof UsernamePasswordAuthenticationToken);
//        assertEquals(userEmail, ((UsernamePasswordAuthenticationToken) authentication).getName());
//
//        verify(filterChain).doFilter(request, response);
//    }
}