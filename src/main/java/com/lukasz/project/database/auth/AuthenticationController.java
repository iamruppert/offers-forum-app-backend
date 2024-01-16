package com.lukasz.project.database.auth;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;


    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@Valid @RequestBody RegisterRequest registerRequest) {
        return ResponseEntity.ok(authenticationService.register(registerRequest));
    }

    @PostMapping("/register-admin")
    public ResponseEntity<AuthenticationResponse> registerAdmin(@Valid @RequestBody RegisterRequest registerRequest) {
        return ResponseEntity.ok(authenticationService.registerAdmin(registerRequest));
    }

    @PostMapping("/register-recruiter")
    public ResponseEntity<AuthenticationResponse> registerRecruiter(@Valid @RequestBody RegisterRequest registerRequest) {
        return ResponseEntity.ok(authenticationService.registerRecruiter(registerRequest));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @Valid @RequestBody AuthenticationRequest authenticationRequest) {
        return ResponseEntity.ok(authenticationService.authenticate(authenticationRequest));
    }

}
