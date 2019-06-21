package uk.ac.ebi.impc_prod_tracker.service.conf;

import java.util.List;
import java.util.Map;

public interface PermissionService
{
    Map<String, Boolean> getPermissions();
}
