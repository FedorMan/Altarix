package com.company.controll.shelduled;

import com.company.controll.entity.Department;
import com.company.controll.entity.SalaryDepartment;
import com.company.controll.repository.DepartmentRepository;
import com.company.controll.repository.SalaryDepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ScheduledTasks {
    @Autowired
    private SalaryDepartmentRepository salaryDepartmentRepository;
    @Autowired
    private DepartmentRepository departmentRepository;

    @Scheduled(fixedRate = 300000)
    public void reportCurrentTime() {
        List<Department> departments = departmentRepository.findAll();
        for(Department d : departments){
            salaryDepartmentRepository.save(new SalaryDepartment(d));
        }
    }
}
