package uk.ac.ebi.impc_prod_tracker.service.conf;

import org.springframework.stereotype.Component;
import uk.ac.ebi.impc_prod_tracker.conf.security.SystemSubject;
import uk.ac.ebi.impc_prod_tracker.conf.security.abac.spring.SubjectRetriever;
import uk.ac.ebi.impc_prod_tracker.data.biology.allele_type.AlleleTypeRepository;
import uk.ac.ebi.impc_prod_tracker.data.biology.attempt.phenotyping_attempt.material_deposited_type.MaterialDepositedTypeRepository;
import uk.ac.ebi.impc_prod_tracker.data.biology.plan.type.PlanTypeRepository;
import uk.ac.ebi.impc_prod_tracker.data.biology.privacy.PrivacyRepository;
import uk.ac.ebi.impc_prod_tracker.data.biology.status.StatusRepository;
import uk.ac.ebi.impc_prod_tracker.data.biology.strain.StrainRepository;
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
    private StatusRepository statusRepository;
    private AlleleTypeRepository alleleTypeRepository;
    private InstituteRepository instituteRepository;
    private RoleRepository roleRepository;
    private StrainRepository strainRepository;

    private MaterialDepositedTypeRepository materialDepositedTypeRepository;

    private Map<String, List<String>> conf = new HashMap<>();

    public ConfigurationServiceImpl(
            SubjectRetriever subjectRetriever,
            WorkUnitRepository workUnitRepository,
            WorkGroupRepository workGroupRepository,
            PlanTypeRepository planTypeRepository,
            PrivacyRepository privacyRepository,
            StatusRepository statusRepository,
            AlleleTypeRepository alleleTypeRepository,
            InstituteRepository instituteRepository,
            RoleRepository roleRepository,
            StrainRepository strainRepository,
            MaterialDepositedTypeRepository materialDepositedTypeRepository
    )
    {
        this.subjectRetriever = subjectRetriever;
        this.workUnitRepository = workUnitRepository;
        this.workGroupRepository = workGroupRepository;
        this.planTypeRepository = planTypeRepository;
        this.privacyRepository = privacyRepository;
        this.statusRepository = statusRepository;
        this.alleleTypeRepository = alleleTypeRepository;
        this.instituteRepository = instituteRepository;
        this.roleRepository = roleRepository;
        this.strainRepository = strainRepository;
        this.materialDepositedTypeRepository = materialDepositedTypeRepository;
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
                addStatuses();
                addAlleleTypes();
                addInstitutes();
                addRoles();
                addStrains();
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

    private void addStrains()
    {
        List<String> trackedStrains = new ArrayList<>();
        strainRepository.findAll().forEach(p -> trackedStrains.add(p.getName()));
        conf.put("trackedStrains", trackedStrains);
    }

    private void addMaterialTypes()
    {
        List<String> materialTypes = new ArrayList<>();
        materialDepositedTypeRepository.findAll().forEach(p -> materialTypes.add(p.getName()));
        conf.put("materialTypes", materialTypes);
    }
}
