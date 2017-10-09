package com.company.controll.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table(name = "departments")
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotNull
    private String name;
    @NotNull
    private LocalDate createBy;
    @ManyToOne(targetEntity = Department.class)
    private Department parentDepartment;

    public Department() {
    }

    public Department(String name, LocalDate createBy, Department parentDepartment) {
        this.name = name;
        this.createBy = createBy;
        this.parentDepartment = parentDepartment;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getCreateBy() {
        return createBy;
    }

    public void setCreateBy(LocalDate createBy) {
        this.createBy = createBy;
    }

    public Department getParentDepartment() {
        return parentDepartment;
    }

    public void setParentDepartment(Department parentDepartment) {
        this.parentDepartment = parentDepartment;
    }
}
