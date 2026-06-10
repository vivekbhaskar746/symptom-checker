package com.symptomchecker.repository;

import com.symptomchecker.entity.Pharmacy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PharmacyRepository extends JpaRepository<Pharmacy, Long> {
    List<Pharmacy> findByAddressContainingIgnoreCase(String location);
}