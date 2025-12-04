package tn.esprit.studentmanagement.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import tn.esprit.studentmanagement.entities.Department;
import tn.esprit.studentmanagement.services.IDepartmentService;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DepartmentController.class)
class DepartmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IDepartmentService departmentService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetAllDepartments() throws Exception {
        List<Department> departments = Arrays.asList(
                new Department(1L, "HR", "Building A", "12345", "Alice", null),
                new Department(2L, "IT", "Building B", "67890", "Bob", null)
        );

        when(departmentService.getAllDepartments()).thenReturn(departments);

        mockMvc.perform(get("/Depatment/getAllDepartment"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("HR"))
                .andExpect(jsonPath("$[1].name").value("IT"));

        verify(departmentService, times(1)).getAllDepartments();
    }

    @Test
    void testGetDepartmentById() throws Exception {
        Department department = new Department(1L, "HR", "Building A", "12345", "Alice", null);

        when(departmentService.getDepartmentById(1L)).thenReturn(department);

        mockMvc.perform(get("/Depatment/getDepartment/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idDepartment").value(1))
                .andExpect(jsonPath("$.name").value("HR"));

        verify(departmentService, times(1)).getDepartmentById(1L);
    }

    @Test
    void testCreateDepartment() throws Exception {
        Department toCreate = new Department(null, "Finance", "Building C", "55555", "Carol", null);
        Department created = new Department(3L, "Finance", "Building C", "55555", "Carol", null);

        when(departmentService.saveDepartment(any(Department.class))).thenReturn(created);

        mockMvc.perform(post("/Depatment/createDepartment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(toCreate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idDepartment").value(3))
                .andExpect(jsonPath("$.name").value("Finance"));

        verify(departmentService, times(1)).saveDepartment(any(Department.class));
    }

    @Test
    void testUpdateDepartment() throws Exception {
        Department toUpdate = new Department(1L, "Human Resources", "Building A Updated", "12345", "Alice Updated", null);
        Department updated = new Department(1L, "Human Resources", "Building A Updated", "12345", "Alice Updated", null);

        when(departmentService.saveDepartment(any(Department.class))).thenReturn(updated);

        mockMvc.perform(put("/Depatment/updateDepartment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(toUpdate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Human Resources"));

        verify(departmentService, times(1)).saveDepartment(any(Department.class));
    }

    @Test
    void testDeleteDepartment() throws Exception {
        doNothing().when(departmentService).deleteDepartment(1L);

        mockMvc.perform(delete("/Depatment/deleteDepartment/1"))
                .andExpect(status().isOk());

        verify(departmentService, times(1)).deleteDepartment(1L);
    }
}
