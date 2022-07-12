package com.workmotion.onboarding;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class EmployeeRepository {

    private Map<String, Employee> employees;

    public EmployeeRepository() {
        this.employees = new HashMap<>();
    }

    public Employee findOne(String employeeId) {
        return this.employees.get(employeeId);
    }

    public Employee save(Employee employee) {
        employees.put(employee.getId(), employee);
        return employee;
    }

    public void update(Employee employee) {
        this.employees.put(employee.getId(), employee);
    }
}
