package org.gentar.biology.intention;

import org.gentar.Mapper;
import org.gentar.biology.intention.project_intention.ProjectIntention;
import org.gentar.biology.mutation.MolecularMutationTypeMapper;
import org.gentar.biology.mutation.MutationCategorizationMapper;
import org.gentar.biology.mutation.categorizarion.MutationCategorization;
import org.springframework.stereotype.Component;
import java.util.HashSet;
import java.util.Set;

@Component
public class ProjectIntentionCommonMapper  implements Mapper<ProjectIntention, ProjectIntentionCommonDTO>
{
    private final MutationCategorizationMapper mutationCategorizationMapper;
    private final MolecularMutationTypeMapper molecularMutationTypeMapper;

    public ProjectIntentionCommonMapper(
        MutationCategorizationMapper mutationCategorizationMapper,
        MolecularMutationTypeMapper molecularMutationTypeMapper)
    {
        this.mutationCategorizationMapper = mutationCategorizationMapper;
        this.molecularMutationTypeMapper = molecularMutationTypeMapper;
    }

    @Override
    public ProjectIntentionCommonDTO toDto(ProjectIntention projectIntention)
    {
        ProjectIntentionCommonDTO projectIntentionCommonDTO = new ProjectIntentionCommonDTO();
        if (projectIntention.getMolecularMutationType() != null)
        {
            projectIntentionCommonDTO.setMolecularMutationTypeName(
                projectIntention.getMolecularMutationType().getName());
        }
        projectIntentionCommonDTO.setMutationCategorizationDTOS(
            mutationCategorizationMapper.toDtos(projectIntention.getMutationCategorizations()));
        return projectIntentionCommonDTO;
    }

    @Override
    public ProjectIntention toEntity(ProjectIntentionCommonDTO projectIntentionDTO)
    {
        ProjectIntention projectIntention = new ProjectIntention();

        projectIntention.setMolecularMutationType(
            molecularMutationTypeMapper.toEntity(projectIntentionDTO.getMolecularMutationTypeName()));

        Set<MutationCategorization> mutationCategorizations =
            new HashSet<>(mutationCategorizationMapper.toEntities(
                projectIntentionDTO.getMutationCategorizationDTOS()));

        projectIntention.setMutationCategorizations(mutationCategorizations);
        return projectIntention;
    }
}
