package com.assignment.vaccinationservice.entities;

import java.time.LocalDate;

import jakarta.persistence.*;

@Entity
@Table(name = "student_vaccinations", schema = "vaccinationdb")
public class StudentVaccinationEntity {

    @EmbeddedId
    private StudentVaccinationId id;

    @Column(nullable = false)
    private LocalDate vaccinationDate;

    @Column(nullable = false)
    private String status; // No default, set in service

    public StudentVaccinationEntity() {}

    public StudentVaccinationEntity(StudentVaccinationId id, LocalDate vaccinationDate, String status) {
        this.id = id;
        this.vaccinationDate = vaccinationDate;
        this.status = status;
    }

    public StudentVaccinationId getId() {
        return id;
    }

    public void setId(StudentVaccinationId id) {
        this.id = id;
    }

    public LocalDate getVaccinationDate() {
        return vaccinationDate;
    }

    public void setVaccinationDate(LocalDate vaccinationDate) {
        this.vaccinationDate = vaccinationDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
