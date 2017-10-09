package com.company.controll.controller;

import com.company.controll.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("api/department")
public class DepartmentController {
    @Autowired
    DepartmentRepository departmentRepository;
}
