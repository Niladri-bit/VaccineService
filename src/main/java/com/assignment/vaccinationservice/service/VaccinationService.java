package com.assignment.vaccinationservice.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.coyote.BadRequestException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.assignment.vaccinationservice.DTOs.StudentRegisterDTO;
import com.assignment.vaccinationservice.DTOs.VaccinationDTO;
import com.assignment.vaccinationservice.DTOs.VaccinationReportDTO;
import com.assignment.vaccinationservice.DTOs.VaccinationSummaryDTO;
import com.assignment.vaccinationservice.entities.StudentVaccinationEntity;
import com.assignment.vaccinationservice.entities.StudentVaccinationId;
import com.assignment.vaccinationservice.entities.VaccinationEntity;
import com.assignment.vaccinationservice.exceptions.AlreadyVaccinatedException;
import com.assignment.vaccinationservice.exceptions.DriveCreationConflictException;
import com.assignment.vaccinationservice.exceptions.DriveCreationFailedException;
import com.assignment.vaccinationservice.exceptions.DriveNotFoundException;
import com.assignment.vaccinationservice.exceptions.DriveUpdateFailedException;
import com.assignment.vaccinationservice.exceptions.UnauthorizedUserException;
import com.assignment.vaccinationservice.exceptions.VaccinationEntityNotFoundException;
import com.assignment.vaccinationservice.repositories.StudentVaccinationRepository;
import com.assignment.vaccinationservice.repositories.VaccinationDriveRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;

@Component
public class VaccinationService {
	
	@Autowired
    private ModelMapper modelMapper;
    @Autowired
    private TokenValidationService tokenValidationService;
    @Autowired
    private UserCommunicationService userCommunicationService;
    @Autowired
    private VaccinationDriveRepository vaccinationDriveRepository;
    @Autowired
    private StudentVaccinationRepository studentVaccinationRepository;
	
	@Transactional
	public VaccinationDTO createVaccinationDrive(VaccinationDTO vaccine,String token)  {
		//getTokenInformation(request1);
	    // 1. Validate drive date
		
		if (vaccine.getDriveDate() == null ) {
	        throw new DriveCreationFailedException("Drive date is required. ");
	    }
		
	    if ( vaccine.getDriveDate().isBefore(LocalDate.now().plusDays(15))) {
	        throw new DriveCreationFailedException("Drive date must be at least 15 days in the future. ");
	    }

	    // 2. Validate vaccine name
	    if (vaccine.getVaccineName() == null || vaccine.getVaccineName().isBlank()) {
	        throw new DriveCreationFailedException("Vaccine name is required.");
	    }

	    if (vaccine.getVaccineName() == null || vaccine.getVaccineName().isBlank()) {
	        throw new DriveCreationFailedException("Vaccine name is required.");
	    }
	    
	 // 3. Validate available doses (should not be null or zero)
	    if (vaccine.getAvailableDoses() == null || vaccine.getAvailableDoses() <= 0) {
	        throw new DriveCreationFailedException("Available doses must be greater than 0.");
	    }

	    // 4. Validate applicable classes (should not be null, empty, or blank)
	    if (vaccine.getApplicableClasses() == null || vaccine.getApplicableClasses().trim().isEmpty()) {
	        throw new DriveCreationFailedException("Applicable classes cannot be null, empty, or blank.");
	    }

	   
	    List<StudentRegisterDTO> students = userCommunicationService.fetchStudentsByClasses(vaccine.getApplicableClasses(), token);
	    if (students.isEmpty()) {
	        throw new DriveCreationFailedException("No students found for the specified classes.");
	    }
	    if (vaccine.getAvailableDoses() < students.size()) {
	        throw new DriveCreationFailedException("Available doses are less than total number of eligible students.");
	    }

	    // 4. Check dose sufficiency
	    if (vaccine.getAvailableDoses() < students.size()) {
	        throw new DriveCreationFailedException("Available doses are less than total number of eligible students.");
	    }

	    // 5. Prevent duplicate drives
	    
	    
	    List<VaccinationEntity> existingDrives = vaccinationDriveRepository.findByVaccineNameAndDriveDate(
	    	    vaccine.getVaccineName(), vaccine.getDriveDate()
	    	);

	    	if (!existingDrives.isEmpty()) {
	    	    Set<String> newClasses = Arrays.stream(vaccine.getApplicableClasses().split(","))
	    	            .map(String::trim)
	    	            .filter(s -> !s.isEmpty())
	    	            .collect(Collectors.toSet());

	    	    for (VaccinationEntity existing : existingDrives) {
	    	        Set<String> existingClasses = existing.getApplicableClasses();

	    	        // Check if there is any common class
	    	        boolean clash = existingClasses.stream().anyMatch(newClasses::contains);

	    	        if (clash) {
	    	            throw new DriveCreationConflictException("A vaccination drive for the given vaccine, date, and class already exists.");
	    	        }
	    	    }
	    	}

	    
	    Set<String> classSet = Arrays.stream(vaccine.getApplicableClasses().split(","))
	    	    .map(String::trim)
	    	    .filter(s -> !s.isEmpty())
	    	    .collect(Collectors.toSet());


	    // 6. Save drive
	    VaccinationEntity drive = new VaccinationEntity();
	    drive.setVaccineName(vaccine.getVaccineName());
	    drive.setDriveDate(vaccine.getDriveDate());
	    drive.setAvailableDoses(vaccine.getAvailableDoses());
	    drive.setApplicableClasses(classSet);
	    vaccinationDriveRepository.save(drive);
	    

	    // 7. Pre-assign eligible students
	    List<StudentVaccinationEntity> assignments = students.stream()
	        .map(student -> {
	        	StudentVaccinationEntity record = new StudentVaccinationEntity();
	            record.setId(new StudentVaccinationId(student.getId(),drive.getId()));
	            record.setVaccinationDate(vaccine.getDriveDate());
	            record.setStatus("ASSIGNED"); 
	            return record;
	        })
	        .toList();

	    studentVaccinationRepository.saveAll(assignments);

	    return modelMapper.map(drive, VaccinationDTO.class);
	}
	
	
	public List<VaccinationDTO> getAllVaccinationDrives(boolean upcomingOnly, String className, String vaccineName, String token) {
	    

	    // Fetch all drives
	    List<VaccinationEntity> drives = vaccinationDriveRepository.findAll();

	    // Stream for filtering
	    Stream<VaccinationEntity> stream = drives.stream();

	    if (upcomingOnly) {
	        LocalDate today = LocalDate.now();
	        LocalDate thirtyDaysLater = today.plusDays(30);

	        stream = stream.filter(drive -> 
	            !drive.getDriveDate().isBefore(today) && 
	            !drive.getDriveDate().isAfter(thirtyDaysLater)
	        );
	    }

	    if (className != null && !className.isBlank()) {
	        stream = stream.filter(drive -> drive.getApplicableClasses().contains(className));
	    }

	    if (vaccineName != null && !vaccineName.isBlank()) {
	        stream = stream.filter(drive -> drive.getVaccineName().equalsIgnoreCase(vaccineName));
	    }

	    List<VaccinationEntity> filteredDrives = stream.toList();

	    return filteredDrives.stream()
	            .map(drive -> modelMapper.map(drive, VaccinationDTO.class))
	            .toList();
	}

