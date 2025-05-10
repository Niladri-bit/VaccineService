package com.assignment.vaccinationservice.DTOs;

public class VaccinationSummaryDTO {

	private int totalAssignedStudents;
    private int totalVaccinatedStudents;
    private double vaccinationPercentage;
	public int getTotalAssignedStudents() {
		return totalAssignedStudents;
	}
	public void setTotalAssignedStudents(int totalAssignedStudents) {
		this.totalAssignedStudents = totalAssignedStudents;
	}
	public int getTotalVaccinatedStudents() {
		return totalVaccinatedStudents;
	}
	public void setTotalVaccinatedStudents(int totalVaccinatedStudents) {
		this.totalVaccinatedStudents = totalVaccinatedStudents;
	}
	public double getVaccinationPercentage() {
		return vaccinationPercentage;
	}
	public void setVaccinationPercentage(double vaccinationPercentage) {
		this.vaccinationPercentage = vaccinationPercentage;
	}
	public VaccinationSummaryDTO(int totalAssignedStudents, int totalVaccinatedStudents, double vaccinationPercentage) {
		super();
		this.totalAssignedStudents = totalAssignedStudents;
		this.totalVaccinatedStudents = totalVaccinatedStudents;
		this.vaccinationPercentage = vaccinationPercentage;
	}
	public VaccinationSummaryDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
    
    
}
