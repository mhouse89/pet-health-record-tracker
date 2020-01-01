package com.mhouse.project.pethealthrecordtracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class })
public class PetHealthRecordTrackerApplication {

	public static void main(String[] args) {
		SpringApplication.run(PetHealthRecordTrackerApplication.class, args);
	}

}
