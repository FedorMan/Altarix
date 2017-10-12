package com.company.controll.model;

import com.company.controll.entity.Department;
import com.company.controll.entity.Employe;

import java.time.LocalDate;


public class DepartamentInformation {
    private String nameDepartment;
    private LocalDate createBy;
    private Employe headDepartment;
    private Integer countWorkers;

    public DepartamentInformation() {
    }

    public DepartamentInformation(String nameDepartment, LocalDate createBy, Employe headDepartment, Integer countWorkers) {
        this.nameDepartment = nameDepartment;
        this.createBy = createBy;
        this.headDepartment = headDepartment;
        this.countWorkers = countWorkers;
    }

    public String getNameDepartment() {
        return nameDepartment;
    }

    public void setNameDepartment(String nameDepartment) {
        this.nameDepartment = nameDepartment;
    }

    public LocalDate getCreateBy() {
        return createBy;
    }

    public void setCreateBy(LocalDate createBy) {
        this.createBy = createBy;
    }

    public Employe getHeadDepartment() {
        return headDepartment;
    }

    public void setHeadDepartment(Employe headDepartment) {
        this.headDepartment = headDepartment;
    }

    public Integer getCountWorkers() {
        return countWorkers;
    }

    public void setCountWorkers(Integer countWorkers) {
        this.countWorkers = countWorkers;
    }
}
