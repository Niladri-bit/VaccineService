package com.assignment.vaccinationservice.entities;


import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import jakarta.persistence.*;
import lombok.Data;
 
@Entity
@Table(name = "vaccines", schema = "vaccinationdb")
public class VaccinationEntity {

	  @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	  @Column(nullable = false)
	    private String vaccineName;

	  @Column(nullable = false)
	    private LocalDate driveDate;

	  @Column(nullable = false)
	    private int availableDoses;

	    @ElementCollection
	    @CollectionTable(name = "drive_applicable_classes", joinColumns = @JoinColumn(name = "drive_id"))
	    @Column(name = "class_name",nullable = false)
	    private Set<String> applicableClasses;

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

		public int getAvailableDoses() {
			return availableDoses;
		}

		public void setAvailableDoses(int availableDoses) {
			this.availableDoses = availableDoses;
		}

		public Set<String> getApplicableClasses() {
			return applicableClasses;
		}

		public void setApplicableClasses(Set<String> applicableClasses) {
			this.applicableClasses = applicableClasses;
		}

		public VaccinationEntity(Long id, String vaccineName, LocalDate driveDate, int availableDoses,
				Set<String> applicableClasses) {
			super();
			this.id = id;
			this.vaccineName = vaccineName;
			this.driveDate = driveDate;
			this.availableDoses = availableDoses;
			this.applicableClasses = applicableClasses;
		}

		public VaccinationEntity() {
			// TODO Auto-generated constructor stub
		} 
	    
	    
}
