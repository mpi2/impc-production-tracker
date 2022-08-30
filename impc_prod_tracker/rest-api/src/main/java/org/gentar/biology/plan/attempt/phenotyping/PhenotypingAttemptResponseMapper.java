package org.gentar.biology.plan.attempt.phenotyping;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import org.gentar.EntityMapper;
import org.gentar.Mapper;
import org.gentar.biology.plan.attempt.phenotyping.stage.PhenotypingStageController;
import org.gentar.biology.plan.attempt.phenotyping.stage.PhenotypingStage;
import org.gentar.biology.strain.StrainMapper;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class PhenotypingAttemptResponseMapper implements Mapper<PhenotypingAttempt, PhenotypingAttemptResponseDTO>
{
    private EntityMapper entityMapper;
    private StrainMapper strainMapper;
    private PhenotypingAttemptCommonMapper phenotypingAttemptCommonMapper;

    public PhenotypingAttemptResponseMapper(EntityMapper entityMapper, StrainMapper strainMapper, PhenotypingAttemptCommonMapper phenotypingAttemptCommonMapper)
    {
        this.entityMapper = entityMapper;
        this.strainMapper = strainMapper;
        this.phenotypingAttemptCommonMapper = phenotypingAttemptCommonMapper;
    }

    @Override
    public PhenotypingAttemptResponseDTO toDto(PhenotypingAttempt phenotypingAttempt)
    {
        PhenotypingAttemptResponseDTO phenotypingAttemptResponseDTO = new PhenotypingAttemptResponseDTO();
        if (phenotypingAttempt != null)
        {
            phenotypingAttemptResponseDTO.setPhenotypingAttemptCommonDTO(
                    phenotypingAttemptCommonMapper.toDto(phenotypingAttempt));
            addPhenotypingStageLinks(phenotypingAttemptResponseDTO, phenotypingAttempt);
        }
        return phenotypingAttemptResponseDTO;
    }

    private void addPhenotypingStageLinks(PhenotypingAttemptResponseDTO phenotypingAttemptResponseDTO,
                                          PhenotypingAttempt phenotypingAttempt)
    {
        List<Link> links = new ArrayList<>();
        Set<PhenotypingStage> phenotypingStages = phenotypingAttempt.getPhenotypingStages();
        if (phenotypingStages != null)
        {
            phenotypingStages.forEach(x ->{
                Link link = linkTo(methodOn(PhenotypingStageController.class)
                    .findOneByPlanAndPsn(phenotypingAttempt.getPlan().getPin(), x.getPsn()))
                    .withRel("phenotypingStages");
                link = link.withHref(decode(link.getHref()));
                links.add(link);
                });
        }
        phenotypingAttemptResponseDTO.add(links);
    }

    @Override
    public PhenotypingAttempt toEntity(PhenotypingAttemptResponseDTO dto)
    {
        return null;
    }

    private String decode(String value) {
        try {
            return URLDecoder.decode(value, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return value;
    }
}
