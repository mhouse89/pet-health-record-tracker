package com.mhouse.project.pethealthrecordtracker.entities;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Data
@Entity
public class HealthRecord {
    private @Id @GeneratedValue(strategy= GenerationType.AUTO) Long id;
    private Date dateOfVisit;
    private String vetName;
    private String vetOfficeName;
    private String descriptionOfVisit;

    public HealthRecord() {}

    public HealthRecord(Date dateOfVisit, String vetName, String vetOfficeName, String descriptionOfVisit) {
        this.dateOfVisit = dateOfVisit;
        this.vetName = vetName;
        this.vetOfficeName = vetOfficeName;
        this.descriptionOfVisit = descriptionOfVisit;
    }
}
