package org.gentar.report.export;

import java.util.List;
import org.gentar.report.ReportServiceImpl;
import org.gentar.report.dto.crispr_product.CrisprProductDTO;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/api/reports")
public class ReportExportController {

    private final ReportServiceImpl reportService;

    public ReportExportController(ReportServiceImpl reportService) {
        this.reportService = reportService;
    }

    @GetMapping(value = {"/{reportName}"})
    @Transactional(readOnly = true)
    public void export(HttpServletResponse response, @PathVariable String reportName)
        throws IOException {
        reportService.writeReport(response, reportName);
    }

    @GetMapping(value = {"/json/{reportName}"})
    public ResponseEntity<?> exportAsJson(@PathVariable String reportName) {
        try {
            List<CrisprProductDTO> response = reportService.getReportsAsDtoList(reportName);

            if (response != null) {
                return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(response);
            } else {
                // Report not found
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Report not found");
            }
        } catch (Exception e) {
            // Handle exceptions gracefully
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("An error occurred while processing the request.");
        }
    }


    public static JSONArray convertStringToJsonArray(String input) {
        // Initialize an empty JSON array to store the results
        JSONArray jsonArray = new JSONArray();

        // Split the input string by line breaks to separate individual JSON objects
        String[] jsonObjects = input.split("\n");

        try {
            // Iterate through each JSON object string and add it to the JSON array
            for (String jsonObjectString : jsonObjects) {
                // Parse each JSON object string into a JSONObject
                JSONObject jsonObject = new JSONObject(jsonObjectString.trim());
                jsonArray.put(jsonObject);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonArray;
    }
}
