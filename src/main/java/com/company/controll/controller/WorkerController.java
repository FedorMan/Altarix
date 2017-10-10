package com.company.controll.controller;

import com.company.controll.repository.WorkerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("api/worker")
public class WorkerController {

    WorkerRepository workerRepository;
}
