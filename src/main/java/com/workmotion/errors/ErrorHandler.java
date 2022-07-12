package com.workmotion.errors;

import com.workmotion.state.Events;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(basePackages = "com.workmotion")
public class ErrorHandler {
    @ExceptionHandler(value = {EmployeeNotFoundException.class})
    public ResponseEntity handleEmployeeNotFound(EmployeeNotFoundException e){
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(value = EmployeeAlreadyExistsException.class)
    public ResponseEntity<Error> handleEmployeeAlreadyExists(){
        return ResponseEntity.badRequest().body(new Error("The user already exists"));
    }

    @ExceptionHandler(value = InvalidStateTransitionException.class)
    public ResponseEntity<Error> handInvalidStateTransitionException(){
        return ResponseEntity.badRequest().body(new Error("The state transition is invalid"));
    }

    @ExceptionHandler(value = InvalidActionException.class)
    public ResponseEntity<Error> handleInvalidActionException(InvalidActionException e){
        return ResponseEntity.badRequest().body(new Error("The action is not valid, it should be one of " + Events.events.keySet()));
    }
}
