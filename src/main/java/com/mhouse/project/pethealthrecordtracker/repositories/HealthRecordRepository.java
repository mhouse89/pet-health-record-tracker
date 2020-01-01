package com.mhouse.project.pethealthrecordtracker.repositories;

import com.mhouse.project.pethealthrecordtracker.entities.HealthRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HealthRecordRepository extends JpaRepository<HealthRecord, Long> {
}
