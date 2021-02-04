package org.gentar.report.report_type;

import org.gentar.report.ReportType;
import org.gentar.report.ReportTypeDTO;
import org.gentar.report.ReportTypeServiceImpl;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class PublicReportTypeController {

    private final ReportTypeServiceImpl reportTypeService;
    private final ReportTypeMapper reportTypeMapper;

    public PublicReportTypeController(ReportTypeServiceImpl reportTypeService,
                                      ReportTypeMapper reportTypeMapper) {
        this.reportTypeService = reportTypeService;
        this.reportTypeMapper = reportTypeMapper;
    }

    @GetMapping(value ="/list")
    public ResponseEntity<CollectionModel<ReportTypeDTO>> listPublicReportTypes()
    {
        List<ReportType> publicReportTypes = reportTypeService.listPublicReportTypes();
        List<ReportTypeDTO> publicReportTypeDTOs = reportTypeMapper.toDtos(publicReportTypes);
        return ResponseEntity.ok(CollectionModel.of(publicReportTypeDTOs));
    }
}
