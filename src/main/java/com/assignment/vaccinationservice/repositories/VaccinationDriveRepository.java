package com.assignment.vaccinationservice.repositories;


import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.assignment.vaccinationservice.entities.VaccinationEntity;


@Repository
public interface VaccinationDriveRepository extends JpaRepository<VaccinationEntity, Long>{

	 boolean existsByVaccineNameAndDriveDate(String vaccineName, LocalDate driveDate);

	List<VaccinationEntity> findByVaccineNameAndDriveDate(String vaccineName, LocalDate driveDate);

	List<VaccinationEntity> findByVaccineName(String vaccineName);
	
	
}
