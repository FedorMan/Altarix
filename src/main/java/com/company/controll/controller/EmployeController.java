package com.company.controll.controller;

import com.company.controll.entity.Department;
import com.company.controll.entity.Employe;
import com.company.controll.repository.DepartmentRepository;
import com.company.controll.repository.EmployeRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("api/employe")
public class EmployeController {
    @Autowired
    private EmployeRepository employeRepository;
    @Autowired
    private DepartmentRepository departmentRepository;

    //Получение списка сотрудников департамента.
    @RequestMapping(path = "/getbydepartment", method = RequestMethod.GET)
    public @ResponseBody List<Employe> getByDepartment(@RequestParam(value = "id") long id){
        return employeRepository.findByDepartmentId(id);
    }

    //Создание сотрудника департамента.
    @RequestMapping(path = "/create",method = RequestMethod.POST)
    public ResponseEntity<String> create(RequestEntity<Employe> requestEntity){
        Employe employe = requestEntity.getBody();
        employeRepository.save(employe);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    //Получение информации о сотруднике.
    @RequestMapping(path = "/informationof", method = RequestMethod.GET)
    public @ResponseBody Employe getInformationOf(@RequestParam(value = "id") long id){
        return employeRepository.getOne(id);
    }

    //Редактирование сведений о сотруднике департамента.
    @RequestMapping(path = "/update",method = RequestMethod.POST)
    public ResponseEntity<String> update(RequestEntity<Employe> requestEmploye){
        Employe newEmploye = requestEmploye.getBody();
        Employe employe = employeRepository.getOne(newEmploye.getId());
        employe.setFirstname(newEmploye.getFirstname());
        employe.setLastname(newEmploye.getLastname());
        employe.setPatronymic(newEmploye.getPatronymic());
        employe.setBirthday(newEmploye.getBirthday());
        employe.setPosition(newEmploye.getPosition());
        employe.setSalary(newEmploye.getSalary());
        employe.setMain(newEmploye.isMain());
        employe.setStartDate(newEmploye.getStartDate());
        employe.setEndDate(newEmploye.getEndDate());
        employe.setSex(newEmploye.getSex());
        employeRepository.flush();
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    //Увольнение сотрудника с указанием даты увольнения.
    @RequestMapping(path = "/dismiss",method = RequestMethod.GET)
    public ResponseEntity<String> dismissal(@RequestParam(value = "id") long id, @RequestParam(value = "year") int year, @RequestParam(value = "month") int month, @RequestParam(value = "day") int day){
        Employe employe = employeRepository.getOne(id);
        LocalDate endDate = LocalDate.of(year,month,day);
        employe.setEndDate(endDate);
        employeRepository.flush();
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    //Перевод сотрудника из одного департамента в другой.
    @RequestMapping(path = "/transfer", method = RequestMethod.GET)
    public ResponseEntity<String> transfer(@RequestParam(value = "id") long id, @RequestParam(value = "idDepartment") long idDepatment){
        Employe employe = employeRepository.getOne(id);
        Department department = departmentRepository.getOne(idDepatment);
        employe.setDepartment(department);
        employeRepository.flush();
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    //Перевод всех сотрудников департамента в другой департамент.
    @RequestMapping(path = "/transferdepartment", method = RequestMethod.GET)
    public ResponseEntity<String> transferDepartment(@RequestParam(value = "idCurrent") long idCurrent, @RequestParam(value = "idDepartment") long idDepatment){
        Department currentDepartment = departmentRepository.getOne(idCurrent);
        Department department = departmentRepository.getOne(idDepatment);
        currentDepartment.getEmployes().stream().forEach(employe -> employe.setDepartment(department));
        employeRepository.flush();
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    

}
