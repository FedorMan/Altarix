package com.company.controll.repository;

import com.company.controll.entity.Department;
import com.company.controll.entity.Employe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface EmployeRepository extends JpaRepository<Employe,Long>{
    public List<Employe> findByDepartmentId(long id);
    public List<Employe> findByBirthday(LocalDate date);
}
