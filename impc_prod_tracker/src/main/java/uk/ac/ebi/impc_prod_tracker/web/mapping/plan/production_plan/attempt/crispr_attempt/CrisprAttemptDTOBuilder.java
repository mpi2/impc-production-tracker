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
import uk.ac.ebi.impc_prod_tracker.data.biology.attempt.crispr_attempt.genotype_primer.GenotypePrimer;
import uk.ac.ebi.impc_prod_tracker.data.biology.attempt.crispr_attempt.mutagenesis_donor.MutagenesisDonor;
import uk.ac.ebi.impc_prod_tracker.data.biology.attempt.crispr_attempt.mutagenesis_donor.preparation_type.PreparationType;
import uk.ac.ebi.impc_prod_tracker.data.biology.attempt.crispr_attempt.nuclease.Nuclease;
import uk.ac.ebi.impc_prod_tracker.service.plan.CrisprAttempService;
import uk.ac.ebi.impc_prod_tracker.web.dto.plan.production_plan.attempt.crispr_attempt.CrisprAttemptDTO;
import uk.ac.ebi.impc_prod_tracker.web.dto.plan.production_plan.attempt.crispr_attempt.MutagenesisDonorDTO;
import uk.ac.ebi.impc_prod_tracker.web.dto.plan.production_plan.attempt.crispr_attempt.NucleaseDTO;

import java.util.ArrayList;
import java.util.List;
//TODO create mappers for all the nested entities that requiere one instead of having
// all the logic in this class.
@Component
public class CrisprAttemptDTOBuilder
{
    private CrisprAttempService crisprAttempService;
    private GuideMapper guideMapper;
    private ReagentMapper reagentMapper;
    private GenotypePrimerMapper genotypePrimerMapper;
    private ModelMapper modelMapper;
    private static final String ELECTROPORATION_METHOD = "Electroporation";
    public CrisprAttemptDTOBuilder(
            CrisprAttempService crisprAttempService,
            GuideMapper guideMapper,
            ReagentMapper reagentMapper,
            GenotypePrimerMapper genotypePrimerMapper, ModelMapper modelMapper)
    {
        this.crisprAttempService = crisprAttempService;
        this.guideMapper = guideMapper;
        this.reagentMapper = reagentMapper;
        this.genotypePrimerMapper = genotypePrimerMapper;
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
        setDeliveryTypeNameDto(crisprAttemptDTO, crisprAttempt);
        setReagentNamesDto(crisprAttemptDTO, crisprAttempt);
        setPrimersDto(crisprAttemptDTO, crisprAttempt);
        setNucleasesDto(crisprAttemptDTO, crisprAttempt);
        setMutagenesisDonorsDto(crisprAttemptDTO, crisprAttempt);
        setGuidesDto(crisprAttemptDTO, crisprAttempt);
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
            crisprAttempt.getAssay().setAssayType(assayType);
        }
    }
    private void setDeliveryTypeToEntity(CrisprAttempt crisprAttempt, String deliveryTypeName)
    {
        DeliveryMethodType deliveryMethodType = crisprAttempService.getDeliveryTypeByName(deliveryTypeName);
        if (deliveryMethodType != null)
        {
            crisprAttempt.setDeliveryMethodType(deliveryMethodType);
        }
    }
    private void setDeliveryTypeNameDto(
            CrisprAttemptDTO crisprAttemptDTO, final CrisprAttempt crisprAttempt)
    {
        DeliveryMethodType deliveryMethodType = crisprAttempt.getDeliveryMethodType();
        if (deliveryMethodType != null)
        {
            crisprAttemptDTO.setDeliveryMethod(deliveryMethodType.getName());
            if (ELECTROPORATION_METHOD.equals(deliveryMethodType.getName()))
            {
                crisprAttemptDTO.setVoltage(crisprAttempt.getVoltage());
                crisprAttemptDTO.setNoOfPulses(crisprAttempt.getNoOfPulses());
            }
        }
    }
    private void setReagentNamesDto(
            CrisprAttemptDTO crisprAttemptDTO, final CrisprAttempt crisprAttempt)
    {
        crisprAttemptDTO.setReagents(reagentMapper.toDtos(crisprAttempt.getCrisprAttemptReagents()));
    }
    private void setPrimersDto(CrisprAttemptDTO crisprAttemptDTO, final CrisprAttempt crisprAttempt)
    {
        List<GenotypePrimer> genotypePrimers =
                crisprAttempService.getGenotypePrimersByCrisprAttempt(crisprAttempt);
        crisprAttemptDTO.setPrimers(genotypePrimerMapper.toDtos(genotypePrimers));
    }
    private void setNucleasesDto(CrisprAttemptDTO crisprAttemptDTO, final CrisprAttempt crisprAttempt)
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
    private void setMutagenesisDonorsDto(
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
    private void setGuidesDto(CrisprAttemptDTO crisprAttemptDTO, CrisprAttempt crisprAttempt)
    {
        crisprAttemptDTO.setGuides(guideMapper.toDtos(crisprAttempt.getGuides()));
    }
}