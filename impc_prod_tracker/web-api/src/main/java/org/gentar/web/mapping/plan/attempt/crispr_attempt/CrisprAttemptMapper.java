/******************************************************************************
 Copyright 2019 EMBL - European Bioinformatics Institute

 Licensed under the Apache License, Version 2.0 (the
 "License"); you may not use this file except in compliance
 with the License. You may obtain a copy of the License at
 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing,
 software distributed under the License is distributed on an
 "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 either express or implied. See the License for the specific
 language governing permissions and limitations under the
 License.
 */
package org.gentar.web.mapping.plan.attempt.crispr_attempt;

import org.gentar.exceptions.UserOperationFailedException;
import org.gentar.service.biology.plan.crispr.CrisprAttemptService;
import org.gentar.web.dto.plan.production.crispr_attempt.CrisprAttemptDTO;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import org.gentar.biology.crispr_attempt.CrisprAttempt;
import org.gentar.biology.crispr_attempt.assay.Assay;
import org.gentar.biology.crispr_attempt.crispr_attempt_reagent.CrisprAttemptReagent;
import org.gentar.biology.crispr_attempt.delivery_type.DeliveryMethodType;
import org.gentar.biology.crispr_attempt.genotype_primer.GenotypePrimer;
import org.gentar.biology.crispr_attempt.guide.Guide;
import org.gentar.biology.crispr_attempt.mutagenesis_donor.MutagenesisDonor;
import org.gentar.biology.crispr_attempt.nuclease.Nuclease;
import org.gentar.biology.strain.Strain;
import java.util.HashSet;
import java.util.Set;

@Component
public class CrisprAttemptMapper
{
    private ModelMapper modelMapper;
    private GuideMapper guideMapper;
    private NucleaseMapper nucleaseMapper;
    private StrainMapper strainMapper;
    private MutagenesisDonorMapper mutagenesisDonorMapper;
    private GenotypePrimerMapper genotypePrimerMapper;
    private AssayMapper assayMapper;
    private CrisprAttemptService crisprAttemptService;

    private static final String DELIVERY_TYPE_METHOD_NOT_FOUND = "Delivery Method Type [%s]" +
        " does not exist. Please correct the name or create first the delivery type method.";

    public CrisprAttemptMapper(
        ModelMapper modelMapper,
        GuideMapper guideMapper,
        NucleaseMapper nucleaseMapper,
        StrainMapper strainMapper,
        MutagenesisDonorMapper mutagenesisDonorMapper,
        GenotypePrimerMapper genotypePrimerMapper,
        AssayMapper assayMapper,
        CrisprAttemptService crisprAttemptService)
    {
        this.modelMapper = modelMapper;
        this.guideMapper = guideMapper;
        this.nucleaseMapper = nucleaseMapper;
        this.strainMapper = strainMapper;
        this.mutagenesisDonorMapper = mutagenesisDonorMapper;
        this.genotypePrimerMapper = genotypePrimerMapper;
        this.assayMapper = assayMapper;
        this.crisprAttemptService = crisprAttemptService;
    }

    public CrisprAttemptDTO toDto(CrisprAttempt crisprAttempt)
    {
        CrisprAttemptDTO crisprAttemptDTO = null;
        if (crisprAttempt != null)
        {
            crisprAttemptDTO = modelMapper.map(crisprAttempt, CrisprAttemptDTO.class);
            crisprAttemptDTO.setGuideDTOS(guideMapper.toDtos(crisprAttempt.getGuides()));
            crisprAttemptDTO.setGenotypePrimerDTOS(
                genotypePrimerMapper.toDtos(crisprAttempt.getPrimers()));
            crisprAttemptDTO.setMutagenesisDonorDTOS(
                mutagenesisDonorMapper.toDtos(crisprAttempt.getMutagenesisDonors()));
            crisprAttemptDTO.setNucleaseDTOS(nucleaseMapper.toDtos(crisprAttempt.getNucleases()));
        }
        return crisprAttemptDTO;
    }

