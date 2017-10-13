package com.company.controll.entity;



import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;


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
    @OneToMany(targetEntity = Employe.class, mappedBy = "department", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Employe> employes;

    public Department() {
    }

    public Department(String name, LocalDate createBy, Department parentDepartment) {
        this.name = name;
        this.createBy = createBy;
        this.parentDepartment = parentDepartment;
    }

    public Department(String name, LocalDate createBy, Department parentDepartment, Set<Employe> employes) {
        this.name = name;
        this.createBy = createBy;
        this.parentDepartment = parentDepartment;
        this.employes = employes;
    }

    public Set<Employe> getEmployes() {
        return employes;
    }

    public void setEmployes(Set<Employe> employes) {
        this.employes = employes;
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

    @Override
    public String toString() {
        return "Department{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", createBy=" + createBy +
                ", parentDepartment=" + parentDepartment +
                ", employes=" + employes +
                '}';
    }
}
