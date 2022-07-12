package com.workmotion.state;

import java.util.Set;

import static com.workmotion.state.EmployeeState.ACTIVE;
import static com.workmotion.state.EmployeeState.APPROVED;
import static com.workmotion.state.EmployeeState.INCHECK;
import static com.workmotion.state.EmployeeState.SECURITY_CHECK_FINISHED;
import static com.workmotion.state.EmployeeState.SECURITY_CHECK_STARTED;
import static com.workmotion.state.EmployeeState.WORK_PERMIT_CHECK_FINISHED;
import static com.workmotion.state.EmployeeState.WORK_PERMIT_CHECK_PENDING_VERIFICATION;
import static com.workmotion.state.EmployeeState.WORK_PERMIT_CHECK_STARTED;

public enum EmployeeStateMachine {
    BEGIN_CHECK {
        @Override
        public ValidTransitions sourceAndTargetState() {
            ValidTransitions validTransitions = new ValidTransitions();
            validTransitions.addTransition(Set.of(EmployeeState.ADDED), Set.of(EmployeeState.INCHECK,
                    EmployeeState.SECURITY_CHECK_STARTED, EmployeeState.WORK_PERMIT_CHECK_STARTED));

            return validTransitions;
        }
    },

    FINISH_SECURITY_CHECK {
        @Override
        public ValidTransitions sourceAndTargetState() {
            ValidTransitions validTransitions = new ValidTransitions();
            validTransitions.addTransition(Set.of(INCHECK, SECURITY_CHECK_STARTED, WORK_PERMIT_CHECK_STARTED), Set.of(INCHECK, SECURITY_CHECK_FINISHED, WORK_PERMIT_CHECK_STARTED));
            validTransitions.addTransition(Set.of(INCHECK, SECURITY_CHECK_STARTED, WORK_PERMIT_CHECK_PENDING_VERIFICATION), Set.of(INCHECK, SECURITY_CHECK_FINISHED, WORK_PERMIT_CHECK_PENDING_VERIFICATION));
            validTransitions.addTransition(Set.of(INCHECK, SECURITY_CHECK_STARTED, WORK_PERMIT_CHECK_FINISHED), Set.of(APPROVED));

            return validTransitions;
        }
    },

    COMPLETE_INITIAL_WORK_PERMIT_CHECK {
        @Override
        public ValidTransitions sourceAndTargetState() {
            ValidTransitions validTransitions = new ValidTransitions();
            validTransitions.addTransition(Set.of(INCHECK, SECURITY_CHECK_STARTED, WORK_PERMIT_CHECK_STARTED), Set.of(INCHECK, SECURITY_CHECK_STARTED, WORK_PERMIT_CHECK_PENDING_VERIFICATION));
            validTransitions.addTransition(Set.of(INCHECK, SECURITY_CHECK_FINISHED, WORK_PERMIT_CHECK_STARTED), Set.of(INCHECK, SECURITY_CHECK_FINISHED, WORK_PERMIT_CHECK_PENDING_VERIFICATION));
            return validTransitions;
        }
    },

    FINISH_WORK_PERMIT_CHECK {
        @Override
        public ValidTransitions sourceAndTargetState() {
            ValidTransitions validTransitions = new ValidTransitions();
            validTransitions.addTransition(Set.of(INCHECK, SECURITY_CHECK_FINISHED, WORK_PERMIT_CHECK_PENDING_VERIFICATION), Set.of(APPROVED));
            validTransitions.addTransition(Set.of(INCHECK, SECURITY_CHECK_STARTED, WORK_PERMIT_CHECK_PENDING_VERIFICATION), Set.of(INCHECK, SECURITY_CHECK_STARTED, WORK_PERMIT_CHECK_FINISHED));
            return validTransitions;
        }
    },

    ACTIVATE {
        @Override
        public ValidTransitions sourceAndTargetState() {
            ValidTransitions validTransitions = new ValidTransitions();
            validTransitions.addTransition(Set.of(APPROVED), Set.of(ACTIVE));
            return validTransitions;
        }
    };

    public abstract ValidTransitions sourceAndTargetState();
}
