package com.assignment.vaccinationservice.DTOs;

public class MarkVaccinatedDTO {

	private Long studentId;
    private Long driveId;
	public MarkVaccinatedDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Long getStudentId() {
		return studentId;
	}
	public void setStudentId(Long studentId) {
		this.studentId = studentId;
	}
	public Long getDriveId() {
		return driveId;
	}
	public void setDriveId(Long driveId) {
		this.driveId = driveId;
	}
	public MarkVaccinatedDTO(Long studentId, Long driveId) {
		super();
		this.studentId = studentId;
		this.driveId = driveId;
	}
    
    
}
