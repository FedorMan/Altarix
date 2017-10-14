package com.company.controll.entity;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table(name = "employes")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Employe {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotNull
    private String lastname;
    @NotNull
    private String firstname;
    @NotNull
    private String patronymic;
    @NotNull
    private String sex;
    @NotNull
    private LocalDate birthday;
    @NotNull
    private LocalDate startDate;
    private LocalDate endDate;
    @NotNull
    @OneToOne(targetEntity = Position.class)
    private Position position;
    @NotNull
    private double salary;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;
    @NotNull
    private Boolean main;

    public Employe() {
    }

    public Employe(String lastname, String firstname, String patronymic, String sex, LocalDate birthday, LocalDate startDate, LocalDate endDate, Position position, double salary, Department department, Boolean main) {
        this.lastname = lastname;
        this.firstname = firstname;
        this.patronymic = patronymic;
        this.sex = sex;
        this.birthday = birthday;
        this.startDate = startDate;
        this.endDate = endDate;
        this.position = position;
        this.salary = salary;
        this.department = department;
        this.main = main;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Boolean isMain() {
        return main;
    }

    public void setMain(Boolean main) {
        this.main = main;
    }
}
