package com.workmotion.state;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ValidTransitions {

    private final Map<Set<EmployeeState>, Set<EmployeeState>> validTransitions;

    public ValidTransitions(){
        validTransitions = new HashMap<>();
    }

    public Map<Set<EmployeeState>, Set<EmployeeState>> getValidTransitions(){
        return this.validTransitions;
    }

    public void addTransition(Set<EmployeeState> source, Set<EmployeeState> target) {
        this.validTransitions.put(source, target);
    }
}
