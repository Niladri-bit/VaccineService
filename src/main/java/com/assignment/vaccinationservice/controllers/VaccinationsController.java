package com.assignment.vaccinationservice.controllers;

import java.util.List;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.assignment.vaccinationservice.DTOs.MarkVaccinatedDTO;
import com.assignment.vaccinationservice.DTOs.StudentRegisterDTO;
import com.assignment.vaccinationservice.DTOs.VaccinationDTO;
import com.assignment.vaccinationservice.DTOs.VaccinationReportDTO;
import com.assignment.vaccinationservice.DTOs.VaccinationSummaryDTO;
import com.assignment.vaccinationservice.entities.VaccinationEntity;
import com.assignment.vaccinationservice.service.VaccinationService;

import jakarta.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/vaccinations")
public class VaccinationsController {
	
	@Autowired
	private VaccinationService vaccinationService;

	@PostMapping
    public ResponseEntity<VaccinationDTO> addVaccinationDrive(@RequestBody VaccinationDTO vaccination,HttpServletRequest request,@RequestHeader("Authorization") String token) throws BadRequestException {
		
		VaccinationDTO createdVaccinationDrive = vaccinationService.createVaccinationDrive(vaccination, token);
        return ResponseEntity.status(201).body(createdVaccinationDrive);
    }
	
	@GetMapping
    public List<VaccinationDTO> getAllVaccinationDrives(
            @RequestParam(value = "upcomingOnly", required = false, defaultValue = "false") boolean upcomingOnly,
            @RequestParam(value = "class", required = false) String className,
            @RequestParam(value = "vaccineName", required = false) String vaccineName,
            @RequestHeader("Authorization") String token
    ) {
        return vaccinationService.getAllVaccinationDrives(upcomingOnly, className, vaccineName, token);
    }
	
	@GetMapping("/{id}")
	public ResponseEntity<VaccinationDTO> getVaccinationDriveById(@PathVariable Long id,@RequestHeader("Authorization") String token) {
	    VaccinationDTO vaccinationDTO = vaccinationService.getDriveById(id,token);
	    return ResponseEntity.ok(vaccinationDTO);
	}

	 @PutMapping("/{driveId}")
	    public ResponseEntity<String> updateVaccinationDrive(
	            @PathVariable("driveId") Long driveId,
	            @RequestBody VaccinationDTO vaccinationDriveDTO,@RequestHeader("Authorization") String token) {
		 vaccinationService.updateVaccinationDrive(driveId, vaccinationDriveDTO,token);
		 return ResponseEntity.ok("Vaccination drive updated successfully.");
	 }
	 
	 @GetMapping("/report")
	    public ResponseEntity<List<VaccinationReportDTO>> getVaccinationReport(
	            @RequestParam(required = false) String studentClass,
	            @RequestParam(required = false) String vaccineName,
	            @RequestParam(defaultValue = "0") int page,
	            @RequestParam(defaultValue = "10") int size,
	            @RequestHeader("Authorization") String token) {

		 List<VaccinationReportDTO> report = vaccinationService.createVaccinationReport(token, vaccineName, studentClass, page, size);
		   
	        return ResponseEntity.ok(report);
	    }
	 
	 @GetMapping("/summary")
	 public ResponseEntity<VaccinationSummaryDTO> getVaccinationSummary(
	     @RequestParam(required = false) Long vaccineId) {

	     VaccinationSummaryDTO summary = vaccinationService.getVaccinationSummary(vaccineId);
	     return ResponseEntity.ok(summary);
	 }
	 
	 @PatchMapping("/mark-vaccinated")
	 public ResponseEntity<String> markVaccinated(@RequestBody MarkVaccinatedDTO markVaccinatedDTO) {
	     vaccinationService.markStudentAsVaccinated(markVaccinatedDTO.getStudentId(), markVaccinatedDTO.getDriveId());
	     return ResponseEntity.ok("Student marked as vaccinated successfully.");
	 }
	 
	 @DeleteMapping("/students/{studentId}")
	    public ResponseEntity<String> deleteVaccinationsForStudent(@PathVariable("studentId") Long studentId) {
	        try {
	            vaccinationService.deleteVaccinationsByStudentId(studentId);
	            return ResponseEntity.status(HttpStatus.OK).body("Vaccinations deleted successfully.");
	        } catch (Exception e) {
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete vaccinations.");
	        }
	    }
	 
	 @GetMapping("/assigned/{driveId}")
	    public List<StudentRegisterDTO> getAssignedStudents(@PathVariable Long driveId,@RequestHeader("Authorization") String token) {
	        return vaccinationService.getAssignedStudentsByDriveId(driveId,token);
	    }

}
