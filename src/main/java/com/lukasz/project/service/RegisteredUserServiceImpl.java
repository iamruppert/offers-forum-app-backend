package com.lukasz.project.service;


import com.lukasz.project.model.Offer;
import com.lukasz.project.model.RegisteredUser;
import com.lukasz.project.repository.OfferRepository;
import com.lukasz.project.repository.RegisteredUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RegisteredUserServiceImpl implements RegisteredUserService {

    private final RegisteredUserRepository userRepository;
    private final OfferRepository offerRepository;

    @Override
    public void updateUser(RegisteredUser user) {
        userRepository.save(user);
    }


    @Override
    public void addOfferToFavourites(Integer offerId, UserDetails userDetails)  {

        RegisteredUser user = userRepository
                .findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Offer offer = offerRepository
                .findById(offerId)
                .orElseThrow(() -> new RuntimeException(
                        String.format("Offer with id {%s} not found", offerId)));

        user.getFavoriteOffers().add(offer);
        updateUser(user);

    }

    @Override
    public Set<Offer> getFavoriteOffers(UserDetails userDetails) {
        return userRepository.findByEmail(userDetails.getUsername())
                .map(RegisteredUser::getFavoriteOffers)
                .orElse(Collections.emptySet()); // Return an empty set if user not found
    }

    @Override
    public void deleteOfferFromFavourites(Integer offerId, UserDetails userDetails) {
        RegisteredUser user = userRepository
                .findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Offer offer = offerRepository
                .findById(offerId)
                .orElseThrow(() -> new RuntimeException(
                        String.format("Offer with id {%s} not found", offerId)));

        // Check if the user's favorites contain the offer and remove it
        boolean removed = user.getFavoriteOffers().remove(offer);
        if (removed) {
            updateUser(user);
        } else {
            throw new RuntimeException(
                    String.format("Offer with id {%s} is not in the user's favorites", offerId));
        }
    }

}
