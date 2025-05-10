package com.assignment.vaccinationservice.entities;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Embeddable;

@Embeddable
public class StudentVaccinationId implements Serializable {

    private Long studentId;
    private Long driveId;

    public StudentVaccinationId() {}

    public StudentVaccinationId(Long studentId, Long driveId) {
        this.studentId = studentId;
        this.driveId = driveId;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StudentVaccinationId)) return false;
        StudentVaccinationId that = (StudentVaccinationId) o;
        return Objects.equals(studentId, that.studentId) &&
               Objects.equals(driveId, that.driveId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(studentId, driveId);
    }
}
