package com.assignment.vaccinationservice.DTOs;

import java.time.LocalDate;

public class VaccinationReportDTO {

	private String firstName;
    private String lastName;
    private String studentClass;
    private String email;
    private String vaccineName;
    private LocalDate vaccinationDate;
    private String vaccinationStatus;
    private Long studentId;
	private Long vaccineId;
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getStudentClass() {
		return studentClass;
	}
	public void setStudentClass(String studentClass) {
		this.studentClass = studentClass;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getVaccineName() {
		return vaccineName;
	}
	public void setVaccineName(String vaccineName) {
		this.vaccineName = vaccineName;
	}
	public LocalDate getVaccinationDate() {
		return vaccinationDate;
	}
	public void setVaccinationDate(LocalDate vaccinationDate) {
		this.vaccinationDate = vaccinationDate;
	}
	public String getVaccinationStatus() {
		return vaccinationStatus;
	}
	public void setVaccinationStatus(String vaccinationStatus) {
		this.vaccinationStatus = vaccinationStatus;
	}
	public Long getStudentId() {
		return studentId;
	}
	public void setStudentId(Long studentId) {
		this.studentId = studentId;
	}
	public Long getVaccineId() {
		return vaccineId;
	}
	public void setVaccineId(Long vaccineId) {
		this.vaccineId = vaccineId;
	}
	public VaccinationReportDTO(String firstName, String lastName, String studentClass, String email,
			String vaccineName, LocalDate vaccinationDate, String vaccinationStatus, Long studentId, Long vaccineId) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.studentClass = studentClass;
		this.email = email;
		this.vaccineName = vaccineName;
		this.vaccinationDate = vaccinationDate;
		this.vaccinationStatus = vaccinationStatus;
		this.studentId = studentId;
		this.vaccineId = vaccineId;
	}
	public VaccinationReportDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
}
