package com.lukasz.project.controller;

import com.lukasz.project.model.Offer;
import com.lukasz.project.service.RegisteredUserServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/registeredUser")
@AllArgsConstructor
public class RegisteredUserController {

    private final RegisteredUserServiceImpl userService;

    @PostMapping("/addToFavourite/{offerId}")
    public ResponseEntity<String> addOfferToFavourite(
            @PathVariable Integer offerId,
            @AuthenticationPrincipal UserDetails userDetails){
        userService.addOfferToFavourites(offerId, userDetails);
        return ResponseEntity.ok(String.format("Offer with id {%s} added to favourites successfully", offerId));
    }

    @GetMapping("/getFavourites/")
    public ResponseEntity<Set<Offer>> getFavourites(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        Set<Offer> favoriteOffers = userService.getFavoriteOffers(userDetails);
        return ResponseEntity.ok(favoriteOffers);
    }

    @DeleteMapping("/deleteFromFavourites/{offerId}")
    public ResponseEntity<String> deleteOfferFromFavourites(
            @PathVariable Integer offerId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        userService.deleteOfferFromFavourites(offerId, userDetails);
        return ResponseEntity.ok("");
    }
}

