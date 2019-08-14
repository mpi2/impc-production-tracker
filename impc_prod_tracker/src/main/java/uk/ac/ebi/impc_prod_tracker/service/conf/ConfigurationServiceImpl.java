package uk.ac.ebi.impc_prod_tracker.service.conf;

import org.springframework.stereotype.Component;
import uk.ac.ebi.impc_prod_tracker.conf.security.SystemSubject;
import uk.ac.ebi.impc_prod_tracker.conf.security.abac.spring.SubjectRetriever;
import uk.ac.ebi.impc_prod_tracker.data.biology.allele_type.AlleleTypeRepository;
import uk.ac.ebi.impc_prod_tracker.data.biology.material_type.MaterialTypeRepository;
import uk.ac.ebi.impc_prod_tracker.data.biology.tracked_strain.TrackedStrainRepository;
import uk.ac.ebi.impc_prod_tracker.data.experiment.plan.type.PlanTypeRepository;
import uk.ac.ebi.impc_prod_tracker.data.experiment.privacy.PrivacyRepository;
import uk.ac.ebi.impc_prod_tracker.data.experiment.project_priority.ProjectPriorityRepository;
import uk.ac.ebi.impc_prod_tracker.data.experiment.status.StatusRepository;
import uk.ac.ebi.impc_prod_tracker.data.organization.institute.InstituteRepository;
import uk.ac.ebi.impc_prod_tracker.data.organization.role.RoleRepository;
import uk.ac.ebi.impc_prod_tracker.data.organization.work_group.WorkGroupRepository;
import uk.ac.ebi.impc_prod_tracker.data.organization.work_unit.WorkUnitRepository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ConfigurationServiceImpl implements ConfigurationService
{
    private SubjectRetriever subjectRetriever;
    private WorkUnitRepository workUnitRepository;
    private WorkGroupRepository workGroupRepository;
    private PlanTypeRepository planTypeRepository;
    private PrivacyRepository privacyRepository;
    private ProjectPriorityRepository projectPriorityRepository;
    private StatusRepository statusRepository;
    private AlleleTypeRepository alleleTypeRepository;
    private InstituteRepository instituteRepository;
    private RoleRepository roleRepository;
    private TrackedStrainRepository trackedStrainRepository;
    private MaterialTypeRepository materialTypeRepository;

    private Map<String, List<String>> conf = new HashMap<>();

    public ConfigurationServiceImpl(
        SubjectRetriever subjectRetriever,
        WorkUnitRepository workUnitRepository,
        WorkGroupRepository workGroupRepository,
        PlanTypeRepository planTypeRepository,
        PrivacyRepository privacyRepository,
        ProjectPriorityRepository projectPriorityRepository,
        StatusRepository statusRepository,
        AlleleTypeRepository alleleTypeRepository,
        InstituteRepository instituteRepository,
        RoleRepository roleRepository,
        TrackedStrainRepository trackedStrainRepository,
        MaterialTypeRepository materialTypeRepository)
    {

        this.subjectRetriever = subjectRetriever;
        this.workUnitRepository = workUnitRepository;
        this.workGroupRepository = workGroupRepository;
        this.planTypeRepository = planTypeRepository;
        this.privacyRepository = privacyRepository;
        this.projectPriorityRepository = projectPriorityRepository;
        this.statusRepository = statusRepository;
        this.alleleTypeRepository = alleleTypeRepository;
        this.instituteRepository = instituteRepository;
        this.roleRepository = roleRepository;
        this.trackedStrainRepository = trackedStrainRepository;
        this.materialTypeRepository = materialTypeRepository;
    }

    @Override
    public Map<String, List<String>> getConfiguration()
    {
        if (conf.isEmpty())
        {
            SystemSubject systemSubject = subjectRetriever.getSubject();
            if (systemSubject != null)
            {
                addWorkUnits();
                addWorkGroups();
                addPlanTypes();
                addPrivacies();
                addPriorities();
                addStatuses();
                addAlleleTypes();
                addInstitutes();
                addRoles();
                addTrackedStrains();
                addMaterialTypes();
            }
        }

        return conf;
    }

    private void addWorkUnits()
    {
        List<String> workUnits = new ArrayList<>();
        workUnitRepository.findAll().forEach(p -> workUnits.add(p.getName()));
        conf.put("workUnits", workUnits);
    }

    private void addWorkGroups()
    {
        List<String> workGroups = new ArrayList<>();
        workGroupRepository.findAll().forEach(p -> workGroups.add(p.getName()));
        conf.put("workGroups", workGroups);
    }

    private void addPlanTypes()
    {
        List<String> planTypes = new ArrayList<>();
        planTypeRepository.findAll().forEach(p -> planTypes.add(p.getName()));
        conf.put("planTypes", planTypes);
    }

    private void addPrivacies()
    {
        List<String> privacies = new ArrayList<>();
        privacyRepository.findAll().forEach(p -> privacies.add(p.getName()));
        conf.put("privacies", privacies);
    }

    private void addPriorities()
    {
        List<String> priorities = new ArrayList<>();
        projectPriorityRepository.findAll().forEach(p -> priorities.add(p.getName()));
        conf.put("priorities", priorities);
    }

    private void addStatuses()
    {
        List<String> statuses = new ArrayList<>();
        statusRepository.findAll().forEach(p -> statuses.add(p.getName()));
        conf.put("statuses", statuses);
    }

    private void addAlleleTypes()
    {
        List<String> alleleTypes = new ArrayList<>();
        alleleTypeRepository.findAll().forEach(p -> alleleTypes.add(p.getName()));
        conf.put("alleleTypes", alleleTypes);
    }

    private void addInstitutes()
    {
        List<String> institutes = new ArrayList<>();
        instituteRepository.findAll().forEach(p -> institutes.add(p.getName()));
        conf.put("institutes", institutes);
    }

    private void addRoles()
    {
        List<String> roles = new ArrayList<>();
        roleRepository.findAll().forEach(p -> roles.add(p.getName()));
        conf.put("roles", roles);
    }

    private void addTrackedStrains()
    {
        List<String> trackedStrains = new ArrayList<>();
        trackedStrainRepository.findAll().forEach(p -> trackedStrains.add(p.getName()));
        conf.put("trackedStrains", trackedStrains);
    }

    private void addMaterialTypes()
    {
        List<String> materialTypes = new ArrayList<>();
        materialTypeRepository.findAll().forEach(p -> materialTypes.add(p.getName()));
        conf.put("materialTypes", materialTypes);
    }
}
