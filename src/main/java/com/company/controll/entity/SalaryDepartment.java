package com.company.controll.entity;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "salary_departments")
public class SalaryDepartment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne(cascade = CascadeType.REMOVE)
    @NotNull
    private Department department;
    @NotNull
    private Double salary;
    @NotNull
    @Type(type="org.hibernate.type.LocalDateTimeType")
    private LocalDateTime time;

    public SalaryDepartment() {
    }

    public SalaryDepartment(Long id, Department department, Double salary,LocalDateTime time) {
        this.id = id;
        this.department = department;
        this.salary = salary;
        this.time = time;
    }

    public SalaryDepartment(Department department) {
        this.department = department;
        double sum = department.getEmployes().stream().mapToDouble(value -> value.getSalary()).sum();
        this.salary = sum;
        this.time = LocalDateTime.now();

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }
}
