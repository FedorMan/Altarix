package com.company.controll.repository;

import com.company.controll.entity.Department;
import com.company.controll.entity.Worker;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WorkerRepository extends JpaRepository<Worker,Long>{
    public List<Worker> findAllByDepartment(Department department);
}
