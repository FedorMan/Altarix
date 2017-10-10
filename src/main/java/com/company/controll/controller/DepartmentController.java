package com.company.controll.controller;

import com.company.controll.entity.Department;
import com.company.controll.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("api/department")
public class DepartmentController {

    @Autowired
    DepartmentRepository departmentRepository;

    //Создает новый департамент с учетом следующих правил:
    //1)только у Верхнего в иерархии департамента нет родительского
    //2)в системе не может быть двух одинаково названных департаментов
    @RequestMapping(path = "/createDepartment",method = RequestMethod.POST)
    public ResponseEntity<String> createDepartment(RequestEntity<Department> requestEntity){
        Department department = requestEntity.getBody();
        if (!validateHeadDepartment(department)) return ResponseEntity.status(HttpStatus.FAILED_DEPENDENCY).build();
        if (!validateEqualsDepartment(department.getName())) return ResponseEntity.status(HttpStatus.CONFLICT).build();
        departmentRepository.save(department);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    //изменяет наименование департамента с учетом того что в системе нет департаментов с одинаковыми названиями
    @RequestMapping(path = "/updateDepartment", method = RequestMethod.GET)
    public ResponseEntity<String> updateDepartment(@RequestParam(value = "id") long id, @RequestParam(value = "name") String name) {
        if (!validateEqualsDepartment(name)) return ResponseEntity.status(HttpStatus.CONFLICT).build();
        departmentRepository.getOne(id).setName(name);
        departmentRepository.flush();
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @RequestMapping(path="/getAllDepartments", method = RequestMethod.GET)
    public @ResponseBody List<Department> getAllDepartment(){
        return departmentRepository.findAll();
    }

    //проверить является ли добавляемый департамент верхним в иерархии
    public boolean validateHeadDepartment(Department department){
        List<Department> departmentList = departmentRepository.findAll();
        if ((departmentList.isEmpty() && department.getParentDepartment()==null) || (!departmentList.isEmpty() && department.getParentDepartment()!=null)){
                return true;
        }
        return false;
    }

    //проверить нет ли совпадений имен у создаваемого департамента и уже существующих
    public boolean validateEqualsDepartment(String name){
        List<Department> departmentList = departmentRepository.findAll();
        for (Department d: departmentList) {
            if (d.getName().equals(name)) return false;
        }
        return true;
    }
}
