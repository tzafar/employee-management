package com.workmotion.onboarding;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;

import static com.workmotion.state.EmployeeState.ADDED;
import static com.workmotion.state.EmployeeState.INCHECK;
import static com.workmotion.state.EmployeeState.SECURITY_CHECK_STARTED;
import static com.workmotion.state.EmployeeState.WORK_PERMIT_CHECK_STARTED;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(OnboardingController.class)
public class OnboardingControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    EmployeeService employeeService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void returnUserInfo() throws Exception {
        String employeeId = "1";
        when(employeeService.findOne(employeeId)).thenReturn(new Employee(employeeId, "name", "address", Set.of(ADDED)));
        mockMvc.perform(get("/employees/" + employeeId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", equalTo("name")))
                .andExpect(jsonPath("$.address", equalTo("address")))
                .andExpect(jsonPath("$.states", hasSize(1)));
    }

    @Test
    public void testSaveEmployee() throws Exception {
        Employee employee = new Employee("1", "name", "address", Set.of(ADDED));
        when(employeeService.save(any())).thenReturn(employee);

        mockMvc.perform(post("/employees").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(employee)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", equalTo("name")))
                .andExpect(jsonPath("$.address", equalTo("address")))
                .andExpect(jsonPath("$.states", hasSize(1)));

    }

    @Test
    public void testStateChange() throws Exception {
        Employee employee = new Employee("1", "name", "address", Set.of(INCHECK, SECURITY_CHECK_STARTED, WORK_PERMIT_CHECK_STARTED));
        when(employeeService.changeStateFor(employee.getId(), "BEGIN_CHECK")).thenReturn(employee);

        mockMvc.perform(put("/employees/" + employee.getId() + "/BEGIN_CHECK").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", equalTo("name")))
                .andExpect(jsonPath("$.address", equalTo("address")))
                .andExpect(jsonPath("$.states", hasSize(3)));
    }
}
