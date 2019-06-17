package uk.ac.ebi.impc_prod_tracker.service.conf;

import java.util.List;
import java.util.Map;

public interface ConfigurationService
{
    Map<String, List<String>> getConfiguration();
}
