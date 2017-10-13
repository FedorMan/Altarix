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
import java.util.Optional;

@Controller
@RequestMapping("api/department")
public class DepartmentController {

    @Autowired
    DepartmentRepository departmentRepository;

    //Создает новый департамент с учетом следующих правил:
    //1)только у Верхнего в иерархии департамента нет родительского
    //2)в системе не может быть двух одинаково названных департаментов
    @RequestMapping(path = "/create", method = RequestMethod.POST)
    public ResponseEntity<String> create(RequestEntity<Department> requestEntity) {
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
    public ResponseEntity<String> delete(@RequestParam(value = "id") long id) {
        if (departmentRepository.getOne(id).getEmployes().isEmpty()) {
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
    public @ResponseBody DepartamentInformation getInformation(@RequestParam(value = "id") long id) {
        Department department = departmentRepository.getOne(id);
        Optional<Employe> mainEmploye = department.getEmployes().stream().filter(employe -> employe.getMain()).findFirst();
        DepartamentInformation departamentInformation = new DepartamentInformation(department.getName(), department.getCreateBy(), mainEmploye.get(), department.getEmployes().size());
        return departamentInformation;
    }

    //Предоставление информации о департаментах, находящихся в
    // непосредственном подчинении данного департамента (на уровень ниже).
    @RequestMapping(path = "/downinformation", method = RequestMethod.GET)
    public @ResponseBody List<DepartamentInformation> getDownInformation(@RequestParam(value = "id") long id) {
        List<Department> departments = departmentRepository.findAllByParentDepartmentId(id);
        List<DepartamentInformation> departamentInformations = new ArrayList<>();
        for (Department department : departments) {
            Optional<Employe> mainEmploye = department.getEmployes().stream().filter(employe -> employe.getMain()).findFirst();
            departamentInformations.add(new DepartamentInformation(department.getName(),
                    department.getCreateBy(),
                    mainEmploye.isPresent() ? mainEmploye.get() : null,
                    department.getEmployes().size()));
        }
        return departamentInformations;
    }

    //Предоставление информации о всех департаментах, находящихся в подчинении данного департамента
    // (все подчиненные департаменты. Для головного департамента - это все остальные департаменты).
    @RequestMapping(path = "/downallinformation", method = RequestMethod.GET)
    public @ResponseBody List<DepartamentInformation> getDownAllInformation(@RequestParam(value = "id") long id) {
        List<DepartamentInformation> departamentInformations = new ArrayList<>();
        findAllInformationAboutDepartment(id, departamentInformations);
        return departamentInformations;
    }

    //Перенос департамента. Задание другого департамента, куда будет входить данный департамент.
    @RequestMapping(path = "/transposition", method = RequestMethod.GET)
    public ResponseEntity<String> transposition(@RequestParam(value = "id") long id, @RequestParam(value = "parentId") long parentId){
        Department department = departmentRepository.getOne(id);
        Department parentDepartment = departmentRepository.getOne(parentId);
        department.setParentDepartment(parentDepartment);
        departmentRepository.flush();
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    //Поиск департамента по наименованию.
    @RequestMapping(path = "/findbyname", method = RequestMethod.GET)
    public @ResponseBody Department findByName(@RequestParam(value = "name") String name){
        return departmentRepository.findByName(name);
    }

    //Получение информации о фонде заработной платы департамента
    // (сумма зарплат всех сотрудников департамента).
    @RequestMapping(path = "/salarydepartment", method = RequestMethod.GET)
    public @ResponseBody Double getAllSalary(@RequestParam(value = "id") long id){
        Department department = departmentRepository.getOne(id);
        return department.getEmployes().stream().mapToDouble(value -> value.getSalary()).sum();
    }

    //Получение информации о всех вышестоящих департаментах данного департамента.
    @RequestMapping(path = "upallinformation", method = RequestMethod.GET)
    public @ResponseBody List<DepartamentInformation> getUpAllInformation(@RequestParam(value = "id") long id){
        List<DepartamentInformation> departamentInformations = new ArrayList<>();
        Department department = departmentRepository.getOne(id);
        while (department.getParentDepartment()!=null){
            department = department.getParentDepartment();
            Optional<Employe> mainEmploye = department.getEmployes().stream().filter(employe -> employe.getMain()).findFirst();
            departamentInformations.add(new DepartamentInformation(department.getName(),
                    department.getCreateBy(),
                    mainEmploye.isPresent() ? mainEmploye.get() : null,
                    department.getEmployes().size()));
        }
        return departamentInformations;
    }

    //проверить является ли добавляемый департамент верхним в иерархии
    public boolean validateHeadDepartment(Department department) {
        List<Department> departmentList = departmentRepository.findAll();
        if ((departmentList.isEmpty() && department.getParentDepartment() == null) || (!departmentList.isEmpty() && department.getParentDepartment() != null)) {
            return true;
        }
        return false;
    }

    //проверить нет ли совпадений имен у создаваемого департамента и уже существующих
    public boolean validateEqualsDepartment(String name) {
        List<Department> departmentList = departmentRepository.findAll();
        for (Department d : departmentList) {
            if (d.getName().equals(name)) return false;
        }
        return true;
    }

    //рекурсивная функция для получения информации о всех нижележащих департаментах
    public void findAllInformationAboutDepartment(long id, List<DepartamentInformation> departamentInformations) {
        List<Department> departments = departmentRepository.findAllByParentDepartmentId(id);
        List<Long> idsDepartments = new ArrayList<>();
        for (Department department : departments) {
            idsDepartments.add(department.getId());
            Optional<Employe> mainEmploye = department.getEmployes().stream().filter(employe -> employe.getMain()).findFirst();
            departamentInformations.add(new DepartamentInformation(department.getName(),
                    department.getCreateBy(),
                    mainEmploye.isPresent() ? mainEmploye.get() : null,
                    department.getEmployes().size()));
        }
        idsDepartments.stream().forEach(newId -> findAllInformationAboutDepartment(newId, departamentInformations));
    }
}
