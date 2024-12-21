package org.example.employees.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ApiRestController {

    @PostMapping("/modifyhours/{id}/{hours}")
    public ResponseEntity<String> modifyHours(@PathVariable int id, @PathVariable int hours) {
        System.out.println(id + " | " + hours);
        return ResponseEntity.ok("Modified");
    }
}
