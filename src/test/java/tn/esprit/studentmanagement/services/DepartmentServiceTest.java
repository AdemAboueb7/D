package tn.esprit.studentmanagement.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.studentmanagement.entities.Department;
import tn.esprit.studentmanagement.repositories.DepartmentRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DepartmentServiceTest {

    @Mock
    DepartmentRepository departmentRepository;

    @InjectMocks
    DepartmentService departmentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllDepartments() {
        List<Department> deps = Arrays.asList(
                new Department(1L, "HR", "Building A", "12345", "Alice", null),
                new Department(2L, "IT", "Building B", "67890", "Bob", null)
        );

        when(departmentRepository.findAll()).thenReturn(deps);

        List<Department> result = departmentService.getAllDepartments();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(departmentRepository, times(1)).findAll();
    }

    @Test
    void testGetDepartmentByIdFound() {
        Department dep = new Department(1L, "HR", "Building A", "12345", "Alice", null);
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(dep));

        Department result = departmentService.getDepartmentById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getIdDepartment());
        assertEquals("HR", result.getName());
        verify(departmentRepository, times(1)).findById(1L);
    }

    @Test
    void testGetDepartmentByIdNotFound() {
        when(departmentRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> departmentService.getDepartmentById(99L));
        verify(departmentRepository, times(1)).findById(99L);
    }

    @Test
    void testSaveDepartment() {
        Department toSave = new Department(null, "Finance", "Building C", "55555", "Carol", null);
        Department saved = new Department(3L, "Finance", "Building C", "55555", "Carol", null);

        when(departmentRepository.save(toSave)).thenReturn(saved);

        Department result = departmentService.saveDepartment(toSave);

        assertNotNull(result);
        assertEquals(3L, result.getIdDepartment());
        assertEquals("Finance", result.getName());
        verify(departmentRepository, times(1)).save(toSave);
    }

    @Test
    void testDeleteDepartment() {
        doNothing().when(departmentRepository).deleteById(1L);

        departmentService.deleteDepartment(1L);

        verify(departmentRepository, times(1)).deleteById(1L);
    }
}
