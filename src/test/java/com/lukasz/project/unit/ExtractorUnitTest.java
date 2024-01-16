package com.lukasz.project.unit;

import com.lukasz.project.database.auth.Extractor;
import com.lukasz.project.database.auth.RegisterRequest;
import com.lukasz.project.dto.OfferRequest;
import com.lukasz.project.model.*;
import com.lukasz.project.token.Token;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExtractorUnitTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    private Extractor extractor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        extractor = new Extractor(passwordEncoder);
    }

    @Test
    void testCreateActorFromRequestForAdmin() {
        // Arrange
        RegisterRequest request = new RegisterRequest(); // Set the properties
        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");

        // Act
        Admin admin = extractor.createActorFromRequest(request, Admin.class);

        // Assert
        assertNotNull(admin);
        assertEquals(request.getEmail(), admin.getEmail());
        assertEquals("encodedPassword", admin.getPassword());
    }

    @Test
    void testCreateActorFromRequestForRecruiter() {
        // Arrange
        RegisterRequest request = new RegisterRequest(); // Initialize with appropriate values
        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");

        // Act
        Recruiter recruiter = extractor.createActorFromRequest(request, Recruiter.class);

        // Assert
        assertNotNull(recruiter);
        assertEquals(request.getEmail(), recruiter.getEmail());
        assertEquals("encodedPassword", recruiter.getPassword());
        // ... assert other properties
    }

    @Test
    void testCreateActorFromRequestForRegisteredUser() {
        // Arrange
        RegisterRequest request = new RegisterRequest(/* set properties */);
        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");

        // Act
        RegisteredUser registeredUser = extractor.createActorFromRequest(request, RegisteredUser.class);

        // Assert
        assertNotNull(registeredUser);
        assertEquals(request.getName(), registeredUser.getName());
        assertEquals("encodedPassword", registeredUser.getPassword());
        // ... assert other properties
    }

    @Test
    void testCreateActorFromRequestForUnsupportedType() {
        // Arrange
        RegisterRequest request = new RegisterRequest(); // Set properties if necessary

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            extractor.createActorFromRequest(request, Token.class); // Object.class is likely unsupported
        });

        // Assert exception message
        assertTrue(exception.getMessage().contains("Unsupported type: Token"));
    }

    @Test
    void testCreateOfferFromRequest() {
        // Arrange
        OfferRequest offerRequest = new OfferRequest();
        offerRequest.setName("Test Offer");
        offerRequest.setPosition("Developer");
        offerRequest.setKeywords(Set.of("Java, Spring"));
        offerRequest.setCurrency(Currency.USD);
        offerRequest.setSalary("50000"); // Assuming the salary is a String

        // Act
        Offer offer = extractor.createOfferFromRequest(offerRequest);

        // Assert
        assertNotNull(offer);
        assertEquals(offerRequest.getName(), offer.getName());
        assertEquals(offerRequest.getPosition(), offer.getPosition());
        assertEquals(offerRequest.getKeywords(), offer.getKeywords());
        assertEquals(offerRequest.getCurrency(), offer.getCurrency());
        assertEquals(new BigDecimal(offerRequest.getSalary()), offer.getSalary());
    }
}