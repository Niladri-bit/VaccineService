package com.assignment.vaccinationservice.controllers;

import java.util.List;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.assignment.vaccinationservice.DTOs.MarkVaccinatedDTO;
import com.assignment.vaccinationservice.DTOs.StudentRegisterDTO;
import com.assignment.vaccinationservice.DTOs.VaccinationDTO;
import com.assignment.vaccinationservice.DTOs.VaccinationReportDTO;
import com.assignment.vaccinationservice.DTOs.VaccinationSummaryDTO;
import com.assignment.vaccinationservice.service.VaccinationService;

import jakarta.servlet.http.HttpServletRequest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/vaccinations")
@Tag(name = "Vaccination Drive Management", description = "APIs for managing vaccination drives and student registrations.")
public class VaccinationsController {

    @Autowired
    private VaccinationService vaccinationService;

    @PostMapping
    @Operation(summary = "Create a new vaccination drive", description = "Creates a new vaccination drive and returns the created vaccination drive data.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Vaccination drive created successfully"),
        @ApiResponse(responseCode = "400", description = "Validation failed or bad request"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<VaccinationDTO> addVaccinationDrive(
            @RequestBody VaccinationDTO vaccination,
            HttpServletRequest request,
            @RequestHeader("Authorization") String token) throws BadRequestException {
        VaccinationDTO created = vaccinationService.createVaccinationDrive(vaccination, token);
        return ResponseEntity.status(201).body(created);
    }

    @GetMapping
    @Operation(summary = "Get all vaccination drives", description = "Retrieves a list of all vaccination drives based on filters.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Successfully retrieved drives"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public List<VaccinationDTO> getAllVaccinationDrives(
            @RequestParam(value = "upcomingOnly", required = false, defaultValue = "false") boolean upcomingOnly,
            @RequestParam(value = "class", required = false) String className,
            @RequestParam(value = "vaccineName", required = false) String vaccineName,
            @RequestHeader("Authorization") String token) {
        return vaccinationService.getAllVaccinationDrives(upcomingOnly, className, vaccineName, token);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a vaccination drive by ID", description = "Retrieves the details of a vaccination drive using its unique ID.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Vaccination drive found"),
        @ApiResponse(responseCode = "404", description = "Vaccination drive not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<VaccinationDTO> getVaccinationDriveById(
            @PathVariable Long id,
            @RequestHeader("Authorization") String token) {
        VaccinationDTO dto = vaccinationService.getDriveById(id, token);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{driveId}")
    @Operation(summary = "Update a vaccination drive", description = "Updates a vaccination drive by ID.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Vaccination drive updated"),
        @ApiResponse(responseCode = "400", description = "Invalid request body"),
        @ApiResponse(responseCode = "404", description = "Vaccination drive not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<String> updateVaccinationDrive(
            @PathVariable("driveId") Long driveId,
            @RequestBody VaccinationDTO dto,
            @RequestHeader("Authorization") String token) {
        vaccinationService.updateVaccinationDrive(driveId, dto, token);
        return ResponseEntity.ok("Vaccination drive updated successfully.");
    }

    @GetMapping("/report")
    @Operation(summary = "Get vaccination report", description = "Generates a report of vaccination drives.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Report generated successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
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
    @Operation(summary = "Get vaccination summary", description = "Retrieves vaccination statistics, optionally filtered by vaccine.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Summary generated successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<VaccinationSummaryDTO> getVaccinationSummary(
            @RequestParam(required = false) Long vaccineId,
            @RequestHeader("Authorization") String token) {
        VaccinationSummaryDTO summary = vaccinationService.getVaccinationSummary(vaccineId);
        return ResponseEntity.ok(summary);
    }

    @PatchMapping("/mark-vaccinated")
    @Operation(summary = "Mark student as vaccinated", description = "Marks a student as vaccinated for a specific drive.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Student marked vaccinated"),
        @ApiResponse(responseCode = "400", description = "Invalid data"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "404", description = "Drive or student not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<String> markVaccinated(
            @RequestBody MarkVaccinatedDTO dto,
            @RequestHeader("Authorization") String token) {
        vaccinationService.markStudentAsVaccinated(dto.getStudentId(), dto.getDriveId());
        return ResponseEntity.ok("Student marked as vaccinated successfully.");
    }

    @DeleteMapping("/students/{studentId}")
    @Operation(summary = "Delete student's vaccinations", description = "Deletes vaccination records for a specific student.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Vaccinations deleted"),
        @ApiResponse(responseCode = "404", description = "Student not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<String> deleteVaccinationsForStudent(
            @PathVariable("studentId") Long studentId,
            @RequestHeader("Authorization") String token) {
        try {
            vaccinationService.deleteVaccinationsByStudentId(studentId);
            return ResponseEntity.status(HttpStatus.OK).body("Vaccinations deleted successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete vaccinations.");
        }
    }

    @GetMapping("/assigned/{driveId}")
    @Operation(summary = "Get students assigned to a drive", description = "Retrieves a list of students assigned to a drive.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Students fetched successfully"),
        @ApiResponse(responseCode = "404", description = "Drive not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public List<StudentRegisterDTO> getAssignedStudents(
            @PathVariable Long driveId,
            @RequestHeader("Authorization") String token) {
        return vaccinationService.getAssignedStudentsByDriveId(driveId, token);
    }
}
