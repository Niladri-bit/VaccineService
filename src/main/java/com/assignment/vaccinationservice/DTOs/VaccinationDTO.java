package com.assignment.vaccinationservice.DTOs;

import java.time.LocalDate;

public class VaccinationDTO {
	
	private Long id;
	private String vaccineName;
    private LocalDate driveDate;
    private Integer availableDoses;
    private String applicableClasses;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getVaccineName() {
		return vaccineName;
	}
	public void setVaccineName(String vaccineName) {
		this.vaccineName = vaccineName;
	}
	public LocalDate getDriveDate() {
		return driveDate;
	}
	public void setDriveDate(LocalDate driveDate) {
		this.driveDate = driveDate;
	}
	public Integer getAvailableDoses() {
		return availableDoses;
	}
	public void setAvailableDoses(Integer availableDoses) {
		this.availableDoses = availableDoses;
	}
	public String getApplicableClasses() {
		return applicableClasses;
	}
	public void setApplicableClasses(String applicableClasses) {
		this.applicableClasses = applicableClasses;
	}
	public VaccinationDTO(Long id, String vaccineName, LocalDate driveDate, Integer availableDoses,
			String applicableClasses) {
		super();
		this.id = id;
		this.vaccineName = vaccineName;
		this.driveDate = driveDate;
		this.availableDoses = availableDoses;
		this.applicableClasses = applicableClasses;
	}
	public VaccinationDTO() {
		super();
		// TODO Auto-generated constructor stub
	} 
    
    

}
