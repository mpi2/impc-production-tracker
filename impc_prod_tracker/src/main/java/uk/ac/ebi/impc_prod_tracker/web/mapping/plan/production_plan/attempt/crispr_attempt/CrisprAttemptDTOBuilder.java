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
package uk.ac.ebi.impc_prod_tracker.web.mapping.plan.production_plan.attempt.crispr_attempt;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import uk.ac.ebi.impc_prod_tracker.data.biology.attempt.crispr_attempt.CrisprAttempt;
import uk.ac.ebi.impc_prod_tracker.data.biology.attempt.crispr_attempt.assay.assay_type.AssayType;
import uk.ac.ebi.impc_prod_tracker.data.biology.attempt.crispr_attempt.delivery_type.DeliveryMethodType;
import uk.ac.ebi.impc_prod_tracker.data.biology.attempt.crispr_attempt.mutagenesis_donor.MutagenesisDonor;
import uk.ac.ebi.impc_prod_tracker.data.biology.attempt.crispr_attempt.mutagenesis_donor.preparation_type.PreparationType;
import uk.ac.ebi.impc_prod_tracker.data.biology.attempt.crispr_attempt.nuclease.Nuclease;
import uk.ac.ebi.impc_prod_tracker.service.plan.CrisprAttempService;
import uk.ac.ebi.impc_prod_tracker.web.dto.plan.production_plan.attempt.MutagenesisDonorDTO;
import uk.ac.ebi.impc_prod_tracker.web.dto.plan.production_plan.attempt.NucleaseDTO;
import uk.ac.ebi.impc_prod_tracker.web.dto.plan.production_plan.attempt.crispr_attempt.CrisprAttemptDTO;

import java.util.ArrayList;
import java.util.List;

@Component
public class CrisprAttemptDTOBuilder
{
    private CrisprAttempService crisprAttempService;
    private ModelMapper modelMapper;

    private static final String ELECTROPORATION_METHOD = "Electroporation";

    public CrisprAttemptDTOBuilder(CrisprAttempService crisprAttempService, ModelMapper modelMapper)
    {
        this.crisprAttempService = crisprAttempService;
        this.modelMapper = modelMapper;
    }

    /**
     * Maps a {@link CrisprAttempt} to a {@link CrisprAttemptDTO} object.
     *
     * @param crisprAttempt The {@link CrisprAttempt} object.
     * @return a {@link CrisprAttemptDTO} object.
     */
    public CrisprAttemptDTO convertToDto(CrisprAttempt crisprAttempt)
    {
        CrisprAttemptDTO crisprAttemptDTO = modelMapper.map(crisprAttempt, CrisprAttemptDTO.class);
        setDeliveryTypeNameToDto(crisprAttemptDTO, crisprAttempt);
        setReagentNamesToDto(crisprAttemptDTO, crisprAttempt);
        setPrimersToDto(crisprAttemptDTO, crisprAttempt);
        setNucleasesToDto(crisprAttemptDTO, crisprAttempt);
        setMutagenesisDonorsToDto(crisprAttemptDTO, crisprAttempt);
        return crisprAttemptDTO;
    }

    /**
     * Maps a {@link CrisprAttemptDTO} to a {@link CrisprAttempt} object.
     *
     * @param crisprAttemptDTO The {@link CrisprAttemptDTO} object.
     * @return a {@link CrisprAttempt} object.
     */
    public CrisprAttempt convertToEntity(CrisprAttemptDTO crisprAttemptDTO)
    {
        CrisprAttempt crisprAttempt = modelMapper.map(crisprAttemptDTO, CrisprAttempt.class);
        setAssayTypeToEntity(crisprAttempt, crisprAttemptDTO.getAssayTypeName());
        setDeliveryTypeToEntity(crisprAttempt, crisprAttemptDTO.getDeliveryMethod());
        return crisprAttempt;
    }

    private void setAssayTypeToEntity(CrisprAttempt crisprAttempt, String assayTypeName)
    {
        AssayType assayType = crisprAttempService.getAssayTypeByName(assayTypeName);
        if (assayType != null)
        {
//            crisprAttempt.setAssayType(assayType);
        }
    }

    private void setDeliveryTypeToEntity(CrisprAttempt crisprAttempt, String deliveryTypeName)
    {
        DeliveryMethodType deliveryType = crisprAttempService.getDeliveryTypeByName(deliveryTypeName);
        if (deliveryType != null)
        {
//            crisprAttempt.setDeliveryType(deliveryType);
        }
    }

    private void setDeliveryTypeNameToDto(
        CrisprAttemptDTO crisprAttemptDTO, final CrisprAttempt crisprAttempt)
    {
//        DeliveryMethodType deliveryType = crisprAttempt.getDeliveryType();
//        if (deliveryType != null)
//        {
//            crisprAttemptDTO.setDeliveryMethod(deliveryType.getName());
//            if (ELECTROPORATION_METHOD.equals(deliveryType.getName()))
//            {
//                crisprAttemptDTO.setVoltage(crisprAttempt.getVoltage());
//                crisprAttemptDTO.setNoOfPulses(crisprAttempt.getNoOfPulses());
//            }
//        }
    }

    private void setReagentNamesToDto(CrisprAttemptDTO crisprAttemptDTO, final CrisprAttempt crisprAttempt)
    {
//        List<ReagentDTO> reagents = new ArrayList<>();
//        crisprAttempt.getCrisprAttemptReagents().forEach(x ->
//            reagents.add(new ReagentDTO(
//                x.getReagent().getName(),
//                x.getConcentration())));
//        crisprAttemptDTO.setReagents(reagents);
    }

    private void setPrimersToDto(CrisprAttemptDTO crisprAttemptDTO, final CrisprAttempt crisprAttempt)
    {
//        List<GenotypePrimerDTO> genotypePrimerList = new ArrayList<>();
//        List<GenotypePrimer> genotypePrimers =
//            crisprAttempService.getGenotypePrimersByCrisprAttempt(crisprAttempt);
//        genotypePrimers.forEach(p -> genotypePrimerList.add(
//            new GenotypePrimerDTO(
//                p.getSequence(),
//                p.getName(),
//                p.getChromosome(),
//                p.getStart(),
//                p.getStop())));
//        crisprAttemptDTO.setPrimers(genotypePrimerList);
    }

    private void setNucleasesToDto(CrisprAttemptDTO crisprAttemptDTO, final CrisprAttempt crisprAttempt)
    {
        List<NucleaseDTO> nucleaseList = new ArrayList<>();
        List<Nuclease> nucleases =
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

    private void setMutagenesisDonorsToDto(
        CrisprAttemptDTO crisprAttemptDTO, final CrisprAttempt crisprAttempt)
    {
        List<MutagenesisDonorDTO> mutagenesisDonorDTOS = new ArrayList<>();
        List<MutagenesisDonor> mutagenesisDonors =
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
