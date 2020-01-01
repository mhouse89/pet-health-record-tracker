package com.mhouse.project.pethealthrecordtracker.entities;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
public class Pet {
    private @Id @GeneratedValue(strategy= GenerationType.AUTO) Long id;
    private String name;
    private String breed;
    private String color;
    private boolean hasMicrochip;
    private String microchipNumber;
    @OneToMany private List<HealthRecord> records;

    public Pet(){};

    public Pet(String name, String breed, String color, List<HealthRecord> records, boolean hasMicrochip, String microchipNumber) {
        this.name = name;
        this.breed = breed;
        this.color = color;
        this.records = records;
        this.hasMicrochip = hasMicrochip;
        this.microchipNumber = microchipNumber;
    }
}
