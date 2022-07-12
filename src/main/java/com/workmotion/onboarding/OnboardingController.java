package com.workmotion.onboarding;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
public class OnboardingController {
    private final EmployeeService employeeService;

    @Autowired
    public OnboardingController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping(value = "/employees/{id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Employee> getEmployeeDetails(@PathVariable String id){
        return ResponseEntity.ok(employeeService.findOne(id));
    }

    @PostMapping(value = "/employees", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Employee> addEmployee(@RequestBody Employee employee){
        return ResponseEntity.status(CREATED).body(employeeService.save(employee));
    }

    @PutMapping(value = "/employees/{id}/{event}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Employee> updateStates(@PathVariable String event, @PathVariable String id){
        return ResponseEntity.ok(employeeService.changeStateFor(id, event));
    }
}
