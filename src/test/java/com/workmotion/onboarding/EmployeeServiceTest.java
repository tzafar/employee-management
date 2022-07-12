package com.workmotion.onboarding;

import com.workmotion.errors.EmployeeNotFoundException;
import com.workmotion.errors.InvalidStateTransitionException;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static com.workmotion.state.EmployeeState.ACTIVE;
import static com.workmotion.state.EmployeeState.ADDED;
import static com.workmotion.state.EmployeeState.APPROVED;
import static com.workmotion.state.EmployeeState.INCHECK;
import static com.workmotion.state.EmployeeState.SECURITY_CHECK_FINISHED;
import static com.workmotion.state.EmployeeState.SECURITY_CHECK_STARTED;
import static com.workmotion.state.EmployeeState.WORK_PERMIT_CHECK_FINISHED;
import static com.workmotion.state.EmployeeState.WORK_PERMIT_CHECK_PENDING_VERIFICATION;
import static com.workmotion.state.EmployeeState.WORK_PERMIT_CHECK_STARTED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

class EmployeeServiceTest {
    EmployeeRepository employeeRepository = new EmployeeRepository();
    EmployeeService employeeService = new EmployeeService(employeeRepository);

    @Test
    public void shouldChangeStateFromBeginCheck() {
        employeeRepository.save(new Employee("1", "name", "address", Set.of(ADDED)));
        EmployeeService employeeService = new EmployeeService(employeeRepository);
        Employee actual = employeeService.changeStateFor("1", "BEGIN_CHECK");

        assertEquals(actual.getStates(), Set.of(INCHECK, SECURITY_CHECK_STARTED, WORK_PERMIT_CHECK_STARTED));

    }

    @Test
    public void shouldChangeStateFromCompleteInitialWorkPermitCheck() {
        Employee employee = new Employee("1", "name", "address", Set.of(ADDED));
        employeeRepository.save(employee);
        employee.setStates(Set.of(INCHECK, SECURITY_CHECK_STARTED, WORK_PERMIT_CHECK_STARTED));
        employeeRepository.update(employee);

        EmployeeService employeeService = new EmployeeService(employeeRepository);
        Employee actual = employeeService.changeStateFor("1", "COMPLETE_INITIAL_WORK_PERMIT_CHECK");

        assertEquals(actual.getStates(), Set.of(INCHECK, SECURITY_CHECK_STARTED, WORK_PERMIT_CHECK_PENDING_VERIFICATION));

    }

    @Test
    public void shouldChangeStateFromCompleteInitialWorkPermitCheckWithSecurityCheckFinished() {
        Employee employee = new Employee("1", "name", "address", Set.of(ADDED));
        employeeRepository.save(employee);
        employee.setStates(Set.of(INCHECK, SECURITY_CHECK_FINISHED, WORK_PERMIT_CHECK_STARTED));
        employeeRepository.update(employee);

        EmployeeService employeeService = new EmployeeService(employeeRepository);
        Employee actual = employeeService.changeStateFor("1", "COMPLETE_INITIAL_WORK_PERMIT_CHECK");

        assertEquals(actual.getStates(), Set.of(INCHECK, SECURITY_CHECK_FINISHED, WORK_PERMIT_CHECK_PENDING_VERIFICATION));

    }

    @Test
    public void shouldChangeStateFromFinishWorkPermitCheck() {
        Employee employee = new Employee("1", "name", "address", Set.of(ADDED));
        employeeRepository.save(employee);
        employee.setStates(Set.of(INCHECK, SECURITY_CHECK_STARTED, WORK_PERMIT_CHECK_PENDING_VERIFICATION));
        employeeRepository.update(employee);

        EmployeeService employeeService = new EmployeeService(employeeRepository);
        Employee actual = employeeService.changeStateFor("1", "FINISH_WORK_PERMIT_CHECK");

        assertEquals(actual.getStates(), Set.of(INCHECK, SECURITY_CHECK_STARTED, WORK_PERMIT_CHECK_FINISHED));

    }

