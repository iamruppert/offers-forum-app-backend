package com.lukasz.project.service;

import com.lukasz.project.model.Offer;
import com.lukasz.project.model.RegisteredUser;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Set;

public interface RegisteredUserService {

    void updateUser(RegisteredUser user);

    void addOfferToFavourites(Integer offerId, UserDetails userDetails);

    Set<Offer> getFavoriteOffers(UserDetails userDetails);

    void deleteOfferFromFavourites(Integer offerId, UserDetails userDetails);
}
