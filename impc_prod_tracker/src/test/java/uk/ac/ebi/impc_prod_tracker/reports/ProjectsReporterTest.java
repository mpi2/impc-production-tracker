package uk.ac.ebi.impc_prod_tracker.reports;

import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

@SpringBootTest
public class ProjectsReporterTest {
    @Autowired
    ProjectsReporter reporter;

    @BeforeEach
    void setUp() {

    }

    @AfterEach
    void tearDown() {
    }

    @Test
    public void testProjectReport() {
        try {
            File file = new File("testReportsFiile");
            PrintWriter printWriter = null;
            try {
                printWriter = new PrintWriter(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            try {
                reporter.printReport(printWriter);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}