package tn.esprit.studentmanagement.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.studentmanagement.entities.Enrollment;
import tn.esprit.studentmanagement.entities.Status;
import tn.esprit.studentmanagement.repositories.EnrollmentRepository;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EnrollmentServiceTest {

    @Mock
    EnrollmentRepository enrollmentRepository;

    @InjectMocks
    EnrollmentService enrollmentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllEnrollments() {
        List<Enrollment> enrollments = Arrays.asList(
                new Enrollment(1L, LocalDate.of(2024, 1, 15), 85.5, Status.ACTIVE, null, null),
                new Enrollment(2L, LocalDate.of(2024, 2, 20), 92.0, Status.COMPLETED, null, null)
        );

        when(enrollmentRepository.findAll()).thenReturn(enrollments);

        List<Enrollment> result = enrollmentService.getAllEnrollments();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(85.5, result.get(0).getGrade());
        verify(enrollmentRepository, times(1)).findAll();
    }

    @Test
    void testGetAllEnrollmentsEmpty() {
        when(enrollmentRepository.findAll()).thenReturn(Arrays.asList());

        List<Enrollment> result = enrollmentService.getAllEnrollments();

        assertNotNull(result);
        assertEquals(0, result.size());
        verify(enrollmentRepository, times(1)).findAll();
    }

    @Test
    void testGetEnrollmentByIdFound() {
        Enrollment enrollment = new Enrollment(1L, LocalDate.of(2024, 1, 15), 85.5, Status.ACTIVE, null, null);
        when(enrollmentRepository.findById(1L)).thenReturn(Optional.of(enrollment));

        Enrollment result = enrollmentService.getEnrollmentById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getIdEnrollment());
        assertEquals(85.5, result.getGrade());
        verify(enrollmentRepository, times(1)).findById(1L);
    }

    @Test
    void testGetEnrollmentByIdNotFound() {
        when(enrollmentRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> enrollmentService.getEnrollmentById(99L));
        verify(enrollmentRepository, times(1)).findById(99L);
    }

    @Test
    void testGetEnrollmentByIdNotFoundMessage() {
        when(enrollmentRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> enrollmentService.getEnrollmentById(99L));
        assertTrue(exception.getMessage().contains("Enrollment not found"));
    }

    @Test
    void testSaveEnrollment() {
        Enrollment toSave = new Enrollment(null, LocalDate.of(2024, 3, 10), 78.0, Status.ACTIVE, null, null);
        Enrollment saved = new Enrollment(3L, LocalDate.of(2024, 3, 10), 78.0, Status.ACTIVE, null, null);

        when(enrollmentRepository.save(toSave)).thenReturn(saved);

        Enrollment result = enrollmentService.saveEnrollment(toSave);

        assertNotNull(result);
        assertEquals(3L, result.getIdEnrollment());
        assertEquals(78.0, result.getGrade());
        verify(enrollmentRepository, times(1)).save(toSave);
    }

    @Test
    void testDeleteEnrollment() {
        doNothing().when(enrollmentRepository).deleteById(1L);

        enrollmentService.deleteEnrollment(1L);

        verify(enrollmentRepository, times(1)).deleteById(1L);
    }
}
