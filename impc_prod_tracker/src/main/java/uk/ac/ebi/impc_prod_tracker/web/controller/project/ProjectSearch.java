package uk.ac.ebi.impc_prod_tracker.web.controller.project;

import lombok.Data;
import java.util.List;

@Data
public class ProjectSearch
{
    private List<String> tpns;
    private List<String> markerSymbols;
    private List<String> intentions;
    private List<String> workUnitNames;
    private List<String> consortiaNames;
    private List<String> statusesNames;
    private List<String> privaciesNames;
}
