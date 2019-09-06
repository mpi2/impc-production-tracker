package uk.ac.ebi.impc_prod_tracker.web.controller.project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import uk.ac.ebi.impc_prod_tracker.reports.ProjectsReporter;

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
    public String getProjectSummariesPaginated()
//                                                        @RequestParam(value = "markerSymbol", required = false) List<String> markerSymbols,
//                                                @RequestParam(value = "workUnit", required = false) List<String> workUnits,
//                                                @RequestParam(value = "workGroup", required = false) List<String> workGroups,
//                                                @RequestParam(value = "planType", required = false) List<String> planTypes,
//                                                @RequestParam(value = "status", required = false) List<String> statuses,
//                                                @RequestParam(value = "privacy", required = false) List<String> privacies)
    {
        System.out.println("calling reports controller here");
        try {
            reporter.printReport();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "csv data here";//change to file type later string for debug
    }
}
