package tn.esprit.studentmanagement.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import tn.esprit.studentmanagement.entities.Enrollment;
import tn.esprit.studentmanagement.entities.Status;
import tn.esprit.studentmanagement.services.IEnrollment;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EnrollmentController.class)
class EnrollmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IEnrollment enrollmentService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetAllEnrollment() throws Exception {
        List<Enrollment> enrollments = Arrays.asList(
                new Enrollment(1L, LocalDate.of(2024, 1, 15), 85.5, Status.ENROLLED, null, null),
                new Enrollment(2L, LocalDate.of(2024, 2, 20), 92.0, Status.COMPLETED, null, null)
        );

        when(enrollmentService.getAllEnrollments()).thenReturn(enrollments);

        mockMvc.perform(get("/Enrollment/getAllEnrollment"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].grade").value(85.5))
                .andExpect(jsonPath("$[1].grade").value(92.0));

        verify(enrollmentService, times(1)).getAllEnrollments();
    }

    @Test
    void testGetEnrollment() throws Exception {
        Enrollment enrollment = new Enrollment(1L, LocalDate.of(2024, 1, 15), 85.5, Status.ENROLLED, null, null);

        when(enrollmentService.getEnrollmentById(1L)).thenReturn(enrollment);

        mockMvc.perform(get("/Enrollment/getEnrollment/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idEnrollment").value(1))
                .andExpect(jsonPath("$.grade").value(85.5));

        verify(enrollmentService, times(1)).getEnrollmentById(1L);
    }

    @Test
    void testCreateEnrollment() throws Exception {
        Enrollment toCreate = new Enrollment(null, LocalDate.of(2024, 3, 10), 78.0, Status.ENROLLED, null, null);
        Enrollment created = new Enrollment(3L, LocalDate.of(2024, 3, 10), 78.0, Status.ENROLLED, null, null);

        when(enrollmentService.saveEnrollment(any(Enrollment.class))).thenReturn(created);

        mockMvc.perform(post("/Enrollment/createEnrollment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(toCreate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idEnrollment").value(3))
                .andExpect(jsonPath("$.grade").value(78.0));

        verify(enrollmentService, times(1)).saveEnrollment(any(Enrollment.class));
    }

    @Test
    void testUpdateEnrollment() throws Exception {
        Enrollment toUpdate = new Enrollment(1L, LocalDate.of(2024, 1, 15), 90.0, Status.COMPLETED, null, null);
        Enrollment updated = new Enrollment(1L, LocalDate.of(2024, 1, 15), 90.0, Status.COMPLETED, null, null);

        when(enrollmentService.saveEnrollment(any(Enrollment.class))).thenReturn(updated);

        mockMvc.perform(put("/Enrollment/updateEnrollment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(toUpdate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.grade").value(90.0));

        verify(enrollmentService, times(1)).saveEnrollment(any(Enrollment.class));
    }

    @Test
    void testDeleteEnrollment() throws Exception {
        doNothing().when(enrollmentService).deleteEnrollment(1L);

        mockMvc.perform(delete("/Enrollment/deleteEnrollment/1"))
                .andExpect(status().isOk());

        verify(enrollmentService, times(1)).deleteEnrollment(1L);
    }
}
