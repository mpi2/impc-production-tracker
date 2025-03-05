package org.gentar.biology.mutation;

import org.gentar.Mapper;
import org.gentar.biology.mutation.genetic_type.GeneticMutationType;
import org.gentar.biology.mutation.molecular_type.MolecularMutationType;
import org.gentar.biology.mutation.mutation_deletion.MolecularMutationDeletion;
import org.gentar.biology.mutation.qc_results.MutationQcResult;
import org.gentar.biology.mutation.sequence.MutationSequence;
import org.gentar.biology.plan.attempt.crispr.TargetedExonDTO;
import org.gentar.biology.plan.attempt.crispr.targeted_exon.TargetedExon;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class MutationCommonMapper implements Mapper<Mutation, MutationCommonDTO>
{
    private final MutationQCResultMapper mutationQCResultMapper;
    private final MutationCategorizationMapper mutationCategorizationMapper;
    private final MutationSequenceMapper mutationSequenceMapper;
    private final GeneticMutationTypeMapper geneticMutationTypeMapper;
    private final MolecularMutationTypeMapper molecularMutationTypeMapper;

    private final MolecularMutationDeletionMapper molecularMutationDeletionMapper;

    private final TargetedExonMapper targetedExonMapper;

    public MutationCommonMapper(
            MutationQCResultMapper mutationQCResultMapper,
            MutationCategorizationMapper mutationCategorizationMapper,
            MutationSequenceMapper mutationSequenceMapper,
            GeneticMutationTypeMapper geneticMutationTypeMapper,
            MolecularMutationTypeMapper molecularMutationTypeMapper, MolecularMutationDeletionMapper molecularMutationDeletionMapper, TargetedExonMapper targetedExonMapper)
    {
        this.mutationQCResultMapper = mutationQCResultMapper;
        this.mutationCategorizationMapper = mutationCategorizationMapper;
        this.mutationSequenceMapper = mutationSequenceMapper;
        this.geneticMutationTypeMapper = geneticMutationTypeMapper;
        this.molecularMutationTypeMapper = molecularMutationTypeMapper;
        this.molecularMutationDeletionMapper = molecularMutationDeletionMapper;
        this.targetedExonMapper = targetedExonMapper;
    }

    @Override
    public MutationCommonDTO toDto(Mutation mutation)
    {
        MutationCommonDTO mutationCommonDTO = new MutationCommonDTO();
        mutationCommonDTO.setSymbol(mutation.getSymbol());
        mutationCommonDTO.setDescription(mutation.getDescription());
        mutationCommonDTO.setQcNote(mutation.getQcNote());
        mutationCommonDTO.setMgiAlleleSymbolRequiresConstruction(mutation.getMgiAlleleSymbolRequiresConstruction());
        if (mutation.getGeneticMutationType() != null) {
            mutationCommonDTO.setGeneticMutationTypeName(mutation.getGeneticMutationType().getName());
        }
        if (mutation.getMolecularMutationType() != null) {
            mutationCommonDTO.setMolecularMutationTypeName(mutation.getMolecularMutationType().getName());
        }
        mutationCommonDTO.setAlleleConfirmed(mutation.getAlleleConfirmed());
        mutationCommonDTO.setMutationQCResultDTOs(
            mutationQCResultMapper.toDtos(mutation.getMutationQcResults()));
        mutationCommonDTO.setMutationSequenceDTOS(
            mutationSequenceMapper.toDtos(mutation.getMutationSequences()));
        mutationCommonDTO.setMutationCategorizationDTOS(
            mutationCategorizationMapper.toDtos(mutation.getMutationCategorizations()));
        mutationCommonDTO.setMolecularMutationDeletionDTOs(molecularMutationDeletionMapper.toDtos(mutation.getMolecularMutationDeletion()));
        mutationCommonDTO.setTargetedExonDTOS(targetedExonMapper.toDtos(mutation.getTargetedExons()));
        mutationCommonDTO.setIsManualMutationDeletion(mutation.getIsManualMutationDeletion());
        mutationCommonDTO.setIsMutationDeletionChecked(mutation.getIsMutationDeletionChecked());

        return mutationCommonDTO;
    }

    @Override
    public Mutation toEntity(MutationCommonDTO mutationCommonDTO)
    {
        Mutation mutation = new Mutation();
        if (mutationCommonDTO != null)
        {
            mutation.setSymbol(mutationCommonDTO.getSymbol());
            mutation.setAlleleConfirmed(mutationCommonDTO.getAlleleConfirmed());
            mutation.setMgiAlleleSymbolRequiresConstruction(
                mutationCommonDTO.getMgiAlleleSymbolRequiresConstruction());
            mutation.setDescription(mutationCommonDTO.getDescription());
            mutation.setQcNote(mutationCommonDTO.getQcNote());
            setGeneticMutationType(mutation, mutationCommonDTO);
            setMolecularMutationType(mutation, mutationCommonDTO);
            setMutationQcResults(mutation, mutationCommonDTO);
            setMutationSequences(mutation, mutationCommonDTO);
            mutation.setMutationCategorizations(
                new HashSet<>(mutationCategorizationMapper.toEntities(
                    mutationCommonDTO.getMutationCategorizationDTOS())));

            setMolecularMutationDeletion(mutation, mutationCommonDTO);
            setTargetedExons(mutation, mutationCommonDTO);
            mutation.setIsManualMutationDeletion(mutationCommonDTO.getIsManualMutationDeletion());
            mutation.setIsMutationDeletionChecked(mutationCommonDTO.getIsMutationDeletionChecked());

        }

        return mutation;
    }

    private void setMutationQcResults(Mutation mutation, MutationCommonDTO mutationCommonDTO)
    {
        List<MutationQCResultDTO> mutationQCResultDTOS = mutationCommonDTO.getMutationQCResultDTOs();
        if (mutationQCResultDTOS != null)
        {
            Set<MutationQcResult> mutationQcResults =
                new HashSet<>(mutationQCResultMapper.toEntities(mutationQCResultDTOS));
            mutationQcResults.forEach(x -> x.setMutation(mutation));
            mutation.setMutationQcResults(mutationQcResults);
        }
    }

    private void setGeneticMutationType(Mutation mutation, MutationCommonDTO mutationCommonDTO)
    {
        String geneticMutationTypeName =  mutationCommonDTO.getGeneticMutationTypeName();
        if (geneticMutationTypeName != null)
        {
            GeneticMutationType geneticMutationType =
                geneticMutationTypeMapper.toEntity(geneticMutationTypeName);
            mutation.setGeneticMutationType(geneticMutationType);
        }
    }

    private void setMolecularMutationType(Mutation mutation, MutationCommonDTO mutationCommonDTO)
    {
        String molecularMutationTypeName =  mutationCommonDTO.getMolecularMutationTypeName();
        if (molecularMutationTypeName != null)
        {
            MolecularMutationType molecularMutationType =
                molecularMutationTypeMapper.toEntity(molecularMutationTypeName);
            mutation.setMolecularMutationType(molecularMutationType);
        }
    }

    private void setMutationSequences(Mutation mutation, MutationCommonDTO mutationCommonDTO)
    {
        List<MutationSequenceDTO> mutationSequenceDTOS = mutationCommonDTO.getMutationSequenceDTOS();
        if (mutationSequenceDTOS != null)
        {
            Set<MutationSequence> mutationSequences =
                new HashSet<>(
                    mutationSequenceMapper.toEntities(mutationCommonDTO.getMutationSequenceDTOS()));
            mutationSequences.forEach(x -> x.setMutation(mutation));
            mutation.setMutationSequences(mutationSequences);
        }
    }

    private void setMolecularMutationDeletion(Mutation mutation, MutationCommonDTO mutationCommonDTO)
    {
        List<MolecularMutationDeletionDTO> molecularMutationDeletionDTOS = mutationCommonDTO.getMolecularMutationDeletionDTOs();
        if (molecularMutationDeletionDTOS != null)
        {
            Set<MolecularMutationDeletion> molecularMutationDeletions =
                    new HashSet<>(
                            molecularMutationDeletionMapper.toEntities(mutationCommonDTO.getMolecularMutationDeletionDTOs()));
            molecularMutationDeletions.forEach(x -> x.setMutation(mutation));
            mutation.setMolecularMutationDeletion(molecularMutationDeletions);
        }
    }

    private void setTargetedExons(Mutation mutation, MutationCommonDTO mutationCommonDTO)
    {
        List<TargetedExonDTO> targetedExonDTOS = mutationCommonDTO.getTargetedExonDTOS();
        if (targetedExonDTOS != null)
        {
            Set<TargetedExon> targetedExons =
                    new HashSet<>(
                            targetedExonMapper.toEntities(mutationCommonDTO.getTargetedExonDTOS()));
            targetedExons.forEach(x -> x.setMutation(mutation));
            mutation.setTargetedExons(targetedExons);
        }
    }
}
