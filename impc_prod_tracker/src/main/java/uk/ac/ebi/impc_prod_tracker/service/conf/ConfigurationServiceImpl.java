package uk.ac.ebi.impc_prod_tracker.service.conf;

import org.springframework.stereotype.Component;
import uk.ac.ebi.impc_prod_tracker.conf.security.SystemSubject;
import uk.ac.ebi.impc_prod_tracker.conf.security.abac.spring.SubjectRetriever;
import uk.ac.ebi.impc_prod_tracker.data.biology.allele_type.AlleleTypeRepository;
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
        RoleRepository roleRepository)
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
    }

    @Override
    public Map<String, List<String>> getConfiguration()
    {
        Map<String, List<String>> conf = new HashMap<>();
        SystemSubject systemSubject = subjectRetriever.getSubject();
        if (systemSubject != null)
        {
            List<String> workUnits = new ArrayList<>();
            workUnitRepository.findAll().forEach(p -> workUnits.add(p.getName()));

            List<String> workGroups = new ArrayList<>();
            workGroupRepository.findAll().forEach(p -> workGroups.add(p.getName()));

            List<String> planTypes = new ArrayList<>();
            planTypeRepository.findAll().forEach(p -> planTypes.add(p.getName()));

            List<String> privacies = new ArrayList<>();
            privacyRepository.findAll().forEach(p -> privacies.add(p.getName()));

            List<String> priorities = new ArrayList<>();
            projectPriorityRepository.findAll().forEach(p -> priorities.add(p.getName()));

            List<String> statuses = new ArrayList<>();
            statusRepository.findAll().forEach(p -> statuses.add(p.getName()));

            List<String> alleleTypes = new ArrayList<>();
            alleleTypeRepository.findAll().forEach(p -> alleleTypes.add(p.getName()));

            List<String> institutes = new ArrayList<>();
            instituteRepository.findAll().forEach(p -> institutes.add(p.getName()));

            List<String> roles = new ArrayList<>();
            roleRepository.findAll().forEach(p -> roles.add(p.getName()));

            conf.put("workUnits", workUnits);
            conf.put("workGroups", workGroups);
            conf.put("planTypes", planTypes);
            conf.put("privacies", privacies);
            conf.put("priorities", priorities);
            conf.put("statuses", statuses);
            conf.put("alleleTypes", alleleTypes);
            conf.put("institutes", institutes);
            conf.put("roles", roles);
        }

        return conf;
    }
}
