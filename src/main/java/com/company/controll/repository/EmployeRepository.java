package com.company.controll.repository;

import com.company.controll.entity.Department;
import com.company.controll.entity.Employe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeRepository extends JpaRepository<Employe,Long>{
}
