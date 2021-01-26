package org.gentar.report;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class ReportTypeServiceImpl implements ReportTypeService {

    private final ReportTypeRepository reportTypeRepository;

    public ReportTypeServiceImpl(ReportTypeRepository reportTypeRepository)
    {
        this.reportTypeRepository = reportTypeRepository;
    }

    public void createAllReportTypes() {
        ReportTypeName
                .stream()
                .filter(rt -> !reportTypeExistsInDatabase(rt.getLabel()))
                .forEach(rt -> saveDataBaseReportType(rt));
    }

    public void createSpecificReportType(HttpServletResponse response, String name) throws IOException {
        if (reportTypeNameExists(name) && !reportTypeExistsInDatabase(name)) {
            saveDataBaseReportType(ReportTypeName.valueOfLabel(name));
        } else {
            response.setStatus(HttpStatus.FORBIDDEN.value());
        }
    }

    public void updateAllReportTypeDescriptions() {
        ReportTypeName
                .stream()
                .filter(rt -> reportTypeExistsInDatabase(rt.getLabel()))
                .forEach(rt -> updateDataBaseReportType(rt));
    }

    public void updateReportTypeDescription(HttpServletResponse response, String name) {
        if (reportTypeNameExists(name) && reportTypeExistsInDatabase(name)) {
            updateDataBaseReportType(ReportTypeName.valueOfLabel(name));
        } else {
            response.setStatus(HttpStatus.FORBIDDEN.value());
        }
   }

    @Transactional
    public void saveDataBaseReportType(ReportTypeName reportTypeName)
    {
        ReportType reportType = new ReportType();
        reportType.setName(reportTypeName.getLabel());
        reportType.setDescription(reportTypeName.getDescription());
        reportTypeRepository.save(reportType);
    }

    @Transactional
    public void updateDataBaseReportType(ReportTypeName reportTypeName)
    {
        ReportType reportType = reportTypeRepository.findReportTypeByNameIs(reportTypeName.getLabel());
        if (!(reportType.getDescription().equals(reportTypeName.getDescription()))){
            reportType.setDescription(reportTypeName.getDescription());
            reportTypeRepository.save(reportType);
        }
    }

    public Boolean reportTypeNameExists(String reportName) {
        if (ReportTypeName.valueOfLabel(reportName) == null){
            return Boolean.FALSE;
        } else {
            return Boolean.TRUE;
        }
    }

    public Boolean reportTypeExistsInDatabase(String reportName) {
        ReportType reportType = reportTypeRepository.findReportTypeByNameIs(reportName.toLowerCase());
        if (reportType == null){
            return Boolean.FALSE;
        } else {
            return Boolean.TRUE;
        }
    }
}
