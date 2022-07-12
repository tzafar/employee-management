package com.workmotion.onboarding;

import com.workmotion.errors.EmployeeAlreadyExistsException;
import com.workmotion.errors.EmployeeNotFoundException;
import com.workmotion.errors.InvalidActionException;
import com.workmotion.errors.InvalidStateTransitionException;
import com.workmotion.state.EmployeeStateMachine;
import com.workmotion.state.Events;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

import static com.workmotion.state.EmployeeState.ADDED;

@Component
public class EmployeeService {
    private EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public Employee findOne(String id) {
        Employee employee = this.employeeRepository.findOne(id);
        if (employee == null) {
            throw new EmployeeNotFoundException();
        }
        return employee;
    }

    public Employee save(Employee employee) {
            if (employeeRepository.findOne(employee.getId()) != null) {
                throw new EmployeeAlreadyExistsException();
            } else {
                employee.setStates(Set.of(ADDED));
                employeeRepository.save(employee);
            }
        return employee;
    }

    public Employee changeStateFor(String id, String event) {
        Employee employee = this.employeeRepository.findOne(id);
        if (employee == null) {
            throw new EmployeeNotFoundException();
        }

        EmployeeStateMachine employeeStateMachine = Events.events.get(event);
        if (employeeStateMachine == null) {
            throw new InvalidActionException();
        }
        if (employeeStateMachine.sourceAndTargetState().getValidTransitions().containsKey(employee.getStates())) {
            employee.setStates(employeeStateMachine.sourceAndTargetState().getValidTransitions().get(employee.getStates()));
            this.employeeRepository.update(employee);
            return employee;
        }
        throw new InvalidStateTransitionException();
    }
}
