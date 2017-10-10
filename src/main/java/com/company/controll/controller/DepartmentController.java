package com.company.controll.controller;

import com.company.controll.entity.Department;
import com.company.controll.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("api/department")
public class DepartmentController {

    @Autowired
    DepartmentRepository departmentRepository;

    @RequestMapping(path = "/createDepartment",method = RequestMethod.POST)
    public ResponseEntity<String> createDepartment(RequestEntity<Department> requestEntity){
        Department department = requestEntity.getBody();
        if (!validateHeadDepartment(department)) return ResponseEntity.status(HttpStatus.FAILED_DEPENDENCY).build();
        if (!validateEqualsDepartment(department)) return ResponseEntity.status(HttpStatus.CONFLICT).build();
        departmentRepository.save(department);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @GetMapping(path="/getAllDepartments")
    public @ResponseBody List<Department> getAllDepartment(){
        return departmentRepository.findAll();
    }


    public boolean validateHeadDepartment(Department department){
        List<Department> departmentList = departmentRepository.findAll();
        if ((departmentList.isEmpty() && department.getParentDepartment()==null) || (!departmentList.isEmpty() && department.getParentDepartment()!=null)){
                return true;
        }
        return false;
    }

    public boolean validateEqualsDepartment(Department department){
        List<Department> departmentList = departmentRepository.findAll();
        for (Department d: departmentList) {
            if (d.getName().equals(department.getName())) return false;
        }
        return true;
    }
}
