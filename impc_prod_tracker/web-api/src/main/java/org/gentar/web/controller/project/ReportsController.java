package org.gentar.web.controller.project;

import org.gentar.reports.ProjectsReporter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@RestController
@RequestMapping("/reports")
@CrossOrigin(origins = "*")
public class ReportsController {

    @Autowired
    ProjectsReporter reporter;

    @GetMapping(value = {"/{filename}"})
    @ResponseBody
    public void getProjectSummariesPaginated(HttpServletResponse response,
                                             @PathVariable("filename") String filename)
    {
        System.out.println("calling reports controller here");
        response.setContentType("text/csv");
        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"",
                filename);
        response.setHeader(headerKey, headerValue);
        try {
            reporter.printReport(response.getWriter());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
