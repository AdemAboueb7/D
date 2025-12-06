package tn.esprit.studentmanagement.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import tn.esprit.studentmanagement.entities.Student;
import tn.esprit.studentmanagement.services.IStudentService;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StudentController.class)
class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IStudentService studentService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetAllStudents() throws Exception {
        List<Student> students = Arrays.asList(
                new Student(1L, "John", "Doe", "john@example.com", "1234567890", LocalDate.of(2000, 1, 1), "Address 1", null, null),
                new Student(2L, "Jane", "Smith", "jane@example.com", "0987654321", LocalDate.of(2001, 2, 2), "Address 2", null, null)
        );

        when(studentService.getAllStudents()).thenReturn(students);

        mockMvc.perform(get("/students/getAllStudents"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[1].firstName").value("Jane"));

        verify(studentService, times(1)).getAllStudents();
    }

    @Test
    void testGetStudent() throws Exception {
        Student student = new Student(1L, "John", "Doe", "john@example.com", "1234567890", LocalDate.of(2000, 1, 1), "Address 1", null, null);

        when(studentService.getStudentById(1L)).thenReturn(student);

        mockMvc.perform(get("/students/getStudent/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.email").value("john@example.com"));

        verify(studentService, times(1)).getStudentById(1L);
    }

    @Test
    void testCreateStudent() throws Exception {
        Student toCreate = new Student(null, "Alice", "Johnson", "alice@example.com", "5555555555", LocalDate.of(2002, 3, 3), "Address 3", null, null);
        Student created = new Student(3L, "Alice", "Johnson", "alice@example.com", "5555555555", LocalDate.of(2002, 3, 3), "Address 3", null, null);

        when(studentService.saveStudent(any(Student.class))).thenReturn(created);

        mockMvc.perform(post("/students/createStudent")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(toCreate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idStudent").value(3))
                .andExpect(jsonPath("$.firstName").value("Alice"));

        verify(studentService, times(1)).saveStudent(any(Student.class));
    }

    @Test
    void testUpdateStudent() throws Exception {
        Student toUpdate = new Student(1L, "John", "Doe Updated", "john.updated@example.com", "1234567890", LocalDate.of(2000, 1, 1), "Address 1 Updated", null, null);
        Student updated = new Student(1L, "John", "Doe Updated", "john.updated@example.com", "1234567890", LocalDate.of(2000, 1, 1), "Address 1 Updated", null, null);

        when(studentService.saveStudent(any(Student.class))).thenReturn(updated);

        mockMvc.perform(put("/students/updateStudent")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(toUpdate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lastName").value("Doe Updated"));

        verify(studentService, times(1)).saveStudent(any(Student.class));
    }

    @Test
    void testDeleteStudent() throws Exception {
        doNothing().when(studentService).deleteStudent(1L);

        mockMvc.perform(delete("/students/deleteStudent/1"))
                .andExpect(status().isOk());

        verify(studentService, times(1)).deleteStudent(1L);
    }
}
