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
package org.gentar.biology.plan.attempt.crispr_attempt;

import org.gentar.biology.plan.attempt.crispr.mutagenesis_strategy.MutagenesisStrategy;
import org.gentar.biology.plan.attempt.crispr.CrisprAttemptService;
import org.gentar.biology.plan.attempt.crispr.reagent.Reagent;
import org.gentar.biology.plan.production.crispr_attempt.CrisprAttemptDTO;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import org.gentar.biology.plan.attempt.crispr.CrisprAttempt;
import org.gentar.biology.plan.attempt.crispr.assay.Assay;
import org.gentar.biology.plan.attempt.crispr.genotype_primer.GenotypePrimer;
import org.gentar.biology.plan.attempt.crispr.guide.Guide;
import org.gentar.biology.plan.attempt.crispr.mutagenesis_donor.MutagenesisDonor;
import org.gentar.biology.plan.attempt.crispr.nuclease.Nuclease;
import org.gentar.biology.strain.Strain;
import java.util.HashSet;
import java.util.Set;

@Component
public class CrisprAttemptMapper
{
    private ModelMapper modelMapper;
    private GuideMapper guideMapper;
    private NucleaseMapper nucleaseMapper;
    private ReagentMapper reagentMapper;
    private StrainMapper strainMapper;
    private MutagenesisDonorMapper mutagenesisDonorMapper;
    private MutagenesisStrategyMapper mutagenesisStrategyMapper;
    private GenotypePrimerMapper genotypePrimerMapper;
    private AssayMapper assayMapper;
    private CrisprAttemptService crisprAttemptService;

    public CrisprAttemptMapper(
        ModelMapper modelMapper,
        GuideMapper guideMapper,
        NucleaseMapper nucleaseMapper,
        ReagentMapper reagentMapper,
        StrainMapper strainMapper,
        MutagenesisDonorMapper mutagenesisDonorMapper,
        MutagenesisStrategyMapper mutagenesisStrategyMapper, GenotypePrimerMapper genotypePrimerMapper,
        AssayMapper assayMapper,
        CrisprAttemptService crisprAttemptService)
    {
        this.modelMapper = modelMapper;
        this.guideMapper = guideMapper;
        this.nucleaseMapper = nucleaseMapper;
        this.reagentMapper = reagentMapper;
        this.strainMapper = strainMapper;
        this.mutagenesisDonorMapper = mutagenesisDonorMapper;
        this.mutagenesisStrategyMapper = mutagenesisStrategyMapper;
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
            crisprAttemptDTO.setMutagenesisStrategyDTO(
                mutagenesisStrategyMapper.toDto(crisprAttempt.getStrategy())
            );
            crisprAttemptDTO.setNucleaseDTOS(nucleaseMapper.toDtos(crisprAttempt.getNucleases()));
            crisprAttemptDTO.setReagentDTOS(reagentMapper.toDtos(crisprAttempt.getReagents()));
        }
        return crisprAttemptDTO;
    }

    public CrisprAttempt toEntity(CrisprAttemptDTO crisprAttemptDTO)
    {
        CrisprAttempt crisprAttempt = modelMapper.map(crisprAttemptDTO, CrisprAttempt.class);
        setAssayType(crisprAttempt, crisprAttemptDTO);
        setStrain(crisprAttempt, crisprAttemptDTO);
        setGuidesToEntity(crisprAttempt, crisprAttemptDTO);
        setGenotypePrimersToEntity(crisprAttempt, crisprAttemptDTO);
        setMutagenesisDonorsToEntity(crisprAttempt, crisprAttemptDTO);
        setMutagenesisStrategyToEntity(crisprAttempt, crisprAttemptDTO);
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

    private void setMutagenesisDonorsToEntity(
        CrisprAttempt crisprAttempt, CrisprAttemptDTO crisprAttemptDTO)
    {
        Set<MutagenesisDonor> mutagenesisDonors =
            mutagenesisDonorMapper.toEntities(crisprAttemptDTO.getMutagenesisDonorDTOS());
        mutagenesisDonors.forEach(x -> x.setCrisprAttempt(crisprAttempt));
        crisprAttempt.setMutagenesisDonors(mutagenesisDonors);
    }

    private void setMutagenesisStrategyToEntity(
        CrisprAttempt crisprAttempt, CrisprAttemptDTO crisprAttemptDTO)
    {
        MutagenesisStrategy mutagenesisStrategy =
            mutagenesisStrategyMapper.toEntity(crisprAttemptDTO.getMutagenesisStrategyDTO());
        crisprAttempt.setStrategy(mutagenesisStrategy);
    }

    private void setReagentsToEntity(CrisprAttempt crisprAttempt, CrisprAttemptDTO crisprAttemptDTO)
    {
        Set<Reagent> reagents = new HashSet<Reagent>(reagentMapper.toEntities(crisprAttemptDTO.getReagentDTOS()));
        crisprAttempt.setReagents(reagents);
   }

    private void setNucleasesToEntity(CrisprAttempt crisprAttempt, CrisprAttemptDTO crisprAttemptDTO)
    {
        Set<Nuclease> nucleases =
            new HashSet<>(nucleaseMapper.toEntities(crisprAttemptDTO.getNucleaseDTOS()));
        nucleases.forEach(x -> x.setCrisprAttempt(crisprAttempt));
        crisprAttempt.setNucleases(nucleases);
    }
}
