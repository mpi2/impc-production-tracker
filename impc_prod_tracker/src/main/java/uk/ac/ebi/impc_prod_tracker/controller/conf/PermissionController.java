package uk.ac.ebi.impc_prod_tracker.controller.conf;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.ac.ebi.impc_prod_tracker.service.conf.PermissionService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class PermissionController
{
    private PermissionService permissionService;

    public PermissionController(PermissionService permissionService)
    {

        this.permissionService = permissionService;
    }

    @GetMapping(value = {"/permissions"})
    public Map<String, Boolean> getConfiguration()
    {
        return permissionService.getPermissions();
    }
}
