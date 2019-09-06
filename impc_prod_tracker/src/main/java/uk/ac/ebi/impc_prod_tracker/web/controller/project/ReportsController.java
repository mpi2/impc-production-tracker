package uk.ac.ebi.impc_prod_tracker.web.controller.project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import uk.ac.ebi.impc_prod_tracker.reports.ProjectsReporter;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@RestController
@RequestMapping("/download")
@CrossOrigin(origins = "*")
public class ReportsController {

    @Autowired
    ProjectsReporter reporter;

    @GetMapping(value = {"/projectSummaries"})
    @ResponseBody
    public void getProjectSummariesPaginated(HttpServletResponse response)
//                                                        @RequestParam(value = "markerSymbol", required = false) List<String> markerSymbols,
//                                                @RequestParam(value = "workUnit", required = false) List<String> workUnits,
//                                                @RequestParam(value = "workGroup", required = false) List<String> workGroups,
//                                                @RequestParam(value = "planType", required = false) List<String> planTypes,
//                                                @RequestParam(value = "status", required = false) List<String> statuses,
//                                                @RequestParam(value = "privacy", required = false) List<String> privacies)
    {
        System.out.println("calling reports controller here");
        String csvFileName = "csvFilename";
        response.setContentType("text/csv");
        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"",
                csvFileName);
        response.setHeader(headerKey, headerValue);
//        try {
//            response.getWriter().print("a,b,c\n1,2,3\n3,4,5");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        try {
            reporter.printReport(response.getWriter());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
