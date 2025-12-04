package tn.esprit.studentmanagement.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.studentmanagement.entities.Student;
import tn.esprit.studentmanagement.repositories.StudentRepository;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StudentServiceTest {

    @Mock
    StudentRepository studentRepository;

    @InjectMocks
    StudentService studentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllStudents() {
        List<Student> students = Arrays.asList(
                new Student(1L, "John", "Doe", "john@example.com", "1234567890", LocalDate.of(2000, 1, 1), "Address 1", null, null),
                new Student(2L, "Jane", "Smith", "jane@example.com", "0987654321", LocalDate.of(2001, 2, 2), "Address 2", null, null)
        );

        when(studentRepository.findAll()).thenReturn(students);

        List<Student> result = studentService.getAllStudents();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("John", result.get(0).getFirstName());
        verify(studentRepository, times(1)).findAll();
    }

    @Test
    void testGetAllStudentsEmpty() {
        when(studentRepository.findAll()).thenReturn(Arrays.asList());

        List<Student> result = studentService.getAllStudents();

        assertNotNull(result);
        assertEquals(0, result.size());
        verify(studentRepository, times(1)).findAll();
    }

    @Test
    void testGetStudentByIdFound() {
        Student student = new Student(1L, "John", "Doe", "john@example.com", "1234567890", LocalDate.of(2000, 1, 1), "Address 1", null, null);
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        Student result = studentService.getStudentById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getIdStudent());
        assertEquals("John", result.getFirstName());
        verify(studentRepository, times(1)).findById(1L);
    }

    @Test
    void testGetStudentByIdNotFound() {
        when(studentRepository.findById(99L)).thenReturn(Optional.empty());

        Student result = studentService.getStudentById(99L);

        assertNull(result);
        verify(studentRepository, times(1)).findById(99L);
    }

    @Test
    void testSaveStudent() {
        Student toSave = new Student(null, "Alice", "Johnson", "alice@example.com", "5555555555", LocalDate.of(2002, 3, 3), "Address 3", null, null);
        Student saved = new Student(3L, "Alice", "Johnson", "alice@example.com", "5555555555", LocalDate.of(2002, 3, 3), "Address 3", null, null);

        when(studentRepository.save(toSave)).thenReturn(saved);

        Student result = studentService.saveStudent(toSave);

        assertNotNull(result);
        assertEquals(3L, result.getIdStudent());
        assertEquals("Alice", result.getFirstName());
        verify(studentRepository, times(1)).save(toSave);
    }

    @Test
    void testDeleteStudent() {
        doNothing().when(studentRepository).deleteById(1L);

        studentService.deleteStudent(1L);

        verify(studentRepository, times(1)).deleteById(1L);
    }
}
