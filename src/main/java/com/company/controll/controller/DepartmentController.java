package com.company.controll.controller;

import com.company.controll.entity.ChangeDepartament;
import com.company.controll.entity.Department;
import com.company.controll.entity.Employe;
import com.company.controll.model.DepartamentInformation;
import com.company.controll.repository.ChangeDepartmentRepository;
import com.company.controll.repository.DepartmentRepository;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static javax.servlet.http.HttpServletResponse.*;

@Controller
@RequestMapping("api/department")
public class DepartmentController {

    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private ChangeDepartmentRepository changeDepartmentRepository;

    @ApiOperation(value = "Создать департамент",
            notes = "Только у Верхнего в иерархии департамента нет родительского." +
                    "В системе не может быть двух одинаково названных департаментов.",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses({
            @ApiResponse(code = SC_CREATED, message = "Департамент создан"),
            @ApiResponse(code = SC_CONFLICT, message = "Главный департамент уже имеется или департамент с таким именем уже создан")
    })
    @RequestMapping(path = "/create", method = RequestMethod.POST)
    public ResponseEntity<String> create(RequestEntity<Department> requestEntity) {
        Department department = requestEntity.getBody();
        if (!validateHeadDepartment(department) || !validateEqualsDepartment(department.getName()))
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        departmentRepository.save(department);
        ChangeDepartament changeDepartament = new ChangeDepartament(department.getId(),department.getName(),
                department.getParentDepartment().getId(),null,null, LocalDateTime.now());
        changeDepartmentRepository.save(changeDepartament);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @ApiOperation(value = "Изменить название департамента",
            notes = "Изменяет наименование департамента с учетом того что в системе " +
                    "нет департаментов с одинаковыми названиями",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses({
            @ApiResponse(code = SC_OK, message = "Департамент изменен"),
            @ApiResponse(code = SC_CONFLICT, message = "Департамент с таким именем уже создан")
    })
    @RequestMapping(path = "/update", method = RequestMethod.GET)
    public ResponseEntity<String> update(@RequestParam(value = "id") long id, @RequestParam(value = "name") String name) {
        if (!validateEqualsDepartment(name)) return ResponseEntity.status(HttpStatus.CONFLICT).build();
        Department department = departmentRepository.getOne(id);
        ChangeDepartament changeDepartament = new ChangeDepartament(department.getId(),department.getName(),
                department.getParentDepartment().getId(),name,null, LocalDateTime.now());
        department.setName(name);
        departmentRepository.flush();
        changeDepartmentRepository.save(changeDepartament);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @ApiOperation(value = "Удалить департамент",
            notes = "Удаляет указанный департамент, при условии, что в нем нет сотрудников",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses({
            @ApiResponse(code = SC_OK, message = "Департамент удален"),
            @ApiResponse(code = SC_CONFLICT, message = "В департаменте есть сотрудники")
    })
    @RequestMapping(path = "/delete", method = RequestMethod.DELETE)
    public ResponseEntity<String> delete(@RequestParam(value = "id") long id) {
        if (departmentRepository.getOne(id).getEmployes().isEmpty()) {
            departmentRepository.delete(id);
            departmentRepository.flush();
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    @ApiOperation(value = "Просмотр сведений о департаменте. ",
            notes = "Выдает информацию о наименовании департамента, дате создания," +
                    " руководителе департамента и количестве сотрудников департамента.",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses({
            @ApiResponse(code = SC_OK, response = DepartamentInformation.class, message = "Информация о департаменте"),
            @ApiResponse(code = SC_NOT_FOUND, message = "Департамент не найден")
    })
    @RequestMapping(path = "/information", method = RequestMethod.GET)
    public ResponseEntity<DepartamentInformation> getInformation(@RequestParam(value = "id") long id) {
        try {
            Department department = departmentRepository.getOne(id);
            Optional<Employe> mainEmploye = department.getEmployes().stream().filter(employe -> employe.isMain()).findFirst();
            DepartamentInformation departamentInformation = new DepartamentInformation(department.getName(), department.getCreateBy(),
                    mainEmploye.isPresent() ? mainEmploye.get() : null,
                    department.getEmployes().size());
            return new ResponseEntity(departamentInformation, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity("Департамент не найден", HttpStatus.NOT_FOUND);
        }
    }

    @ApiOperation(value = "Просмотр сведений о подчиненных департаментах. ",
            notes = "Выдает информацию о департаментах, находящихся в непосредственном подчинении",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses({
            @ApiResponse(code = SC_OK, response = DepartamentInformation.class, message = "Список департаментов находящихся в подчинении"),
            @ApiResponse(code = SC_NOT_FOUND, message = "Департамент не найден")
    })
    @RequestMapping(path = "/subordinates", method = RequestMethod.GET)
    public ResponseEntity<List<DepartamentInformation>> getSubordinateDepartments(@RequestParam(value = "id") long id) {
        try {
            Department department = departmentRepository.getOne(id);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity("Департамент не найден", HttpStatus.NOT_FOUND);
        }
        List<Department> departments = departmentRepository.findAllByParentDepartmentId(id);
        List<DepartamentInformation> departamentInformations = new ArrayList<>();
        for (Department department : departments) {
            Optional<Employe> mainEmploye = department.getEmployes().stream().filter(employe -> employe.isMain()).findFirst();
            departamentInformations.add(new DepartamentInformation(department.getName(),
                    department.getCreateBy(),
                    mainEmploye.isPresent() ? mainEmploye.get() : null,
                    department.getEmployes().size()));
        }
        return new ResponseEntity(departamentInformations, HttpStatus.OK);
    }

    @ApiOperation(value = "Просмотр сведений о всех подчиненных департаментах. ",
            notes = "Выдает информацию о всех департаментах, находящихся подчинении",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses({
            @ApiResponse(code = SC_OK, response = DepartamentInformation.class, message = "Список департаментов находящихся в подчинении"),
            @ApiResponse(code = SC_NOT_FOUND, message = "Департамент не найден")
    })
    @RequestMapping(path = "/allsubordinates", method = RequestMethod.GET)
    public ResponseEntity<List<DepartamentInformation>> getAllSubordinateDepartments(@RequestParam(value = "id") long id) {
        try {
            Department department = departmentRepository.getOne(id);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity("Департамент не найден", HttpStatus.NOT_FOUND);
        }
        List<DepartamentInformation> departamentInformations = new ArrayList<>();
        findAllInformationAboutDepartment(id, departamentInformations);
        return new ResponseEntity(departamentInformations, HttpStatus.OK);
    }

    @ApiOperation(value = "Перенос департамента",
            notes = "Задает другой департамент в который будет входить указанный",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses({
            @ApiResponse(code = SC_OK, response = DepartamentInformation.class, message = "Департамент перенесен"),
            @ApiResponse(code = SC_NOT_FOUND, message = "Департамент не найден")
    })
    @RequestMapping(path = "/transposition", method = RequestMethod.GET)
    public ResponseEntity<String> transposition(@RequestParam(value = "id") long id, @RequestParam(value = "parentId") long parentId) {
        try {
            Department department = departmentRepository.getOne(id);
            Department parentDepartment = departmentRepository.getOne(parentId);
            ChangeDepartament changeDepartament = new ChangeDepartament(department.getId(),department.getName(),
                    department.getParentDepartment().getId(),null,parentDepartment.getId(), LocalDateTime.now());
            department.setParentDepartment(parentDepartment);
            departmentRepository.flush();
            changeDepartmentRepository.save(changeDepartament);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (EntityNotFoundException e) {
            return new ResponseEntity("Департамент не найден", HttpStatus.NOT_FOUND);
        }
    }

    @ApiOperation(value = "Поиск департамента по наименованию",
            notes = "Возвращает департамент с заданным наименованием",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses({
            @ApiResponse(code = SC_OK, response = DepartamentInformation.class, message = "Департамент найден"),
            @ApiResponse(code = SC_NOT_FOUND, message = "Департамент не найден")
    })
    @RequestMapping(path = "/find", method = RequestMethod.GET)
    public ResponseEntity<Department> findByName(@RequestParam(value = "name") String name) {
        Department department = departmentRepository.findByName(name);
        if (department == null) return new ResponseEntity(HttpStatus.NOT_FOUND);
        return new ResponseEntity(department, HttpStatus.OK);
    }

    @ApiOperation(value = "Информация о заработной плате департамента",
            notes = "Возвращает суммарную заработную пллату департамента",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses({
            @ApiResponse(code = SC_OK, response = DepartamentInformation.class, message = "Заработная плата департамента"),
            @ApiResponse(code = SC_NOT_FOUND, message = "Департамент не найден")
    })
    @RequestMapping(path = "/salary", method = RequestMethod.GET)
    public ResponseEntity<Double> getSalaryDepartment(@RequestParam(value = "id") long id) {
        try {
            Department department = departmentRepository.getOne(id);
            double sallary = department.getEmployes().stream().mapToDouble(value -> value.getSalary()).sum();
            return new ResponseEntity(sallary, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity("Департамент не найден", HttpStatus.NOT_FOUND);
        }
    }

    @ApiOperation(value = "Просмотр сведений о вышестоящих департаментах. ",
            notes = "Выдает информацию о всех вышестоящих департаментах данного департамент",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses({
            @ApiResponse(code = SC_OK, response = DepartamentInformation.class, message = "Список вышестоящих департаментов."),
            @ApiResponse(code = SC_NOT_FOUND, message = "Департамент не найден")
    })
    @RequestMapping(path = "superordinate", method = RequestMethod.GET)
    public ResponseEntity<List<DepartamentInformation>> getSuperordinateDepartments(@RequestParam(value = "id") long id) {
        try {
            List<DepartamentInformation> departamentInformations = new ArrayList<>();
            Department department = departmentRepository.getOne(id);
            while (department.getParentDepartment() != null) {
                department = department.getParentDepartment();
                Optional<Employe> mainEmploye = department.getEmployes().stream().filter(employe -> employe.isMain()).findFirst();
                departamentInformations.add(new DepartamentInformation(department.getName(),
                        department.getCreateBy(),
                        mainEmploye.isPresent() ? mainEmploye.get() : null,
                        department.getEmployes().size()));
            }
            return new ResponseEntity(departamentInformations, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity("Департамент не найден", HttpStatus.NOT_FOUND);
        }
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
            Optional<Employe> mainEmploye = department.getEmployes().stream().filter(employe -> employe.isMain()).findFirst();
            departamentInformations.add(new DepartamentInformation(department.getName(),
                    department.getCreateBy(),
                    mainEmploye.isPresent() ? mainEmploye.get() : null,
                    department.getEmployes().size()));
        }
        idsDepartments.stream().forEach(newId -> findAllInformationAboutDepartment(newId, departamentInformations));
    }
}
