package com.company.controll.controller;

import com.company.controll.entity.Department;
import com.company.controll.entity.Employe;
import com.company.controll.repository.DepartmentRepository;
import com.company.controll.repository.EmployeRepository;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static javax.servlet.http.HttpServletResponse.*;

@Controller
@RequestMapping("api/employe")
public class EmployeController {
    @Autowired
    private EmployeRepository employeRepository;
    @Autowired
    private DepartmentRepository departmentRepository;

    @ApiOperation(value = "Получить Сотрудников департамента",
            notes = "Возвращает список сотрудников департамента",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses({
            @ApiResponse(code = SC_OK, message = "Список возвращен"),
            @ApiResponse(code = SC_NOT_FOUND, message = "Департамент не найден")
    })
    @RequestMapping(path = "/department", method = RequestMethod.GET)
    public ResponseEntity<List<Employe>> getByDepartment(@RequestParam(value = "id") long id) {
        try {
            Department department = departmentRepository.getOne(id);
            return new ResponseEntity(employeRepository.findByDepartmentId(id), HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity("Департамент не найден", HttpStatus.NOT_FOUND);
        }
    }

    @ApiOperation(value = "Создать сотрудика",
            notes = "Создает сотрудника",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses({
            @ApiResponse(code = SC_CREATED, message = "Сотрудник создан"),
    })
    @RequestMapping(path = "/create", method = RequestMethod.POST)
    public ResponseEntity<String> create(RequestEntity<Employe> requestEntity) {
        Employe employe = requestEntity.getBody();
        employeRepository.save(employe);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @ApiOperation(value = "Получить информацию о сотруднике",
            notes = "Возвращает информацию о сотруднике",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses({
            @ApiResponse(code = SC_OK, message = "Информация возвращена"),
            @ApiResponse(code = SC_NOT_FOUND, message = "Сотрудник не  найден")
    })
    @RequestMapping(path = "/information", method = RequestMethod.GET)
    public ResponseEntity<Employe> getInformation(@RequestParam(value = "id") long id) {
        try {
            return new ResponseEntity(employeRepository.getOne(id), HttpStatus.OK);
        }catch (EntityNotFoundException e) {
            return new ResponseEntity("Сотрудник не найден", HttpStatus.NOT_FOUND);
        }
    }

    @ApiOperation(value = "Редактировать сведения о сотруднике",
            notes = "Изменяет информацию о сотруднике",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses({
            @ApiResponse(code = SC_OK, message = "Информация изменена"),
            @ApiResponse(code = SC_NOT_FOUND, message = "Сотрудник не  найден")
    })
    @RequestMapping(path = "/update", method = RequestMethod.POST)
    public ResponseEntity<String> update(RequestEntity<Employe> requestEmploye) {
        try {
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
        }catch (EntityNotFoundException e) {
            return new ResponseEntity("Сотрудник не найден", HttpStatus.NOT_FOUND);
        }
    }

    @ApiOperation(value = "Уволить сотрудника",
            notes = "Выставляет сотруднику дату увольнения",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses({
            @ApiResponse(code = SC_OK, message = "Сотрудник уволен"),
            @ApiResponse(code = SC_NOT_FOUND, message = "Сотрудник не  найден")
    })
    @RequestMapping(path = "/dismiss", method = RequestMethod.GET)
    public ResponseEntity<String> dismiss(@RequestParam(value = "id") long id, @RequestParam(value = "year") int year, @RequestParam(value = "month") int month, @RequestParam(value = "day") int day) {
        try {
            Employe employe = employeRepository.getOne(id);
            LocalDate endDate = LocalDate.of(year, month, day);
            employe.setEndDate(endDate);
            employeRepository.flush();
            return ResponseEntity.status(HttpStatus.OK).build();
        }catch (EntityNotFoundException e) {
            return new ResponseEntity("Сотрудник не найден", HttpStatus.NOT_FOUND);
        }
    }

    @ApiOperation(value = "Перевести сотрудника в другой департамент",
            notes = "Переводит сотрудника в другой департамент",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses({
            @ApiResponse(code = SC_OK, message = "Сотрудник переведен"),
            @ApiResponse(code = SC_NOT_FOUND, message = "Сотрудник или департамент не найден")
    })
    @RequestMapping(path = "/transfer", method = RequestMethod.GET)
    public ResponseEntity<String> transfer(@RequestParam(value = "id") long id, @RequestParam(value = "idDepartment") long idDepatment) {
        try {
            Employe employe = employeRepository.getOne(id);
            Department department = departmentRepository.getOne(idDepatment);
            employe.setDepartment(department);
            employeRepository.flush();
            return ResponseEntity.status(HttpStatus.OK).build();
        }catch (EntityNotFoundException e) {
            return new ResponseEntity("Сотрудник, либо департамент не найден", HttpStatus.NOT_FOUND);
        }
    }

    @ApiOperation(value = "Перевести сотрудников департамента в другой департамент",
            notes = "Переводит сотрудников в другой департамент",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses({
            @ApiResponse(code = SC_OK, message = "Сотрудники переведены"),
            @ApiResponse(code = SC_NOT_FOUND, message = "Департамент не найден")
    })
    @RequestMapping(path = "/transferall", method = RequestMethod.GET)
    public ResponseEntity<String > transferDepartment(@RequestParam(value = "idCurrent") long idCurrent, @RequestParam(value = "idDepartment") long idDepatment) {
        try {
            Department currentDepartment = departmentRepository.getOne(idCurrent);
            Department department = departmentRepository.getOne(idDepatment);
            currentDepartment.getEmployes().stream().forEach(employe -> employe.setDepartment(department));
            employeRepository.flush();
            return ResponseEntity.status(HttpStatus.OK).build();
        }catch (EntityNotFoundException e) {
            return new ResponseEntity("Департамент не найден", HttpStatus.NOT_FOUND);
        }
    }

    @ApiOperation(value = "Получить Руководителя",
            notes = "Возвращает руководителя указанного сотрудника",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses({
            @ApiResponse(code = SC_OK, message = "Руководитель возвращен"),
            @ApiResponse(code = SC_NOT_FOUND, message = "Сотрудник не найден")
    })
    @RequestMapping(path = "/manager", method = RequestMethod.GET)
    public ResponseEntity<Employe> getInformationOfManager(@RequestParam(value = "id") long id) {
        try {
            Employe employe = employeRepository.getOne(id);
            if (employe.isMain() && employe.getDepartment().getParentDepartment() == null) return new ResponseEntity(null,HttpStatus.OK);
            Department department = employe.isMain() ? employe.getDepartment().getParentDepartment() : employe.getDepartment();
            Optional<Employe> manager = department.getEmployes().stream().filter(e -> e.isMain()).findFirst();
            return manager.isPresent() ? new ResponseEntity(manager.get(),HttpStatus.OK) : new ResponseEntity(null,HttpStatus.OK);
        }catch (EntityNotFoundException e) {
            return new ResponseEntity("Сотрудник не найден", HttpStatus.NOT_FOUND);
        }

    }

    @ApiOperation(value = "Поиск по дню рождения",
            notes = "Возвращает список сотрудников с указанным днем рождения",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses({
            @ApiResponse(code = SC_OK, message = "Список возвращен"),
    })
    @RequestMapping(path = "/find", method = RequestMethod.GET)
    public ResponseEntity<List<Employe>> findByBirthday(@RequestParam(value = "year") int year, @RequestParam(value = "month") int month, @RequestParam(value = "day") int day){
        LocalDate date = LocalDate.of(year,month,day);
        return new ResponseEntity(employeRepository.findByBirthday(date).stream().filter(employe -> employe.getEndDate() == null).collect(Collectors.toList()),HttpStatus.OK);
    }
}
