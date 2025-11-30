package tn.esprit.studentmanagement;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")  // ← LIGNE TRÈS IMPORTANTE
class StudentManagementApplicationTests {

    @Test
    void contextLoads() {
        // Ce test vérifie simplement que le contexte Spring se charge correctement
    }

}