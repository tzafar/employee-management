package com.workmotion.onboarding;

import com.workmotion.state.EmployeeState;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;

@AllArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
public class Employee {
    private String id;
    private String name;
    private String address;
    @Setter
    private Set<EmployeeState> states;
}