    @Test
    public void shouldChangeStateFromFinishWorkPermitCheckWhenSecurityCheckFinished() {
        Employee employee = new Employee("1", "name", "address", Set.of(ADDED));
        employeeRepository.save(employee);
        employee.setStates(Set.of(INCHECK, SECURITY_CHECK_FINISHED, WORK_PERMIT_CHECK_PENDING_VERIFICATION));
        employeeRepository.update(employee);

        EmployeeService employeeService = new EmployeeService(employeeRepository);
        Employee actual = employeeService.changeStateFor("1", "FINISH_WORK_PERMIT_CHECK");

        assertEquals(actual.getStates(), Set.of(APPROVED));

    }

    @Test
    public void shouldChangeStateFromFinishSecurityCheck() {
        Employee employee = new Employee("1", "name", "address", Set.of(ADDED));
        employeeRepository.save(employee);
        employee.setStates(Set.of(INCHECK, SECURITY_CHECK_STARTED, WORK_PERMIT_CHECK_STARTED));
        employeeRepository.update(employee);

        EmployeeService employeeService = new EmployeeService(employeeRepository);
        Employee actual = employeeService.changeStateFor("1", "FINISH_SECURITY_CHECK");

        assertEquals(actual.getStates(), Set.of(INCHECK, SECURITY_CHECK_FINISHED, WORK_PERMIT_CHECK_STARTED));

    }

    @Test
    public void shouldChangeStateFromFinishSecurityCheckWhenWorkPermitCheckPending() {
        Employee employee = new Employee("1", "name", "address", Set.of(ADDED));
        employeeRepository.save(employee);
        employee.setStates(Set.of(INCHECK, SECURITY_CHECK_STARTED, WORK_PERMIT_CHECK_PENDING_VERIFICATION));
        employeeRepository.update(employee);

        EmployeeService employeeService = new EmployeeService(employeeRepository);
        Employee actual = employeeService.changeStateFor("1", "FINISH_SECURITY_CHECK");

        assertEquals(actual.getStates(), Set.of(INCHECK, SECURITY_CHECK_FINISHED, WORK_PERMIT_CHECK_PENDING_VERIFICATION));
    }

    @Test
    public void shouldChangeStateFromFinishSecurityCheckWhenWorkPermitCheckFinished() {
        Employee employee = new Employee("1", "name", "address", Set.of(ADDED));
        employeeRepository.save(employee);
        employee.setStates(Set.of(INCHECK, SECURITY_CHECK_STARTED, WORK_PERMIT_CHECK_FINISHED));
        employeeRepository.update(employee);

        EmployeeService employeeService = new EmployeeService(employeeRepository);
        Employee actual = employeeService.changeStateFor("1", "FINISH_SECURITY_CHECK");

        assertEquals(actual.getStates(), Set.of(APPROVED));
    }

    @Test
    public void shouldChangeStateFromActivate() {
        Employee employee = new Employee("1", "name", "address", Set.of(ADDED));
        employeeRepository.save(employee);
        employee.setStates(Set.of(APPROVED));
        employeeRepository.update(employee);

        Employee actual = employeeService.changeStateFor("1", "ACTIVATE");

        assertEquals(actual.getStates(), Set.of(ACTIVE));
    }

    @Test
    public void throwErrorOnInvalidStateChange() {
        Employee employee = new Employee("id", "addres", "address", Set.of(ADDED));
        employeeRepository.save(employee);
        assertThrows(InvalidStateTransitionException.class, () ->
                employeeService.changeStateFor("id", "ACTIVATE")
        );
    }

    @Test
    void shouldThrowEmployeeNotFound() {
        EmployeeService employeeService = new EmployeeService(employeeRepository);
        assertThrows(EmployeeNotFoundException.class, () -> {
            employeeService.findOne("1");
        });
    }


}
