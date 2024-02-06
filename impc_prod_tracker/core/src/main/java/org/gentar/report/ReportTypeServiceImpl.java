package org.gentar.report;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
                .forEach(this::saveDataBaseReportType);
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
                .forEach(this::updateDataBaseReportTypeDescription);
    }

    public void updateReportTypeDescription(HttpServletResponse response, String name) {
        if (reportTypeNameExists(name) && reportTypeExistsInDatabase(name)) {
            updateDataBaseReportTypeDescription(ReportTypeName.valueOfLabel(name));
        } else {
            response.setStatus(HttpStatus.FORBIDDEN.value());
        }
    }

    public void updateAllReportTypePublicSettings() {
        ReportTypeName
                .stream()
                .filter(rt -> reportTypeExistsInDatabase(rt.getLabel()))
                .forEach(this::updateDataBaseReportTypePublicSetting);
    }

    public List<ReportType> listPublicReportTypes() {

        List<ReportType> publicReportTypes = reportTypeRepository.findAllByIsPublicIsTrue();
        return new ArrayList<>(publicReportTypes);
    }

    @Transactional
    public void saveDataBaseReportType(ReportTypeName reportTypeName)
    {
        ReportType reportType = new ReportType();
        reportType.setName(reportTypeName.getLabel());
        reportType.setDescription(reportTypeName.getDescription());
        reportType.setIsPublic(reportTypeName.getIsPublic());
        reportTypeRepository.save(reportType);
    }

    @Transactional
    public void updateDataBaseReportTypeDescription(ReportTypeName reportTypeName)
    {
        ReportType reportType = reportTypeRepository.findReportTypeByNameIs(reportTypeName.getLabel());
        if (!(reportType.getDescription().equals(reportTypeName.getDescription()))){
            reportType.setDescription(reportTypeName.getDescription());
            reportTypeRepository.save(reportType);
        }
    }

    @Transactional
    public void updateDataBaseReportTypePublicSetting(ReportTypeName reportTypeName) {
        ReportType reportType = reportTypeRepository.findReportTypeByNameIs(reportTypeName.getLabel());
        if (!(reportType.getIsPublic().equals(reportTypeName.getIsPublic()))){
            reportType.setIsPublic(reportTypeName.getIsPublic());
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
