package org.gentar.biology.glt_production_numbers;

import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.gentar.report.collection.glt_attempts.GltAttemptsServiceImpl;
import org.gentar.report.collection.glt_attempts.projection.GltAttemptsProjection;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class GltAttemptsJsonController {

    private final GltAttemptsServiceImpl gltAttemptsService;

    public GltAttemptsJsonController(GltAttemptsServiceImpl gltAttemptsService) {
        this.gltAttemptsService = gltAttemptsService;
    }


    //http://localhost:8080/api/glt_production_numbers?attempt=crispr&workunit=IMPC&startingyear=2016&endingyear=2023
    @GetMapping("/glt_production_numbers")
    @Transactional(readOnly = true)
    public ResponseEntity<List<GltAttemptProjectionDto>> exportGltAttemptsWithMonth(HttpServletResponse response,
                                           @RequestParam(value = "reporttype")
                                               String reportType,
                                           @RequestParam(value = "attempt")
                                               String attempt,
                                           @RequestParam(value = "workunit", required = false)
                                               String workUnit,
                                           @RequestParam(value = "workGroup", required = false)
                                               String workGroup,
                                           @RequestParam(value = "startyear", required = false)
                                               String startYear,
                                           @RequestParam(value = "endyear", required = false)
                                               String endYear,
                                           @RequestParam(value = "startmonth", required = false)
                                               String starMonth,
                                           @RequestParam(value = "endmonth", required = false)
                                               String endMonth) {
        if (attempt.equals("escell")) {
            attempt = "es cell";
        }

        try {
            List<String> workUnits= Arrays.stream(workUnit.split(","))
                .map(String::trim) // Remove spaces
                .map(String::toUpperCase) // Convert to uppercase without specifying Locale
                .collect(Collectors.toList());

            List<GltAttemptsProjection> gltAttemptsProjections = gltAttemptsService
                .generateGltAttemptsJson(reportType, attempt, workUnits, workGroup,
                    startYear, endYear, starMonth, endMonth);


            List<GltAttemptProjectionDto>  gltAttemptProjectionsDto =
                gltAttemptsProjections.stream().map(this::mapToDto).collect(Collectors.toList());

            if(workUnits.size()>1){
                gltAttemptProjectionsDto
                    .forEach(dto -> dto.setWorkUnitName(workUnit));
            }

            return ResponseEntity.ok(gltAttemptProjectionsDto);
        } catch (Exception e) {
            // Log the exception for debugging purposes
            e.printStackTrace();
            // Return an error response
            return ResponseEntity.status(500).body(null);
        }

    }

    //http://localhost:8080/api/glt_production_numbers/overlap/intersection
    @GetMapping("/glt_production_numbers/overlap/intersection")
    @Transactional(readOnly = true)
    public ResponseEntity<List<String>> exportGltAttemptsIntersection() {

        try {
            List<GltAttemptsProjection> gltAttemptsProjections = gltAttemptsService
                .getGltAttemptsIntersectionJson();

            List<String> symbols =
                gltAttemptsProjections.stream().map(GltAttemptsProjection::getSymbol).collect(Collectors.toList());

            return ResponseEntity.ok(symbols);
        } catch (Exception e) {
            // Log the exception for debugging purposes
            e.printStackTrace();
            // Return an error response
            return ResponseEntity.status(500).body(null);
        }
    }

    //http://localhost:8080/api/glt_production_numbers/overlap/union
    @GetMapping("/glt_production_numbers/overlap/union")
    @Transactional(readOnly = true)
    public ResponseEntity<List<Long>> exportGltAttemptsUnion() {
        try {
            List<GltAttemptsProjection> gltAttemptsProjections = gltAttemptsService
                .getGltAttemptsUnionJson();

            List<Long> count =
                gltAttemptsProjections.stream().map(GltAttemptsProjection::getCount).collect(Collectors.toList());

            return ResponseEntity.ok(count);
        } catch (Exception e) {
            // Log the exception for debugging purposes
            e.printStackTrace();
            // Return an error response
            return ResponseEntity.status(500).body(null);
        }
    }

    private GltAttemptProjectionDto mapToDto(GltAttemptsProjection gltAttemptsProjection) {
        GltAttemptProjectionDto gltAttemptProjectionDto = new GltAttemptProjectionDto();

        try {
            gltAttemptProjectionDto.setYear(gltAttemptsProjection.getYear());
        } catch (Exception e) {
            // Handle the exception (e.g., log an error) and continue with the next line
        }

        try {
            gltAttemptProjectionDto.setMonth(gltAttemptsProjection.getMonth());
        } catch (Exception e) {
            // Handle the exception (e.g., log an error) and continue with the next line
        }

        try {
            gltAttemptProjectionDto.setWorkUnitName(gltAttemptsProjection.getWorkUnitName());
        } catch (Exception e) {
            // Handle the exception (e.g., log an error) and continue with the next line
        }

        try {
            gltAttemptProjectionDto.setWorkGroupName(gltAttemptsProjection.getWorkGroupName());
        } catch (Exception e) {
            // Handle the exception (e.g., log an error) and continue with the next line
        }

        try {
            gltAttemptProjectionDto.setSum(gltAttemptsProjection.getSum());
        } catch (Exception e) {
            // Handle the exception (e.g., log an error) and continue with the next line
        }

        try {
            gltAttemptProjectionDto.setSymbol(gltAttemptsProjection.getSymbol());
        } catch (Exception e) {
            // Handle the exception (e.g., log an error) and continue with the next line
        }

        try {
            gltAttemptProjectionDto.setCount(gltAttemptsProjection.getCount());
        } catch (Exception e) {
            // Handle the exception (e.g., log an error) and continue with the next line
        }

        return gltAttemptProjectionDto;
    }


}