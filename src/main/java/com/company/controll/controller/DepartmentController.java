package com.company.controll.controller;

import com.company.controll.entity.Department;
import com.company.controll.entity.Employe;
import com.company.controll.model.DepartamentInformation;
import com.company.controll.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("api/department")
public class DepartmentController {

    @Autowired
    DepartmentRepository departmentRepository;

    //Создает новый департамент с учетом следующих правил:
    //1)только у Верхнего в иерархии департамента нет родительского
    //2)в системе не может быть двух одинаково названных департаментов
    @RequestMapping(path = "/create",method = RequestMethod.POST)
    public ResponseEntity<String> create(RequestEntity<Department> requestEntity){
        Department department = requestEntity.getBody();
        if (!validateHeadDepartment(department)) return ResponseEntity.status(HttpStatus.FAILED_DEPENDENCY).build();
        if (!validateEqualsDepartment(department.getName())) return ResponseEntity.status(HttpStatus.CONFLICT).build();
        departmentRepository.save(department);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    //изменяет наименование департамента с учетом того что в системе нет департаментов с одинаковыми названиями
    @RequestMapping(path = "/update", method = RequestMethod.GET)
    public ResponseEntity<String> update(@RequestParam(value = "id") long id, @RequestParam(value = "name") String name) {
        if (!validateEqualsDepartment(name)) return ResponseEntity.status(HttpStatus.CONFLICT).build();
        departmentRepository.getOne(id).setName(name);
        departmentRepository.flush();
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    //удаляет указанный департамент, при условии, что в нем нет сотрудников
    @RequestMapping(path = "/delete", method = RequestMethod.DELETE)
    public ResponseEntity<String> delete(@RequestParam(value = "id") long id){
        if (departmentRepository.getOne(id).getworkers().isEmpty()){
            departmentRepository.delete(id);
            departmentRepository.flush();
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    //Просмотр сведений о департаменте.
    // Должна быть выдана информация о наименовании департамента,
    // дате создания, руководителе департамента и количестве сотрудников департамента.
    @RequestMapping(path = "/information", method = RequestMethod.GET)
    public @ResponseBody DepartamentInformation getInformation(@RequestParam(value = "id") long id){
        Department department = departmentRepository.getOne(id);
        Employe mainEmploye = null;
        for(Employe e : department.getworkers()){
            if (e.getMain()) mainEmploye = e;
        }
        DepartamentInformation departamentInformation = new DepartamentInformation(department.getName(),department.getCreateBy(),mainEmploye,department.getworkers().size());
        return departamentInformation;
    }

    //Предоставление информации о департаментах, находящихся в
    // непосредственном подчинении данного департамента (на уровень ниже).
    @RequestMapping(path = "/downinformation", method = RequestMethod.GET)
    public @ResponseBody List<DepartamentInformation> getDownInformation(@RequestParam(value = "id") long id){
        List<Department> departments = departmentRepository.findAllByParentDepartmentId(id);
        List<DepartamentInformation> departamentInformations = new ArrayList<>();
        for(Department department : departments) {
            Employe mainEmploye = null;
            for (Employe e : department.getworkers()) {
                if (e.getMain()) mainEmploye = e;
            }
            departamentInformations.add(new DepartamentInformation(department.getName(), department.getCreateBy(), mainEmploye, department.getworkers().size()));
        }
        return departamentInformations;
    }

    @RequestMapping(path="/all", method = RequestMethod.GET)
    public @ResponseBody List<Department> getAllDepartment(){
        List<Department> list =departmentRepository.findAll();
        return list;
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