	public VaccinationDTO getDriveById(Long id,String token) {
	    VaccinationEntity drive = vaccinationDriveRepository.findById(id)
	            .orElseThrow(() -> new DriveNotFoundException("Vaccination drive not found with id: " + id));
	    
	    return modelMapper.map(drive, VaccinationDTO.class);
	}
	
	@Transactional
	public VaccinationDTO updateVaccinationDrive(Long driveId, VaccinationDTO updatedDrive,String token) {
	    // 1. Fetch the existing drive
	    VaccinationEntity existingDrive = vaccinationDriveRepository.findById(driveId)
	            .orElseThrow(() -> new DriveNotFoundException("Vaccination drive not found for ID: " + driveId));

	    // 2. Only allow updating if the drive date is still upcoming
	    if (existingDrive.getDriveDate().isBefore(LocalDate.now())) {
	        throw new DriveUpdateFailedException("Cannot update a vaccination drive that has already occurred.");
	    }

	    // 3. Prevent updating vaccine name and classes
	    if (!existingDrive.getVaccineName().equals(updatedDrive.getVaccineName())) {
	    	System.out.println(existingDrive.getVaccineName());
	    	System.out.println(updatedDrive.getVaccineName());
	        throw new DriveUpdateFailedException("Vaccine name cannot be changed.");
	    }

	    if (!existingDrive.getApplicableClasses().equals(
	            Arrays.stream(updatedDrive.getApplicableClasses().split(","))
	                    .map(String::trim)
	                    .filter(s -> !s.isEmpty())
	                    .collect(Collectors.toSet())
	    )) {
	        throw new DriveUpdateFailedException("Applicable classes cannot be changed.");
	    }

	    // 4. Validate new drive date (should not be past)
	    if (updatedDrive.getDriveDate() != null) {
	        if (updatedDrive.getDriveDate().isBefore(LocalDate.now())) {
	            throw new DriveUpdateFailedException("Drive date cannot be in the past.");
	        }
	        existingDrive.setDriveDate(updatedDrive.getDriveDate());
	    }

	    // 5. Validate available doses
	    if (updatedDrive.getAvailableDoses() != null) {
	        // Find total students already assigned
	        long assignedStudentsCount = studentVaccinationRepository.countById_DriveIdAndStatus(
	                existingDrive.getId(), "ASSIGNED"
	        );

	        if (updatedDrive.getAvailableDoses() < assignedStudentsCount) {
	            throw new DriveUpdateFailedException("Available doses cannot be less than the number of students already assigned (" + assignedStudentsCount + ").");
	        }

	        existingDrive.setAvailableDoses(updatedDrive.getAvailableDoses());
	    }

	    // 6. Save updated drive
	    vaccinationDriveRepository.save(existingDrive);

	    return modelMapper.map(existingDrive, VaccinationDTO.class);
	}
	
	
//	public Page<VaccinationReportDTO> getVaccinationReport(String studentClass, String vaccineName, int page, int size, String token) {
//
//	    Pageable pageable = PageRequest.of(page, size);
//	    System.out.println("hee");
//	    // 1. Fetch vaccination records filtered by student class and vaccine name
//	    Page<StudentVaccinationEntity> vaccinationRecords = studentVaccinationRepository.findByStudentClassAndVaccineName(studentClass, vaccineName, pageable);
//	    System.out.println("here");
//	    System.out.println(vaccinationRecords);
//	    if (vaccinationRecords.isEmpty()) {
//	        return Page.empty();
//	    }
//
//	    // 2. Extract student IDs
//	    Set<Long> studentIds = vaccinationRecords.stream()
//	            .map(record -> record.getId().getStudentId())
//	            .collect(Collectors.toSet());
//
//	    // 3. Fetch student details from User Service
//	    List<StudentRegisterDTO> students = new ArrayList<>();
//	    for (Long id : studentIds) {
//	        StudentRegisterDTO student = userCommunicationService.fetchStudentById(id, token);
//	        if (student != null) {
//	            students.add(student);
//	        }
//	    }
//
//	    // 4. Map studentId → StudentRegisterDTO
//	    Map<Long, StudentRegisterDTO> studentMap = students.stream()
//	            .collect(Collectors.toMap(StudentRegisterDTO::getId, Function.identity()));
//
//	    // 5. Build report
//	    Page<VaccinationReportDTO> reportPage = vaccinationRecords.map(record -> {
//	        StudentRegisterDTO student = studentMap.get(record.getId().getStudentId());
//
//	        VaccinationReportDTO dto = new VaccinationReportDTO();
//	        dto.setFirstName(student.getFirstName());
//	        dto.setLastName(student.getLastName());
//	        dto.setStudentClass(student.getStudentClass());
//	        dto.setEmail(student.getEmail());
//	        dto.setVaccineName(vaccineName);  // Vaccine name will be passed here
//	        dto.setVaccinationDate(record.getVaccinationDate());
//
//	        // Status mapping
//	        if ("VACCINATED".equalsIgnoreCase(record.getStatus())) {
//	            dto.setVaccinationStatus("VACCINATED");
//	        } else {
//	            dto.setVaccinationStatus("PENDING");
//	        }
//	        System.out.println(dto);
//	        return dto;
//	    }
//	    );
//	    
//	    System.out.println(reportPage);
//
//	    return reportPage;
//	}

	