    public CrisprAttempt toEntity(CrisprAttemptDTO crisprAttemptDTO)
    {
        CrisprAttempt crisprAttempt = modelMapper.map(crisprAttemptDTO, CrisprAttempt.class);
        setAssayType(crisprAttempt, crisprAttemptDTO);
        setStrain(crisprAttempt, crisprAttemptDTO);
        setGuidesToEntity(crisprAttempt, crisprAttemptDTO);
        setDeliveryTypeMethodToEntity(crisprAttempt, crisprAttemptDTO);
        setGenotypePrimersToEntity(crisprAttempt, crisprAttemptDTO);
        setMutagenesisDonorsToEntity(crisprAttempt, crisprAttemptDTO);
        setReagentsToEntity(crisprAttempt, crisprAttemptDTO);
        setNucleasesToEntity(crisprAttempt, crisprAttemptDTO);
        return crisprAttempt;
    }

    private void setAssayType(CrisprAttempt crisprAttempt, CrisprAttemptDTO crisprAttemptDTO)
    {
        Assay assay = assayMapper.toEntity(crisprAttemptDTO.getAssay());
        assay.setCrisprAttempt(crisprAttempt);
        crisprAttempt.setAssay(assay);
    }

    private void setStrain(CrisprAttempt crisprAttempt, CrisprAttemptDTO crisprAttemptDTO)
    {
        Strain strain = strainMapper.toEntity(crisprAttemptDTO.getStrain());
        crisprAttempt.setStrain(strain);
    }

    private void setGuidesToEntity(CrisprAttempt crisprAttempt, CrisprAttemptDTO crisprAttemptDTO)
    {
        Set<Guide> guides = guideMapper.toEntities(crisprAttemptDTO.getGuideDTOS());
        guides.forEach(x -> x.setCrisprAttempt(crisprAttempt));
        crisprAttempt.setGuides(guides);
    }

    private void setGenotypePrimersToEntity(
        CrisprAttempt crisprAttempt, CrisprAttemptDTO crisprAttemptDTO)
    {
        Set<GenotypePrimer> genotypePrimers =
            genotypePrimerMapper.toEntities(crisprAttemptDTO.getGenotypePrimerDTOS());
        genotypePrimers.forEach(x -> x.setCrisprAttempt(crisprAttempt));
        crisprAttempt.setPrimers(genotypePrimers);
    }

    private void setDeliveryTypeMethodToEntity(
        CrisprAttempt crisprAttempt, CrisprAttemptDTO crisprAttemptDTO)
    {
        String deliveryMethodTypeName = crisprAttemptDTO.getDeliveryTypeMethodName();
        if (deliveryMethodTypeName != null)
        {
            DeliveryMethodType deliveryMethodType =
                crisprAttemptService.getDeliveryTypeByName(deliveryMethodTypeName);
            if (deliveryMethodType == null)
            {
                String errorMessage =
                    String.format(DELIVERY_TYPE_METHOD_NOT_FOUND, deliveryMethodTypeName);
                throw new UserOperationFailedException(errorMessage);
            }
            crisprAttempt.setDeliveryMethodType(deliveryMethodType);
        }
    }

    private void setMutagenesisDonorsToEntity(
        CrisprAttempt crisprAttempt, CrisprAttemptDTO crisprAttemptDTO)
    {
        Set<MutagenesisDonor> mutagenesisDonors =
            mutagenesisDonorMapper.toEntities(crisprAttemptDTO.getMutagenesisDonorDTOS());
        mutagenesisDonors.forEach(x -> x.setCrisprAttempt(crisprAttempt));
        crisprAttempt.setMutagenesisDonors(mutagenesisDonors);
    }

    private void setReagentsToEntity(CrisprAttempt crisprAttempt, CrisprAttemptDTO crisprAttemptDTO)
    {
        Set<CrisprAttemptReagent> crisprAttemptReagents = new HashSet<>();
        crisprAttemptReagents.forEach(x -> x.setCrisprAttempt(crisprAttempt));
        crisprAttempt.setCrisprAttemptReagents(crisprAttemptReagents);
    }

    private void setNucleasesToEntity(CrisprAttempt crisprAttempt, CrisprAttemptDTO crisprAttemptDTO)
    {
        Set<Nuclease> nucleases =
            new HashSet<>(nucleaseMapper.toEntities(crisprAttemptDTO.getNucleaseDTOS()));
        nucleases.forEach(x -> x.setCrisprAttempt(crisprAttempt));
        crisprAttempt.setNucleases(nucleases);
    }
}
