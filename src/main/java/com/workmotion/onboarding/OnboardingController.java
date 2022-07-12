package com.workmotion.onboarding;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static javax.servlet.http.HttpServletResponse.SC_CREATED;
import static javax.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
import static javax.servlet.http.HttpServletResponse.SC_NOT_FOUND;
import static javax.servlet.http.HttpServletResponse.SC_OK;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
public class OnboardingController {
    private final EmployeeService employeeService;

    @Autowired
    public OnboardingController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @ApiOperation(value = "Get Employee", notes = "This endpoint gets details of an employee", produces = APPLICATION_JSON_VALUE)
    @ApiResponses(value = {@ApiResponse(code = SC_OK, message = "Successfully retrieved employee"),
            @ApiResponse(code = SC_NOT_FOUND, message = "Employee not found"),
            @ApiResponse(code = SC_INTERNAL_SERVER_ERROR, message = "Internal error")})
    @GetMapping(value = "/employees/{id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Employee> getEmployeeDetails(@PathVariable String id) {
        return ResponseEntity.ok(employeeService.findOne(id));
    }

    @ApiOperation(value = "Create Employee", notes = "This endpoint saves details of an employee", produces = APPLICATION_JSON_VALUE)
    @ApiResponses(value = {@ApiResponse(code = SC_CREATED, message = "Successfully created employee"),
            @ApiResponse(code = SC_BAD_REQUEST, message = "Something wrong with request data"),
            @ApiResponse(code = SC_INTERNAL_SERVER_ERROR, message = "Internal error")})
    @PostMapping(value = "/employees", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Employee> addEmployee(@RequestBody Employee employee) {
        return ResponseEntity.status(CREATED).body(employeeService.save(employee));
    }

    @ApiOperation(value = "Change Employee State", notes = "This endpoint updates the state of an employee", produces = APPLICATION_JSON_VALUE)
    @ApiResponses(value = {@ApiResponse(code = SC_OK, message = "Successfully updated state"),
            @ApiResponse(code = SC_NOT_FOUND, message = "Employee to be updated is not found"),
            @ApiResponse(code = SC_BAD_REQUEST, message = "Either the state transition is not valid or action provided is not valid"),
            @ApiResponse(code = SC_INTERNAL_SERVER_ERROR, message = "Internal error")})
    @PutMapping(value = "/employees/{id}/{event}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Employee> updateStates(
            @ApiParam(value = "The name of the event to be performed", allowableValues = "BEGIN_CHECK, COMPLETE_INITIAL_WORK_PERMIT_CHECK, FINISH_WORK_PERMIT_CHECK, FINISH_SECURITY_CHECK, ACTIVATE")
            @PathVariable(required = true) String event,
            @ApiParam(value="The id of the employee") @PathVariable String id) {
        return ResponseEntity.ok(employeeService.changeStateFor(id, event));
    }
}
