package com.example.tenpaws.pet.repository;

import com.example.tenpaws.pet.entity.Pet;
import com.example.tenpaws.shelter.entity.Shelter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {
    List<Pet> findByShelter(Shelter shelter);
}