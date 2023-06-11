package com.backend.softue.controllers;

import com.backend.softue.services.TestServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/apoyoIdea")
public class ApoyoIdeaController {
    @GetMapping("/test")
    public ResponseEntity<?> test() {
        return ResponseEntity.ok("pingpong");
    }
}
