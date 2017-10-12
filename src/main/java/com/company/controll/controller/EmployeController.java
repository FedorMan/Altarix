package com.company.controll.controller;

import com.company.controll.entity.Employe;
import com.company.controll.repository.EmployeRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("api/employe")
public class EmployeController {
    @Autowired
    EmployeRepository employeRepository;

    @RequestMapping(path = "/create",method = RequestMethod.POST)
    public ResponseEntity<String> create(RequestEntity<Employe> requestEntity){
        Employe employe = requestEntity.getBody();
        employeRepository.save(employe);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @RequestMapping(path="/all", method = RequestMethod.GET)
    public @ResponseBody List<Employe> getAllDepartment(){
        List<Employe> list =employeRepository.findAll();
        return list;
    }
}
