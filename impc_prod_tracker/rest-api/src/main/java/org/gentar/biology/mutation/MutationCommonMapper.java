package org.gentar.biology.mutation;

import org.gentar.Mapper;
import org.gentar.biology.insertion_sequence.InsertionSequence;
import org.gentar.biology.mutation.aligned_fasta.AlignedFasta;
import org.gentar.biology.mutation.genetic_type.GeneticMutationType;
import org.gentar.biology.mutation.molecular_type.MolecularMutationType;
import org.gentar.biology.mutation.mutation_deletion.MolecularMutationDeletion;
import org.gentar.biology.mutation.qc_results.MutationQcResult;
import org.gentar.biology.mutation.sequence.MutationSequence;
import org.gentar.biology.plan.attempt.crispr.AlignedFastaDTO;
import org.gentar.biology.plan.attempt.crispr.CanonicalTargetedExonDTO;
import org.gentar.biology.plan.attempt.crispr.TargetedExonDTO;
import org.gentar.biology.plan.attempt.crispr.canonical_targeted_exon.CanonicalTargetedExon;
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
    private final InsertionSequenceMapper insertionSequenceMapper;
    private final GeneticMutationTypeMapper geneticMutationTypeMapper;
    private final MolecularMutationTypeMapper molecularMutationTypeMapper;

    private final MolecularMutationDeletionMapper molecularMutationDeletionMapper;

    private final TargetedExonMapper targetedExonMapper;

    private final CanonicalTargetedExonMapper canonicalTargetedExonMapper;

    private final AlignedFastaMapper alignedFastaMapper;


    public MutationCommonMapper(
            MutationQCResultMapper mutationQCResultMapper,
            MutationCategorizationMapper mutationCategorizationMapper,
            MutationSequenceMapper mutationSequenceMapper, InsertionSequenceMapper insertionSequenceMapper,
            GeneticMutationTypeMapper geneticMutationTypeMapper,
            MolecularMutationTypeMapper molecularMutationTypeMapper, MolecularMutationDeletionMapper molecularMutationDeletionMapper, TargetedExonMapper targetedExonMapper, CanonicalTargetedExonMapper canonicalTargetedExonMapper, AlignedFastaMapper alignedFastaMapper)
    {
        this.mutationQCResultMapper = mutationQCResultMapper;
        this.mutationCategorizationMapper = mutationCategorizationMapper;
        this.mutationSequenceMapper = mutationSequenceMapper;
        this.insertionSequenceMapper = insertionSequenceMapper;
        this.geneticMutationTypeMapper = geneticMutationTypeMapper;
        this.molecularMutationTypeMapper = molecularMutationTypeMapper;
        this.molecularMutationDeletionMapper = molecularMutationDeletionMapper;
        this.targetedExonMapper = targetedExonMapper;
        this.canonicalTargetedExonMapper = canonicalTargetedExonMapper;
        this.alignedFastaMapper = alignedFastaMapper;
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
        mutationCommonDTO.setInsertionSequenceDTOS(
                insertionSequenceMapper.toDtos(mutation.getInsertionSequences()));
        mutationCommonDTO.setMutationCategorizationDTOS(
            mutationCategorizationMapper.toDtos(mutation.getMutationCategorizations()));
        mutationCommonDTO.setMolecularMutationDeletionDTOs(molecularMutationDeletionMapper.toDtos(mutation.getMolecularMutationDeletion()));
        mutationCommonDTO.setTargetedExonDTOS(targetedExonMapper.toDtos(mutation.getTargetedExons()));
        mutationCommonDTO.setCanonicalTargetedExonsDTOS(canonicalTargetedExonMapper.toDtos(mutation.getCanonicalTargetedExons()));
        mutationCommonDTO.setAlignedFastaDTO(alignedFastaMapper.toDtos(mutation.getAlignedFastas()));

        mutationCommonDTO.setIsDeletionCoordinatesUpdatedManually(mutation.getIsDeletionCoordinatesUpdatedManually());
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
            setInsertionSequences(mutation, mutationCommonDTO);
            mutation.setMutationCategorizations(
                new HashSet<>(mutationCategorizationMapper.toEntities(
                    mutationCommonDTO.getMutationCategorizationDTOS())));

            setMolecularMutationDeletion(mutation, mutationCommonDTO);
            setTargetedExons(mutation, mutationCommonDTO);
            setCanonicalTargetedExons(mutation, mutationCommonDTO);
            setAlignedFastas(mutation, mutationCommonDTO);
            mutation.setIsMutationDeletionChecked(mutationCommonDTO.getIsMutationDeletionChecked());
            mutation.setIsDeletionCoordinatesUpdatedManually(mutationCommonDTO.getIsDeletionCoordinatesUpdatedManually());

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

    private void setInsertionSequences(Mutation mutation, MutationCommonDTO mutationCommonDTO)
    {
        List<InsertionSequenceDTO> insertionSequenceDTOS = mutationCommonDTO.getInsertionSequenceDTOS();
        if (insertionSequenceDTOS != null)
        {
            Set<InsertionSequence> insertionSequences =
                    new HashSet<>(
                            insertionSequenceMapper.toEntities(mutationCommonDTO.getInsertionSequenceDTOS()));
            insertionSequences.forEach(x -> x.setMutation(mutation));
            mutation.setInsertionSequences(insertionSequences);
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

    private void setCanonicalTargetedExons(Mutation mutation, MutationCommonDTO mutationCommonDTO)
    {
        List<CanonicalTargetedExonDTO> canonicalTargetedExonDTOS = mutationCommonDTO.getCanonicalTargetedExonsDTOS();
        if (canonicalTargetedExonDTOS != null)
        {
            Set<CanonicalTargetedExon> canonicalTargetedExons =
                    new HashSet<>(
                            canonicalTargetedExonMapper.toEntities(mutationCommonDTO.getCanonicalTargetedExonsDTOS()));
            canonicalTargetedExons.forEach(x -> x.setMutation(mutation));
            mutation.setCanonicalTargetedExons(canonicalTargetedExons);
        }
    }

    private void setAlignedFastas(Mutation mutation, MutationCommonDTO mutationCommonDTO)
    {
        List<AlignedFastaDTO> alignedFastaDTOS = mutationCommonDTO.getAlignedFastaDTO();
        if (alignedFastaDTOS != null)
        {
            Set<AlignedFasta> alignedFastas =
                    new HashSet<>(
                            alignedFastaMapper.toEntities(mutationCommonDTO.getAlignedFastaDTO()));
            alignedFastas.forEach(x -> x.setMutation(mutation));
            mutation.setAlignedFastas(alignedFastas);
        }
    }

}
