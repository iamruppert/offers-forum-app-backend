package com.lukasz.project.integration;

import com.lukasz.project.dto.OfferRequest;
import com.lukasz.project.model.Currency;
import com.lukasz.project.model.Offer;
import com.lukasz.project.model.Recruiter;
import com.lukasz.project.model.User;
import com.lukasz.project.repository.OfferRepository;
import com.lukasz.project.repository.UserRepository;
import com.lukasz.project.service.OfferServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
public class OfferServiceIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:16.0");


    @Autowired
    private OfferRepository offerRepository;

    @Autowired
    private UserRepository userRepository;


    @Autowired
    private OfferServiceImpl offerService;

    @Test
    @DirtiesContext
    public void shouldCreateOfferSuccessfully() {
        OfferRequest offerRequest = new OfferRequest();
        offerRequest.setEmail("recruiter@example.com");
        offerRequest.setName("Software Engineer");
        offerRequest.setPosition("Java Developer");
        offerRequest.setKeywords(Set.of("Java, Spring"));
        offerRequest.setSalary("80000");
        offerRequest.setCurrency(Currency.EURO);

        User recruiter = new Recruiter();
        recruiter.setEmail("recruiter@example.com");
        userRepository.save(recruiter);

        offerService.createOffer(offerRequest);

        List<Offer> offers = offerRepository.findAll();
        assertEquals(1, offers.size());

    }

    @Test
    @DirtiesContext
    void shouldDeleteOfferSuccessfully(){
        OfferRequest offerRequest = new OfferRequest();
        offerRequest.setEmail("recruiter@example.com");
        offerRequest.setName("Software Engineer");
        offerRequest.setPosition("Java Developer");
        offerRequest.setKeywords(Set.of("Java, Spring"));
        offerRequest.setSalary("80000");
        offerRequest.setCurrency(Currency.EURO);

        User recruiter = new Recruiter();
        recruiter.setEmail("recruiter@example.com");
        userRepository.save(recruiter);

        offerService.createOffer(offerRequest);
        offerService.deleteOffer(1);

        List<Offer> offers = offerRepository.findAll();
        assertEquals(0, offers.size());
    }

    @Test
    @DirtiesContext
    void shouldUpdateOfferSuccessfully(){

        OfferRequest offerRequest = new OfferRequest();
        offerRequest.setEmail("recruiter@example.com");
        offerRequest.setName("Software Engineer");
        offerRequest.setPosition("Java Developer");
        offerRequest.setKeywords(Set.of("Java, Spring"));
        offerRequest.setSalary("80000");
        offerRequest.setCurrency(Currency.EURO);

        User recruiter = new Recruiter();
        recruiter.setEmail("recruiter@example.com");
        userRepository.save(recruiter);

        offerService.createOffer(offerRequest);
        Optional<Offer> offerToUpdate = offerRepository.findById(1);
        if(offerToUpdate.isPresent()){
            Offer offer = offerToUpdate.get();
            offer.setCurrency(Currency.USD);
            offer.setName("Software Engineer 2");
            offerRepository.save(offer);
        }

        Optional<Offer> offer = offerRepository.findById(1);
        if(offer.isPresent()){
            Offer offer1 = offer.get();
            Assertions.assertEquals("Software Engineer 2",offer1.getName());
            Assertions.assertEquals(Currency.USD,offer1.getCurrency());
        }

    }

}
