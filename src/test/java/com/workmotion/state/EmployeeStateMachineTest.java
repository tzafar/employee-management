package com.workmotion.state;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;
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
import static org.junit.jupiter.api.Assertions.*;

class EmployeeStateMachineTest {

    @Test
    void actionBeginCheckShouldResultInCorrectStatesOfUser() {
        EmployeeStateMachine action = EmployeeStateMachine.BEGIN_CHECK;
        Set<EmployeeState> existingStates = Set.of(ADDED);
        Set<EmployeeState> expectedStates = Set.of(INCHECK, WORK_PERMIT_CHECK_STARTED, SECURITY_CHECK_STARTED);

        Map<Set<EmployeeState>, Set<EmployeeState>> validTransitionForBeingCheck = action.sourceAndTargetState().getValidTransitions();
        Set<EmployeeState> actualStates = validTransitionForBeingCheck.get(existingStates);

        assertEquals(actualStates, expectedStates);
    }

    @DisplayName("FINISH SECURITY CHECK should result in correct state when user state is INCHECK, SECURITY_CHECK_STARTED, WORK_PERMIT_CHECK_STARTED")
    @Test
    void finishSecurityCheckScenario1() {
        EmployeeStateMachine action = EmployeeStateMachine.FINISH_SECURITY_CHECK;
        Set<EmployeeState> existingStates = Set.of(INCHECK, SECURITY_CHECK_STARTED, WORK_PERMIT_CHECK_STARTED);
        Set<EmployeeState> expectedStates = Set.of(INCHECK, SECURITY_CHECK_FINISHED, WORK_PERMIT_CHECK_STARTED);

        Map<Set<EmployeeState>, Set<EmployeeState>> validTransitionForBeingCheck = action.sourceAndTargetState().getValidTransitions();
        Set<EmployeeState> actualStates = validTransitionForBeingCheck.get(existingStates);

        assertEquals(actualStates, expectedStates);
    }

    @DisplayName("FINISH SECURITY CHECK should result in correct state when user state is INCHECK, SECURITY_CHECK_STARTED, WORK_PERMIT_CHECK_PENDING_VERIFICATION")
    @Test
    void finishSecurityCheckScenario2() {
        EmployeeStateMachine action = EmployeeStateMachine.FINISH_SECURITY_CHECK;
        Set<EmployeeState> existingStates = Set.of(INCHECK, SECURITY_CHECK_STARTED, WORK_PERMIT_CHECK_PENDING_VERIFICATION);
        Set<EmployeeState> expectedStates = Set.of(INCHECK, SECURITY_CHECK_FINISHED, WORK_PERMIT_CHECK_PENDING_VERIFICATION);

        Map<Set<EmployeeState>, Set<EmployeeState>> validTransitionForBeingCheck = action.sourceAndTargetState().getValidTransitions();
        Set<EmployeeState> actualStates = validTransitionForBeingCheck.get(existingStates);

        assertEquals(actualStates, expectedStates);
    }

    @DisplayName("FINISH SECURITY CHECK should result in correct state when user state is INCHECK, SECURITY_CHECK_STARTED, WORK_PERMIT_CHECK_FINISHED")
    @Test
    void finishSecurityCheckScenario3() {
        EmployeeStateMachine action = EmployeeStateMachine.FINISH_SECURITY_CHECK;
        Set<EmployeeState> existingStates = Set.of(INCHECK, SECURITY_CHECK_STARTED, WORK_PERMIT_CHECK_FINISHED);
        Set<EmployeeState> expectedStates = Set.of(APPROVED);

        Map<Set<EmployeeState>, Set<EmployeeState>> validTransitionForBeingCheck = action.sourceAndTargetState().getValidTransitions();
        Set<EmployeeState> actualStates = validTransitionForBeingCheck.get(existingStates);

        assertEquals(actualStates, expectedStates);
    }

    @DisplayName("COMPLETE INITIAL WORK PERMIT CHECK should result in correct state when user state is INCHECK, SECURITY_CHECK_STARTED, WORK_PERMIT_CHECK_STARTED")
    @Test
    void completeInitialWorkPermitCheckScenario1() {
        EmployeeStateMachine action = EmployeeStateMachine.COMPLETE_INITIAL_WORK_PERMIT_CHECK;
        Set<EmployeeState> existingStates = Set.of(INCHECK, SECURITY_CHECK_STARTED, WORK_PERMIT_CHECK_STARTED);
        Set<EmployeeState> expectedStates = Set.of(INCHECK, SECURITY_CHECK_STARTED, WORK_PERMIT_CHECK_PENDING_VERIFICATION);

        Map<Set<EmployeeState>, Set<EmployeeState>> validTransitionForBeingCheck = action.sourceAndTargetState().getValidTransitions();
        Set<EmployeeState> actualStates = validTransitionForBeingCheck.get(existingStates);

        assertEquals(actualStates, expectedStates);
    }

    @DisplayName("COMPLETE INITIAL WORK PERMIT CHECK should result in correct state when user state is INCHECK, SECURITY_CHECK_FINISHED, WORK_PERMIT_CHECK_STARTED")
    @Test
    void completeInitialWorkPermitCheckScenario2() {
        EmployeeStateMachine action = EmployeeStateMachine.COMPLETE_INITIAL_WORK_PERMIT_CHECK;
        Set<EmployeeState> existingStates = Set.of(INCHECK, SECURITY_CHECK_FINISHED, WORK_PERMIT_CHECK_STARTED);
        Set<EmployeeState> expectedStates = Set.of(INCHECK, SECURITY_CHECK_FINISHED, WORK_PERMIT_CHECK_PENDING_VERIFICATION);

        Map<Set<EmployeeState>, Set<EmployeeState>> validTransitionForBeingCheck = action.sourceAndTargetState().getValidTransitions();
        Set<EmployeeState> actualStates = validTransitionForBeingCheck.get(existingStates);

        assertEquals(actualStates, expectedStates);
    }

    @DisplayName("FINISH WORK PERMIT CHECK should result in correct state when user state is INCHECK, SECURITY_CHECK_FINISHED, WORK_PERMIT_CHECK_PENDING_VERIFICATION")
    @Test
    void finishWorkPermitCheckScenario2() {
        EmployeeStateMachine action = EmployeeStateMachine.FINISH_WORK_PERMIT_CHECK;
        Set<EmployeeState> existingStates = Set.of(INCHECK, SECURITY_CHECK_FINISHED, WORK_PERMIT_CHECK_PENDING_VERIFICATION);
        Set<EmployeeState> expectedStates = Set.of(APPROVED);

        Map<Set<EmployeeState>, Set<EmployeeState>> validTransitionForBeingCheck = action.sourceAndTargetState().getValidTransitions();
        Set<EmployeeState> actualStates = validTransitionForBeingCheck.get(existingStates);

        assertEquals(actualStates, expectedStates);
    }

    @DisplayName("ACTIVATE should result in correct state when user state is APPROVED")
    @Test
    void activate() {
        EmployeeStateMachine action = EmployeeStateMachine.ACTIVATE;
        Set<EmployeeState> existingStates = Set.of(APPROVED);
        Set<EmployeeState> expectedStates = Set.of(ACTIVE);

        Map<Set<EmployeeState>, Set<EmployeeState>> validTransitionForBeingCheck = action.sourceAndTargetState().getValidTransitions();
        Set<EmployeeState> actualStates = validTransitionForBeingCheck.get(existingStates);

        assertEquals(actualStates, expectedStates);
    }


}
