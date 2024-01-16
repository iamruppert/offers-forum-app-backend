package com.lukasz.project.unit;

import com.lukasz.project.database.auth.Extractor;
import com.lukasz.project.dto.OfferRequest;
import com.lukasz.project.model.Offer;
import com.lukasz.project.model.Recruiter;
import com.lukasz.project.model.User;
import com.lukasz.project.repository.OfferRepository;
import com.lukasz.project.repository.UserRepository;
import com.lukasz.project.service.OfferServiceImpl;
import com.lukasz.project.validator.MyValidationException;
import com.lukasz.project.validator.ObjectValidatorImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OfferServiceUnitTest {

    @Mock
    private OfferRepository offerRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private Extractor extractor;

    @Mock
    private ObjectValidatorImpl<OfferRequest> validator;

    @InjectMocks
    private OfferServiceImpl offerService;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldCreateOfferSuccessfully() {
        OfferRequest offerRequest = new OfferRequest();
        User recruiter = new Recruiter();
        Set<String> violations = Collections.emptySet();

        when(validator.validate(offerRequest)).thenReturn(violations);
        when(extractor.createOfferFromRequest(offerRequest)).thenReturn(new Offer());
        when(userRepository.findByEmail(offerRequest.getEmail())).thenReturn(Optional.of(recruiter));

        assertDoesNotThrow(() -> offerService.createOffer(offerRequest));

        verify(validator, times(1)).validate(offerRequest);
        verify(extractor, times(1)).createOfferFromRequest(offerRequest);
        verify(userRepository, times(1)).findByEmail(offerRequest.getEmail());
        verify(offerRepository, times(1)).save(any(Offer.class));
    }

    @Test
    void shouldCreateOfferWithValidationErrors() {
        OfferRequest offerRequest = new OfferRequest();
        Set<String> violations = new HashSet<>();
        violations.add("Validation error");

        when(validator.validate(offerRequest)).thenReturn(violations);

        MyValidationException exception = assertThrows(MyValidationException.class,
                () -> offerService.createOffer(offerRequest));

        assertEquals(violations, exception.getValidationErrors());

        verify(validator, times(1)).validate(offerRequest);
        verify(extractor, never()).createOfferFromRequest(any(OfferRequest.class));
        verify(userRepository, never()).findByEmail(anyString());
        verify(offerRepository, never()).save(any(Offer.class));
    }

    @Test
    void testCreateOfferWithUserNotFound() {
        OfferRequest offerRequest = new OfferRequest();
        Set<String> violations = Collections.emptySet();

        when(validator.validate(offerRequest)).thenReturn(violations);
        when(userRepository.findByEmail(offerRequest.getEmail())).thenReturn(Optional.empty());
        when(extractor.createOfferFromRequest(any())).thenReturn(new Offer());


        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> offerService.createOffer(offerRequest));

        assertEquals("User with email: [null] not found", exception.getMessage());

        verify(validator, times(1)).validate(offerRequest);
        verify(extractor, only()).createOfferFromRequest(any());
        verify(userRepository, times(1)).findByEmail(offerRequest.getEmail());
        verify(offerRepository, never()).save(any(Offer.class));
    }



}