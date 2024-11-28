package com.example.tenpaws.domain.pet.repository;

import com.example.tenpaws.domain.pet.entity.Pet;
import com.example.tenpaws.domain.shelter.entity.Shelter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {
    List<Pet> findByShelter(Shelter shelter);
    Optional<Pet> findByIdAndStatus(Long id, Pet.PetStatus status);

}