package com.company.controll.entity;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "history_departments")
public class ChangeDepartament {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotNull
    private Long idDepartment;
    @NotNull
    private String oldName;
    @NotNull
    private Long oldParentDepartmentId;
    private String newName;
    private Long newParentDepartmentId;
    @Type(type="org.hibernate.type.LocalDateTimeType")
    private LocalDateTime time;

    public ChangeDepartament() {
    }

    public ChangeDepartament(Long idDepartment, String oldName, Long oldParentDepartmentId, String newName, Long newParentDepartmentId, LocalDateTime time) {
        this.idDepartment = idDepartment;
        this.oldName = oldName;
        this.oldParentDepartmentId = oldParentDepartmentId;
        this.newName = newName;
        this.newParentDepartmentId = newParentDepartmentId;
        this.time = time;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdDepartment() {
        return idDepartment;
    }

    public void setIdDepartment(Long idDepartment) {
        this.idDepartment = idDepartment;
    }

    public String getOldName() {
        return oldName;
    }

    public void setOldName(String oldName) {
        this.oldName = oldName;
    }

    public Long getOldParentDepartmentId() {
        return oldParentDepartmentId;
    }

    public void setOldParentDepartmentId(Long oldParentDepartmentId) {
        this.oldParentDepartmentId = oldParentDepartmentId;
    }

    public String getNewName() {
        return newName;
    }

    public void setNewName(String newName) {
        this.newName = newName;
    }

    public Long getNewParentDepartmentId() {
        return newParentDepartmentId;
    }

    public void setNewParentDepartmentId(Long newParentDepartmentId) {
        this.newParentDepartmentId = newParentDepartmentId;
    }

    public LocalDateTime getDate() {
        return time;
    }

    public void setDate(LocalDateTime date) {
        this.time = date;
    }
}
