package com.mhouse.project.pethealthrecordtracker.repositories;

import com.mhouse.project.pethealthrecordtracker.entities.Pet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetRepository extends JpaRepository<Pet, Long> {
}
