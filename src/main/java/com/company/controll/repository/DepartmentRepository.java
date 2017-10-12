package com.company.controll.repository;

import com.company.controll.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface DepartmentRepository extends JpaRepository<Department,Long>{
    public List<Department> findAllByParentDepartmentId(long id);
}
