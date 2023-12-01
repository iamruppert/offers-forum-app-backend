package com.lukasz.project.repository;

import com.lukasz.project.entity.Recruiter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RecruiterRepository extends JpaRepository<Recruiter,Long> {
    Optional<Recruiter> findByEmail(String email);
    boolean existsByEmail(String email);
}
