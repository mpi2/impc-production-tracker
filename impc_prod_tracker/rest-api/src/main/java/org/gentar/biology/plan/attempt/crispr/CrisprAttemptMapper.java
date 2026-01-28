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
package org.gentar.biology.plan.attempt.crispr;

import org.apache.logging.log4j.util.Strings;
import org.gentar.EntityMapper;
import org.gentar.Mapper;
import org.gentar.biology.strain.StrainMapper;
import org.springframework.stereotype.Component;
import org.gentar.biology.plan.attempt.crispr.assay.Assay;
import org.gentar.biology.plan.attempt.crispr.reagent.CrisprAttemptReagent;
import org.gentar.biology.plan.attempt.crispr.genotype_primer.GenotypePrimer;
import org.gentar.biology.plan.attempt.crispr.guide.Guide;
import org.gentar.biology.plan.attempt.crispr.mutagenesis_donor.MutagenesisDonor;
import org.gentar.biology.plan.attempt.crispr.nuclease.Nuclease;
import org.gentar.biology.strain.Strain;
import java.util.HashSet;
import java.util.Set;

@Component
public class CrisprAttemptMapper implements Mapper<CrisprAttempt, CrisprAttemptDTO>
{
    private final EntityMapper entityMapper;
    private final GuideMapper guideMapper;
    private final NucleaseMapper nucleaseMapper;
    private final StrainMapper strainMapper;
    private final MutagenesisDonorMapper mutagenesisDonorMapper;
    private final GenotypePrimerMapper genotypePrimerMapper;
    private final AssayMapper assayMapper;
    private final CrisprAttemptReagentMapper crisprAttemptReagentMapper;

    public CrisprAttemptMapper(
            EntityMapper entityMapper,
            GuideMapper guideMapper,
            NucleaseMapper nucleaseMapper,
            StrainMapper strainMapper,
            MutagenesisDonorMapper mutagenesisDonorMapper,
            GenotypePrimerMapper genotypePrimerMapper,
            AssayMapper assayMapper,
            CrisprAttemptReagentMapper crisprAttemptReagentMapper)
    {
        this.entityMapper = entityMapper;
        this.guideMapper = guideMapper;
        this.nucleaseMapper = nucleaseMapper;
        this.strainMapper = strainMapper;
        this.mutagenesisDonorMapper = mutagenesisDonorMapper;
        this.genotypePrimerMapper = genotypePrimerMapper;
        this.assayMapper = assayMapper;
        this.crisprAttemptReagentMapper = crisprAttemptReagentMapper;
    }

    @Override
    public CrisprAttemptDTO toDto(CrisprAttempt crisprAttempt)
    {
        CrisprAttemptDTO crisprAttemptDTO = null;
        if (crisprAttempt != null)
        {
            crisprAttemptDTO = entityMapper.toTarget(crisprAttempt, CrisprAttemptDTO.class);

            crisprAttemptDTO.setGuideDTOS(guideMapper.toDtos(crisprAttempt.getGuides()));
            crisprAttemptDTO.setGenotypePrimerDTOS(
                    genotypePrimerMapper.toDtos(crisprAttempt.getPrimers()));
            crisprAttemptDTO.setMutagenesisDonorDTOS(
                    mutagenesisDonorMapper.toDtos(crisprAttempt.getMutagenesisDonors()));
            crisprAttemptDTO.setNucleaseDTOS(nucleaseMapper.toDtos(crisprAttempt.getNucleases()));
            crisprAttemptDTO.setCrisprAttemptReagentDTOS(
                    crisprAttemptReagentMapper.toDtos(crisprAttempt.getCrisprAttemptReagents()));
        }
        return crisprAttemptDTO;
    }

    @Override
    public CrisprAttempt toEntity(CrisprAttemptDTO crisprAttemptDTO)
    {
        CrisprAttempt crisprAttempt = new CrisprAttempt();
        
        crisprAttempt.setImitsMiAttempt(crisprAttemptDTO.getImitsMiAttempt());
        crisprAttempt.setMiDate(crisprAttemptDTO.getMiDate());
        crisprAttempt.setMiExternalRef(crisprAttemptDTO.getMiExternalRef());
        crisprAttempt.setExperimental(crisprAttemptDTO.getExperimental());
        crisprAttempt.setMutagenesisExternalRef(crisprAttemptDTO.getMutagenesisExternalRef());
        crisprAttempt.setTotalEmbryosInjected(crisprAttemptDTO.getTotalEmbryosInjected());
        crisprAttempt.setTotalEmbryosSurvived(crisprAttemptDTO.getTotalEmbryosSurvived());
        crisprAttempt.setEmbryo2Cell(crisprAttemptDTO.getEmbryo2Cell());
        crisprAttempt.setEmbryoTransferDay(crisprAttemptDTO.getEmbryoTransferDay());
        crisprAttempt.setTotalTransferred(crisprAttemptDTO.getTotalTransferred());
        
        if (Strings.isBlank(crisprAttemptDTO.getComment()))
        {
            crisprAttempt.setComment(null);
        }
        else
        {
            crisprAttempt.setComment(crisprAttemptDTO.getComment());
        }
        
        // Set related entities
        setAssay(crisprAttempt, crisprAttemptDTO);
        setStrain(crisprAttempt, crisprAttemptDTO);
        setGuidesToEntity(crisprAttempt, crisprAttemptDTO);
        setGenotypePrimersToEntity(crisprAttempt, crisprAttemptDTO);
        setMutagenesisDonorsToEntity(crisprAttempt, crisprAttemptDTO);
        setReagentsToEntity(crisprAttempt, crisprAttemptDTO);
        setNucleasesToEntity(crisprAttempt, crisprAttemptDTO);
        return crisprAttempt;
    }

    private void setAssay(CrisprAttempt crisprAttempt, CrisprAttemptDTO crisprAttemptDTO)
    {
        Assay assay = assayMapper.toEntity(crisprAttemptDTO.getAssay());
        if (assay != null)
        {
            assay.setCrisprAttempt(crisprAttempt);
        }
        crisprAttempt.setAssay(assay);
    }

    private void setStrain(CrisprAttempt crisprAttempt, CrisprAttemptDTO crisprAttemptDTO)
    {
        Strain strain = strainMapper.toEntity(crisprAttemptDTO.getStrainName());
        crisprAttempt.setStrain(strain);
    }

    private void setGuidesToEntity(CrisprAttempt crisprAttempt, CrisprAttemptDTO crisprAttemptDTO)
    {
        Set<Guide> guides = guideMapper.toEntities(crisprAttemptDTO);
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
        Set<CrisprAttemptReagent> crisprAttemptReagents =
                new HashSet<>(
                        crisprAttemptReagentMapper.toEntities(crisprAttemptDTO.getCrisprAttemptReagentDTOS()));
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