package com.mhouse.project.pethealthrecordtracker.exceptions;

public class PetNotFoundException extends RuntimeException {
    public PetNotFoundException(Long id) {
        super("Could not find Pet with id " + id);
    }
}
