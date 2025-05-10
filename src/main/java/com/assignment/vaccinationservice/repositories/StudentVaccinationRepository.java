package com.assignment.vaccinationservice.repositories;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.assignment.vaccinationservice.entities.StudentVaccinationEntity;
import com.assignment.vaccinationservice.entities.VaccinationEntity;


@Repository
public interface StudentVaccinationRepository extends JpaRepository<StudentVaccinationEntity, UUID>{

	long countById_DriveIdAndStatus(Long driveId, String status);

	List<StudentVaccinationEntity> findByIdDriveId(Long id);
	
	Optional<StudentVaccinationEntity> findByIdDriveIdAndIdStudentId(Long driveId, Long studentId);

	List<StudentVaccinationEntity> findByIdStudentId(Long studentId);

	List<StudentVaccinationEntity> findByIdDriveIdAndStatus(Long driveId, String status);

	
}