	private String getVaccineNameByDriveId(Long driveId) {
	    VaccinationEntity drive = vaccinationDriveRepository.findById(driveId)
	            .orElseThrow(() -> new DriveNotFoundException("Vaccination drive not found"));
	    return drive.getVaccineName();
	}


	public List<VaccinationEntity> getVaccines(String vaccineName) {
	    if (vaccineName != null) {
	        return vaccinationDriveRepository.findByVaccineName(vaccineName);
	    } else {
	        return vaccinationDriveRepository.findAll();
	    }
	}
	

	public List<VaccinationReportDTO> createVaccinationReport(String token, String vaccineName, String studentClass, int page, int size) {
	    // Log incoming parameters
	    System.out.println("Received vaccineName: " + vaccineName);
	    System.out.println("Received studentClass: " + studentClass);
	    System.out.println("Page: " + page + ", Size: " + size);

	    // Fetch all vaccines
	    List<VaccinationEntity> vaccines = vaccinationDriveRepository.findAll();
	    List<VaccinationReportDTO> vaccinationReports = new ArrayList<>();
	    
	    for (VaccinationEntity vaccine : vaccines) {
	        
	        // Filter by vaccine name if provided
	        if (vaccineName != null && !vaccineName.equals(vaccine.getVaccineName())) {
	            continue; // Skip this vaccine if it doesn't match the vaccineName query param
	        }

	        // Debug: log the vaccine's applicable classes
	        System.out.println("Vaccine applicable classes: " + vaccine.getApplicableClasses());

	        // Fetch vaccination records for the current vaccine
	        List<StudentVaccinationEntity> studentVaccinationEntities = studentVaccinationRepository.findByIdDriveId(vaccine.getId());

	        for (StudentVaccinationEntity vaccinationEntity : studentVaccinationEntities) {
	            // Fetch student details based on vaccination record
	            StudentRegisterDTO student = userCommunicationService.fetchStudentById(vaccinationEntity.getId().getStudentId(), token);

	            // Filter by student class if provided
	            if (studentClass != null && !student.getStudentClass().equals(studentClass)){
	                continue; // Skip this record if student's class doesn't match
	            }
	            
	            // Create a report DTO
	            VaccinationReportDTO report = new VaccinationReportDTO();
	            report.setEmail(student.getEmail());
	            report.setFirstName(student.getFirstName());
	            report.setLastName(student.getLastName());
	            report.setStudentClass(student.getStudentClass());
	            report.setVaccinationDate(vaccinationEntity.getVaccinationDate());
	            report.setVaccinationStatus(vaccinationEntity.getStatus());
	            report.setVaccineName(vaccine.getVaccineName());
	            report.setStudentId(student.getId());
	            report.setVaccineId(vaccine.getId());

	            // Add the report to the list
	            vaccinationReports.add(report);
	        }
	    }
	    
	    // Now apply manual pagination
	    int startIndex = page * size;
	    int endIndex = Math.min(startIndex + size, vaccinationReports.size());
	    
	    if (startIndex >= vaccinationReports.size()) {
	        return new ArrayList<>(); // Return empty list if page is out of bounds
	    }

	    return vaccinationReports.subList(startIndex, endIndex);
	}

