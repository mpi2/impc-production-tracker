/*******************************************************************************
 * Copyright 2019 EMBL - European Bioinformatics Institute
 *
 * Licensed under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific
 * language governing permissions and limitations under the
 * License.
 *******************************************************************************/
package uk.ac.ebi.impc_prod_tracker.controller.project.plan.production_plan.attempt.crispr_attempt;

import org.springframework.stereotype.Component;
import uk.ac.ebi.impc_prod_tracker.controller.project.plan.production_plan.attempt.GenotypePrimerDTO;
import uk.ac.ebi.impc_prod_tracker.controller.project.plan.production_plan.attempt.MutagenesisDonorDTO;
import uk.ac.ebi.impc_prod_tracker.controller.project.plan.production_plan.attempt.NucleaseDTO;
import uk.ac.ebi.impc_prod_tracker.controller.project.plan.production_plan.attempt.ReagentDTO;
import uk.ac.ebi.impc_prod_tracker.data.biology.attempt.crispr_attempt.CrisprAttempt;
import uk.ac.ebi.impc_prod_tracker.data.biology.attempt.crispr_attempt.nuclease.Nuclease;
import uk.ac.ebi.impc_prod_tracker.data.biology.genotype_primer.GenotypePrimer;
import uk.ac.ebi.impc_prod_tracker.data.biology.mutagenesis_donor.MutagenesisDonor;
import uk.ac.ebi.impc_prod_tracker.data.biology.plan_reagent.PlanReagent;
import uk.ac.ebi.impc_prod_tracker.data.biology.preparation_type.PreparationType;
import uk.ac.ebi.impc_prod_tracker.data.experiment.assay_type.AssayType;
import uk.ac.ebi.impc_prod_tracker.data.experiment.delivery_type.DeliveryType;
import uk.ac.ebi.impc_prod_tracker.data.experiment.plan.Plan;
import uk.ac.ebi.impc_prod_tracker.service.plan.CrisprAttempService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class CrisprAttemptDTOBuilder
{
    private CrisprAttempService crisprAttempService;

    private static final String ELECTROPORATION_METHOD = "Electroporation";

    public CrisprAttemptDTOBuilder(CrisprAttempService crisprAttempService)
    {
        this.crisprAttempService = crisprAttempService;
    }

    public CrisprAttemptDTO buildFromPlan(final Plan plan)
    {
        CrisprAttemptDTO crisprAttemptDTO = new CrisprAttemptDTO();
        Optional<CrisprAttempt> crisprAttemptOpt =
            crisprAttempService.getCrisprAttemptByPlanId(plan.getId());
        if (crisprAttemptOpt.isPresent())
        {
            CrisprAttempt crisprAttempt = crisprAttemptOpt.get();
            crisprAttemptDTO.setMiDate(crisprAttempt.getMiDate());

            crisprAttemptDTO.setMiExternalRef(crisprAttempt.getMiExternalRef());
            crisprAttemptDTO.setIndividuallySetGrnaConcentrations(
                crisprAttempt.getIndividuallySetGrnaConcentrations());
            crisprAttemptDTO.setGuidesGeneratedInPlasmid(
                crisprAttempt.getGuidesGeneratedInPlasmid());
            crisprAttemptDTO.setGRnaConcentration(crisprAttempt.getGrnaConcentration());
            crisprAttemptDTO.setNoG0WhereMutationDetected(
                crisprAttempt.getNoG0WhereMutationDetected());
            crisprAttemptDTO.setNoNhejG0Mutants(crisprAttempt.getNoNhejG0Mutants());
            crisprAttemptDTO.setNoDeletionG0Mutants(crisprAttempt.getNoDeletionG0Mutants());
            crisprAttemptDTO.setNoHdrG0Mutants(crisprAttempt.getNoHdrG0Mutants());
            crisprAttempt.setNoHdrG0MutantsAllDonorsInserted(
                crisprAttempt.getNoHdrG0MutantsAllDonorsInserted());
            crisprAttemptDTO.setNoHdrG0MutantsSubsetDonorsInserted(
                crisprAttempt.getNoHdrG0MutantsSubsetDonorsInserted());
            crisprAttemptDTO.setTotalEmbryosInjected(crisprAttempt.getTotalEmbryosInjected());
            crisprAttemptDTO.setTotalEmbryosSurvived(crisprAttempt.getTotalEmbryosSurvived());
            crisprAttemptDTO.setTotalTransferred(crisprAttempt.getTotalTransferred());
            crisprAttemptDTO.setNoFounderPups(crisprAttempt.getNoFounderPups());
            crisprAttempt.setNoFounderSelectedForBreeding(
                crisprAttempt.getNoFounderSelectedForBreeding());
            crisprAttemptDTO.setFounderNumAssays(crisprAttempt.getFounderNumAssays());
            crisprAttemptDTO.setExperimental(crisprAttempt.getExperimental());
            crisprAttemptDTO.setComment(crisprAttempt.getComment());
            AssayType assayType = crisprAttempt.getAssayType();
            if (assayType != null)
            {
                crisprAttemptDTO.setAssayType(assayType.getName());
            }
            crisprAttemptDTO.setEmbryoTransferDay(crisprAttempt.getEmbryoTransferDay());
            crisprAttemptDTO.setEmbryo2Cell(crisprAttempt.getEmbryo2Cell());

            setDeliveryTypeName(crisprAttemptDTO, crisprAttempt);
            setReagentNames(crisprAttemptDTO, plan);
            setPrimers(crisprAttemptDTO, crisprAttempt);
            setNucleases(crisprAttemptDTO, crisprAttempt);
            setMutagenesisDonors(crisprAttemptDTO, crisprAttempt);
        }
        return crisprAttemptDTO;
    }

    private void setDeliveryTypeName(
        CrisprAttemptDTO crisprAttemptDTO, final CrisprAttempt crisprAttempt)
    {
        DeliveryType deliveryType = crisprAttempt.getDeliveryType();
        if (deliveryType != null)
        {
            crisprAttemptDTO.setDeliveryMethod(deliveryType.getName());
            if (ELECTROPORATION_METHOD.equals(deliveryType.getName()))
            {
                crisprAttemptDTO.setVoltage(crisprAttempt.getVoltage());
                crisprAttemptDTO.setNumberOfPulses(crisprAttempt.getNoOfPulses());
            }
        }
    }

    private void setReagentNames(CrisprAttemptDTO crisprAttemptDTO, final Plan plan)
    {
        List<ReagentDTO> reagents = new ArrayList<>();
        for (PlanReagent planReagent : plan.getPlanReagents())
        {
            reagents.add(new ReagentDTO(
                planReagent.getReagent().getName(),
                planReagent.getConcentration()));
        }
        crisprAttemptDTO.setReagents(reagents);
    }

    private void setPrimers(CrisprAttemptDTO crisprAttemptDTO, final CrisprAttempt crisprAttempt)
    {
        List<GenotypePrimerDTO> genotypePrimerList = new ArrayList<>();
        Iterable<GenotypePrimer> genotypePrimers =
            crisprAttempService.getGenotypePrimersByCrisprAttempt(crisprAttempt);
        genotypePrimers.forEach(p -> genotypePrimerList.add(
            new GenotypePrimerDTO(
                p.getSequence(),
                p.getName(),
                p.getChromosome(),
                p.getStart(),
                p.getStop())));
        crisprAttemptDTO.setPrimers(genotypePrimerList);
    }

    private void setNucleases(CrisprAttemptDTO crisprAttemptDTO, final CrisprAttempt crisprAttempt)
    {
        List<NucleaseDTO> nucleaseList = new ArrayList<>();
        Iterable<Nuclease> nucleases =
            crisprAttempService.getNucleasesByCrisprAttempt(crisprAttempt);
        nucleases.forEach(p ->
        {
            String typeName = null;
            if (p.getNucleaseType() != null)
            {
                typeName = p.getNucleaseType().getName();
            }
            String typeClassName = null;
            if (p.getNucleaseType().getNucleaseClass() != null)
            {
                typeClassName = p.getNucleaseType().getNucleaseClass().getName();
            }
            nucleaseList.add(
                new NucleaseDTO(
                    typeName,
                    typeClassName,
                    p.getConcentration()));
        });
        crisprAttemptDTO.setNucleases(nucleaseList);
    }

    private void setMutagenesisDonors(
        CrisprAttemptDTO crisprAttemptDTO, final CrisprAttempt crisprAttempt)
    {
        List<MutagenesisDonorDTO> mutagenesisDonorDTOS = new ArrayList<>();
        Iterable<MutagenesisDonor> mutagenesisDonors =
            crisprAttempService.getMutagenesisDonorsByCrisprAttempt(crisprAttempt);
        mutagenesisDonors.forEach(p ->
            {
                String preparationTypeName = null;
                PreparationType preparationType = p.getPreparationType();
                if (preparationType != null)
                {
                    preparationTypeName = preparationType.getName();
                }
                MutagenesisDonorDTO mutagenesisDonorDTO = new MutagenesisDonorDTO(
                    p.getId(), p.getConcentration(), preparationTypeName, p.getOligoSequenceFasta());
                mutagenesisDonorDTOS.add(mutagenesisDonorDTO);
            }
        );
        crisprAttemptDTO.setMutagenesisDonors(mutagenesisDonorDTOS);
    }
}
