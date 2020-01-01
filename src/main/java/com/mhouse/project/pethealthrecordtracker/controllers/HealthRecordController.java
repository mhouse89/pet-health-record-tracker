package com.mhouse.project.pethealthrecordtracker.controllers;

import com.mhouse.project.pethealthrecordtracker.entities.HealthRecord;
import com.mhouse.project.pethealthrecordtracker.entities.Pet;
import com.mhouse.project.pethealthrecordtracker.exceptions.PetNotFoundException;
import com.mhouse.project.pethealthrecordtracker.repositories.HealthRecordRepository;
import com.mhouse.project.pethealthrecordtracker.repositories.PetRepository;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class HealthRecordController {
    private final PetRepository repository;
    private final HealthRecordRepository recordRepository;

    public HealthRecordController(PetRepository repository, HealthRecordRepository recordRepository) {
        this.repository = repository;
        this.recordRepository = recordRepository;
    }

    @PostMapping("pets")
    public EntityModel<Pet> addPet(@RequestBody Pet pet) {
        Pet petToAdd = repository.save(pet);

        return new EntityModel<>(pet,
                linkTo(methodOn(HealthRecordController.class).addPet(pet)).withSelfRel(),
                linkTo(methodOn(HealthRecordController.class).getPetById(petToAdd.getId())).withRel("pets"),
                linkTo(methodOn(HealthRecordController.class).getAllPets()).withRel("pets"));
    }

    @GetMapping("pets")
    public CollectionModel<EntityModel<Pet>> getAllPets() {
        List<EntityModel<Pet>> pets = repository.findAll().stream()
                .map(pet -> new EntityModel<>(pet,
                        linkTo(methodOn(HealthRecordController.class).getAllPets()).withSelfRel(),
                        linkTo(methodOn(HealthRecordController.class).getPetById(pet.getId())).withRel("pets"),
                        linkTo(methodOn(HealthRecordController.class).addPet(pet)).withRel("pets"),
                        linkTo(methodOn(HealthRecordController.class).replacePet(pet.getId(), pet)).withRel("pets")))
                .collect(Collectors.toList());

        return new CollectionModel<>(pets,
                linkTo(methodOn(HealthRecordController.class).getAllPets()).withSelfRel());
    }

    @GetMapping("pets/{id}")
    public EntityModel<Pet> getPetById(@PathVariable Long id)  {
        Pet pet = repository.findById(id)
                .orElseThrow(() -> new PetNotFoundException(id));

        return new EntityModel<>(pet,
                linkTo(methodOn(HealthRecordController.class).getPetById(id)).withSelfRel(),
                linkTo(methodOn(HealthRecordController.class).getAllPets()).withRel("pets"),
                linkTo(methodOn(HealthRecordController.class).addPet(pet)).withRel("pets"),
                linkTo(methodOn(HealthRecordController.class).replacePet(pet.getId(), pet)).withRel("pets"));
    }

    @GetMapping("pets/{id}/records/")
    public List<HealthRecord> getAllRecordsForPet(@PathVariable Long id)  {
        Pet pet = repository.findById(id)
                .orElseThrow(() -> new PetNotFoundException(id));

        return pet.getRecords();
    }

    @GetMapping("records/{id}/")
    public HealthRecord getRecordsById(@PathVariable Long id)  {
        HealthRecord healthRecord = recordRepository.findById(id)
                .orElseThrow(() -> new PetNotFoundException(id));

        return healthRecord;
    }

    @DeleteMapping("records/{id}/")
    public void removeRecordById(@PathVariable Long id)  {
        HealthRecord healthRecord = recordRepository.findById(id)
                .orElseThrow(() -> new PetNotFoundException(id));

        recordRepository.delete(healthRecord);
    }

    @PutMapping("records/{id}/")
    public HealthRecord updateHealthRecord(@PathVariable Long id, HealthRecord updates)  {
        HealthRecord healthRecord = recordRepository.findById(id)
                .orElseThrow(() -> new PetNotFoundException(id));

        healthRecord.setDateOfVisit(updates.getDateOfVisit());
        healthRecord.setDescriptionOfVisit(updates.getDescriptionOfVisit());
        healthRecord.setVetName(updates.getVetName());
        healthRecord.setVetOfficeName(updates.getVetOfficeName());
        return healthRecord;
    }

    @PostMapping("pets/{id}/records/")
    public Pet addHealthRecordToPet(@PathVariable Long id, HealthRecord healthRecord)  {
        Pet pet = repository.findById(id)
                .orElseThrow(() -> new PetNotFoundException(id));

        pet.getRecords().add(healthRecord);
        return pet;
    }

    @PutMapping("pets/{id}")
    public EntityModel<Pet> replacePet(@PathVariable Long id, Pet petUpdates) {
        Pet updatedPet = repository.findById(id)
                .map(pet -> {
                    pet.setName(petUpdates.getName());
                    pet.setBreed(petUpdates.getBreed());
                    pet.setColor(petUpdates.getColor());
                    pet.setHasMicrochip(petUpdates.isHasMicrochip());
                    pet.setMicrochipNumber(petUpdates.getMicrochipNumber());
                    return repository.save(pet);
                }).orElseThrow(() -> new PetNotFoundException(id));

        return new EntityModel<>(updatedPet,
                linkTo(methodOn(HealthRecordController.class).getPetById(id)).withSelfRel(),
                linkTo(methodOn(HealthRecordController.class).getAllPets()).withRel("pets"),
                linkTo(methodOn(HealthRecordController.class).addPet(updatedPet)).withRel("pets"),
                linkTo(methodOn(HealthRecordController.class).replacePet(updatedPet.getId(), updatedPet)).withRel("pets"));
    }

    @DeleteMapping("pets/{id}")
    public void deletePetById(@PathVariable Long id) {
        Pet petToDelete = repository.findById(id).orElseThrow(() -> new PetNotFoundException(id));
        repository.delete(petToDelete);
    }
}
