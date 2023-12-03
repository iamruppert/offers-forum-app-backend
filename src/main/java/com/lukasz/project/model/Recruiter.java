package com.lukasz.project.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@With
@Data
@DiscriminatorValue(value = "recruiter")
public class Recruiter extends User {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;

    @OneToMany(mappedBy = "recruiter", fetch = FetchType.EAGER)
    private Set<Offer> createdOffers;

    @Override
    public Recruiter withId(Integer id) {
        setId(id);
        return this;
    }

    @Override
    public Recruiter withIdentifier(String identifier) {
        setIdentifier(identifier);
        return this;
    }

    @Override
    public Recruiter withName(String name) {
        setName(name);
        return this;
    }

    @Override
    public Recruiter withSurname(String surname) {
        setSurname(surname);
        return this;
    }

    @Override
    public Recruiter withPesel(String pesel) {
        setPesel(pesel);
        return this;
    }

    @Override
    public Recruiter withCountry(String country) {
        setCountry(country);
        return this;
    }

    @Override
    public Recruiter withUsername(String username) {
        setUsername(username);
        return this;
    }

    @Override
    public Recruiter withEmail(String email) {
        setEmail(email);
        return this;
    }

    @Override
    public Recruiter withPassword(String password) {
        setPassword(password);
        return this;
    }

    @Override
    public Recruiter withCreationDate(OffsetDateTime creationDate) {
        setCreationDate(creationDate);
        return this;
    }

    @Override
    public Recruiter withRole(Role role) {
        setRole(role);
        return this;
    }


}