	public VaccinationSummaryDTO getVaccinationSummary(Long vaccineId) {
	    List<StudentVaccinationEntity> records;
	    
	    if (vaccineId != null) {
	        records = studentVaccinationRepository.findByIdDriveId(vaccineId);
	    } else {
	        records = studentVaccinationRepository.findAll();
	    }

	    int totalAssigned = records.size();
	    int totalVaccinated = 0;

	    for (StudentVaccinationEntity record : records) {
	        if (record.getStatus().equalsIgnoreCase("VACCINATED")) {
	            totalVaccinated++;
	        }
	    }

	    double percentage = totalAssigned == 0 ? 0 : (totalVaccinated * 100.0) / totalAssigned;

	    return new VaccinationSummaryDTO(totalAssigned, totalVaccinated, percentage);
	}
	
	
	public void markStudentAsVaccinated(Long studentId, Long driveId) {
	    Optional<StudentVaccinationEntity> optionalEntity = studentVaccinationRepository.findByIdDriveIdAndIdStudentId( driveId,studentId);
	    System.out.println(optionalEntity);
	    if (optionalEntity.isPresent()) {
	    	
	    	if ("VACCINATED".equals(optionalEntity.get().getStatus())) {
	            throw new AlreadyVaccinatedException("Student is already vaccinated");
	        }
	    	
	        StudentVaccinationEntity entity = optionalEntity.get();
	        entity.setStatus("VACCINATED");
	        studentVaccinationRepository.save(entity);
	    } else {
	        throw new VaccinationEntityNotFoundException("Vaccination record not found for given student and drive.");
	    }

	}
	
	 
	    public void deleteVaccinationsByStudentId(Long studentId) {
	        // Fetch all vaccinations for the student
	        List<StudentVaccinationEntity> vaccinations = studentVaccinationRepository.findByIdStudentId(studentId);

	        if (vaccinations.isEmpty()) {
	           return;
	        }

	        // Deleting all vaccinations for the student
	        studentVaccinationRepository.deleteAll(vaccinations);
	    }

	    public List<StudentRegisterDTO> getAssignedStudentsByDriveId(Long driveId,String token) {
	    	List<StudentVaccinationEntity> studentVaccinations = studentVaccinationRepository.findByIdDriveIdAndStatus(driveId, "ASSIGNED");
	        List<StudentRegisterDTO> students = new ArrayList<StudentRegisterDTO>();
	        for (StudentVaccinationEntity vaccinationEntity : studentVaccinations) {
	            students.add(userCommunicationService.fetchStudentById(vaccinationEntity.getId().getStudentId(), token));
	        }
	        return students;
	    }
	    
	}




