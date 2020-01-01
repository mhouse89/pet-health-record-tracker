package com.mhouse.project.pethealthrecordtracker;

import com.mhouse.project.pethealthrecordtracker.entities.HealthRecord;
import com.mhouse.project.pethealthrecordtracker.entities.Pet;
import com.mhouse.project.pethealthrecordtracker.repositories.HealthRecordRepository;
import com.mhouse.project.pethealthrecordtracker.repositories.PetRepository;
import org.aspectj.lang.annotation.After;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class HealthRecordRepositoryTests {

    @Autowired private TestEntityManager entityManager;
    @Autowired private PetRepository petRepository;
    @Autowired private HealthRecordRepository healthRecordRepository;

    @Test
    void findByIdReturnsPetWithHealthRecord() {
        Date today = Calendar.getInstance().getTime();
        HealthRecord vetVisit = new HealthRecord(today, "Dr. Ashley S", "Cats Only VC",
                "Checkup");

        entityManager.persist(vetVisit);

        Pet pet = new Pet("Leo", "American Shorthair", "Gray Striped", Arrays.asList(vetVisit),
                true, "77777");

        entityManager.persist(pet);

        entityManager.flush();

        Optional<Pet> result = petRepository.findById(pet.getId());

        assertThat(result.get()).isEqualTo(pet);
        assertThat(result.get().getRecords().get(0)).isEqualTo(vetVisit);
    }
}
