package com.workmotion.state;

import java.util.HashMap;
import java.util.Map;

public class Events {
    public static Map<String, EmployeeStateMachine> events = new HashMap<>();

    static {
        events.put("BEGIN_CHECK", EmployeeStateMachine.BEGIN_CHECK);
        events.put("COMPLETE_INITIAL_WORK_PERMIT_CHECK", EmployeeStateMachine.COMPLETE_INITIAL_WORK_PERMIT_CHECK);
        events.put("FINISH_WORK_PERMIT_CHECK", EmployeeStateMachine.FINISH_WORK_PERMIT_CHECK);
        events.put("FINISH_SECURITY_CHECK", EmployeeStateMachine.FINISH_SECURITY_CHECK);
        events.put("ACTIVATE", EmployeeStateMachine.ACTIVATE);
    }
}
