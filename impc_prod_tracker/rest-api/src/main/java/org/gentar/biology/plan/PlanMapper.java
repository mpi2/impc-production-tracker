package org.gentar.biology.plan;

import org.gentar.EntityMapper;
import org.gentar.biology.plan.attempt.AttemptTypeMapper;
import org.gentar.biology.plan.attempt.crispr.CrisprAttempt;
import org.gentar.biology.plan.production.crispr_attempt.CrisprAttemptDTO;
import org.gentar.Mapper;
import org.gentar.biology.project.ProjectService;
import org.gentar.biology.statusStamp.StatusMapper;
import org.gentar.organization.funder.FunderMapper;
import org.gentar.organization.work_unit.WorkUnitMapper;
import org.springframework.stereotype.Component;
import org.gentar.biology.plan.attempt.AttemptType;
import org.gentar.biology.plan.attempt.crispr_attempt.CrisprAttemptMapper;
import java.util.ArrayList;
import java.util.List;

@Component
public class PlanMapper implements Mapper<Plan, PlanDTO>
{
    private EntityMapper entityMapper;
    private CrisprAttemptMapper crisprAttemptMapper;
    private AttemptTypeMapper attemptTypeMapper;
    private FunderMapper funderMapper;
    private WorkUnitMapper workUnitMapper;
    private StatusMapper statusMapper;
    private PlanTypeMapper planTypeMapper;
    private ProjectService projectService;

    private static final String CRISPR_ATTEMPT_TYPE = "crispr";

    public PlanMapper(
        EntityMapper entityMapper,
        CrisprAttemptMapper crisprAttemptMapper,
        AttemptTypeMapper attemptTypeMapper,
        FunderMapper funderMapper,
        WorkUnitMapper workUnitMapper,
        StatusMapper statusMapper,
        PlanTypeMapper planTypeMapper, ProjectService projectService)
    {
        this.entityMapper = entityMapper;
        this.crisprAttemptMapper = crisprAttemptMapper;
        this.attemptTypeMapper = attemptTypeMapper;
        this.funderMapper = funderMapper;
        this.workUnitMapper = workUnitMapper;
        this.statusMapper = statusMapper;
        this.planTypeMapper = planTypeMapper;
        this.projectService = projectService;
    }

    public PlanDTO toDto(Plan plan)
    {
        PlanDTO planDTO = entityMapper.toTarget(plan, PlanDTO.class);
        addNoMappedData(planDTO, plan);

        return planDTO;
    }

    private void addNoMappedData(PlanDTO planDTO, Plan plan)
    {
        addAttempt(planDTO, plan);
        planDTO.setTpn(plan.getProject().getTpn());
    }

    public List<PlanDTO> toDtos(List<Plan> plans)
    {
        List<PlanDTO> planDTOS = new ArrayList<>();
        if (plans != null)
        {
            plans.forEach(plan -> planDTOS.add(toDto(plan)));
        }
        return planDTOS;
    }

    private void addAttempt(PlanDTO planDTO, Plan plan)
    {
        AttemptType attemptType = plan.getAttemptType();
        String attemptTypeName = attemptType == null ? "Not defined" : attemptType.getName();

        if (CRISPR_ATTEMPT_TYPE.equalsIgnoreCase(attemptTypeName))
        {
            CrisprAttemptDTO crisprAttemptDTO =
                crisprAttemptMapper.toDto(plan.getCrisprAttempt());
            planDTO.setCrisprAttemptDTO(crisprAttemptDTO);
        } else
        {
            //TODO: other attempts
        }

    }

    @Override
    public Plan toEntity(PlanDTO planDTO)
    {
        Plan plan = entityMapper.toTarget(planDTO, Plan.class);
        plan.setProject(projectService.getProjectByTpn(planDTO.getTpn()));
        if (planDTO.getCrisprAttemptDTO() != null)
        {
            CrisprAttempt crisprAttempt = crisprAttemptMapper.toEntity(planDTO.getCrisprAttemptDTO());
            crisprAttempt.setImitsMiAttemptId(plan.getCrisprAttempt().getImitsMiAttemptId());
            crisprAttempt.setPlan(plan);
            crisprAttempt.setId(plan.getId());
            plan.setCrisprAttempt(crisprAttempt);
        }
        plan.setPlanType(planTypeMapper.toEntity(planDTO.getPlanTypeName()));
        plan.setAttemptType(attemptTypeMapper.toEntity(planDTO.getAttemptTypeName()));
        plan.setFunder(funderMapper.toEntity(planDTO.getFunderName()));
        plan.setWorkUnit(workUnitMapper.toEntity(planDTO.getWorkUnitName()));
        plan.setStatus(statusMapper.toEntity(planDTO.getStatusName()));
        return plan;
    }
}
