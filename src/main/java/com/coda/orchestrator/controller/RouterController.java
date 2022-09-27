package com.coda.orchestrator.controller;

import com.coda.orchestrator.services.Impl.DemoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @author prasad
 */
@RestController
@RequestMapping("/router/v1")
@Slf4j
@Controller
public class RouterController {

    @Autowired
    DemoService demoService;

    @PostMapping("/copy")
    public String postRequest(@RequestBody String request){
         return demoService.postToDemoService(request);
    }

    @GetMapping("/health")
    public String getHealth(){
        return "All Good";
    }
}
