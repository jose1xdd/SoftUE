package com.backend.softue.controllers;

import com.backend.softue.services.DocenteApoyoIdeaServices;
import com.backend.softue.utils.checkSession.CheckSession;
import com.backend.softue.utils.response.ResponseConfirmation;
import com.backend.softue.utils.response.ResponseError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/docenteApoyoIdea")
public class DocenteApoyoIdeaController {
    @GetMapping("/test")
    public ResponseEntity<?> test() {
        return ResponseEntity.ok("pingpong");
    }
}
