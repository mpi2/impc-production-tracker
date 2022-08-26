package org.gentar.biology.targ_rep.allele;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import org.gentar.Mapper;
import org.gentar.biology.targ_rep.TargRepAlleleResponseDTO;
import org.gentar.biology.targ_rep.TargRepMutationSubtypeDTO;
import org.gentar.biology.targ_rep.TargRepMutationTypeDTO;
import org.gentar.biology.targ_rep.allele.DistributionQc.MgiAlleleAccessionMapper;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

/**
 * TargRepAlleleResponseMapper.
 */
@Component
public class TargRepAlleleResponseMapper
    implements Mapper<TargRepAllele, TargRepAlleleResponseDTO> {
    private final MgiAlleleAccessionMapper mgiAlleleAccessionMapper;

    private final TargRepMutationTypeMapper targRepMutationTypeMapper;
    private final TargRepMutationSubtypeMapper targRepMutationSubtypeMapper;

    public TargRepAlleleResponseMapper(
        MgiAlleleAccessionMapper mgiAlleleAccessionMapper,
        TargRepMutationTypeMapper targRepMutationTypeMapper,
        TargRepMutationSubtypeMapper targRepMutationSubtypeMapper) {
        this.mgiAlleleAccessionMapper = mgiAlleleAccessionMapper;
        this.targRepMutationTypeMapper = targRepMutationTypeMapper;
        this.targRepMutationSubtypeMapper = targRepMutationSubtypeMapper;
    }

    @Override
    public TargRepAlleleResponseDTO toDto(TargRepAllele targRepAllele) {

        TargRepAlleleResponseDTO targRepAlleleResponseDto = new TargRepAlleleResponseDTO();

        targRepAlleleResponseDto.setId(targRepAllele.getId());
        targRepAlleleResponseDto.setAssembly(targRepAllele.getAssembly());
        targRepAlleleResponseDto.setChromosome(targRepAllele.getChromosome());
        targRepAlleleResponseDto.setProjectDesignId(targRepAllele.getProjectDesignId());
        targRepAlleleResponseDto.setSubtypeDescription(targRepAllele.getSubtypeDescription());
        targRepAlleleResponseDto.setHomologyArmStart(targRepAllele.getHomologyArmStart());
        targRepAlleleResponseDto.setHomologyArmEnd(targRepAllele.getHomologyArmEnd());
        targRepAlleleResponseDto.setCassetteStart(targRepAllele.getCassetteStart());
        targRepAlleleResponseDto.setCassetteEnd(targRepAllele.getCassetteEnd());
        targRepAlleleResponseDto.setLoxpStart(targRepAllele.getLoxpStart());
        targRepAlleleResponseDto.setLoxpEnd(targRepAllele.getLoxpEnd());
        targRepAlleleResponseDto.setCassette(targRepAllele.getCassette());
        targRepAlleleResponseDto.setCassetteType(targRepAllele.getCassetteType());
        targRepAlleleResponseDto.setBackbone(targRepAllele.getBackbone());
        targRepAlleleResponseDto.setFloxedStartExon(targRepAllele.getFloxedStartExon());
        targRepAlleleResponseDto.setFloxedEndExon(targRepAllele.getFloxedEndExon());
        targRepAlleleResponseDto=mgiAlleleAccessionMapper.mgiAlleleIdToDto(targRepAllele,targRepAlleleResponseDto);
        setTargRepMutationTypeDto(targRepAlleleResponseDto, targRepAllele);
        setTargRepMutationSubtypeDto(targRepAlleleResponseDto, targRepAllele);


        addSelfLink(targRepAlleleResponseDto, targRepAllele);

        return targRepAlleleResponseDto;
    }

    @Override
    public TargRepAllele toEntity(TargRepAlleleResponseDTO targRepAlleleResponseDto) {
        TargRepAllele targRepAllele = new TargRepAllele();
        targRepAllele.setId(targRepAlleleResponseDto.getId());
        targRepAllele.setAssembly(targRepAlleleResponseDto.getAssembly());
        targRepAllele.setChromosome(targRepAlleleResponseDto.getChromosome());
        targRepAllele.setProjectDesignId(targRepAlleleResponseDto.getProjectDesignId());
        targRepAllele.setSubtypeDescription(targRepAlleleResponseDto.getSubtypeDescription());
        targRepAllele.setHomologyArmStart(targRepAlleleResponseDto.getHomologyArmStart());
        targRepAllele.setHomologyArmEnd(targRepAlleleResponseDto.getHomologyArmEnd());
        targRepAllele.setCassetteStart(targRepAlleleResponseDto.getCassetteStart());
        targRepAllele.setCassetteEnd(targRepAlleleResponseDto.getCassetteEnd());
        targRepAllele.setLoxpStart(targRepAlleleResponseDto.getLoxpStart());
        targRepAllele.setLoxpEnd(targRepAlleleResponseDto.getLoxpEnd());
        targRepAllele.setCassette(targRepAlleleResponseDto.getCassette());
        targRepAllele.setCassetteType(targRepAlleleResponseDto.getCassetteType());
        targRepAllele.setBackbone(targRepAlleleResponseDto.getBackbone());
        targRepAllele.setFloxedStartExon(targRepAlleleResponseDto.getFloxedStartExon());
        targRepAllele.setFloxedEndExon(targRepAlleleResponseDto.getFloxedEndExon());

        setTargRepMutationTypeToEntity(targRepAllele, targRepAlleleResponseDto);
        setTargRepMutationSubtypeToEntity(targRepAllele, targRepAlleleResponseDto);

        return targRepAllele;
    }

    private void setTargRepMutationTypeDto(
        TargRepAlleleResponseDTO targRepAlleleResponseDto, TargRepAllele targRepAllele) {
        TargRepMutationTypeDTO targRepMutationTypeDto =
            targRepMutationTypeMapper.toDto(targRepAllele.getMutationType());
        targRepAlleleResponseDto.setMutationType(targRepMutationTypeDto);
    }

    private void setTargRepMutationSubtypeDto(
        TargRepAlleleResponseDTO targRepAlleleResponseDto, TargRepAllele targRepAllele) {
        TargRepMutationSubtypeDTO targRepMutationSubtypeDto =
            targRepMutationSubtypeMapper.toDto(targRepAllele.getMutationSubtype());
        targRepAlleleResponseDto.setMutationSubtype(targRepMutationSubtypeDto);
    }

    private void setTargRepMutationTypeToEntity(TargRepAllele targRepAllele,
                                                TargRepAlleleResponseDTO targRepAlleleResponseDto) {
        targRepAllele.setMutationType(
            targRepMutationTypeMapper.toEntity(targRepAlleleResponseDto.getMutationType()));
    }

    private void setTargRepMutationSubtypeToEntity(
        TargRepAllele targRepAllele,
        TargRepAlleleResponseDTO targRepAlleleResponseDto) {
        targRepAllele.setMutationSubtype(
            targRepMutationSubtypeMapper.toEntity(targRepAlleleResponseDto.getMutationSubtype()));
    }

    private void addSelfLink(TargRepAlleleResponseDTO targRepAlleleResponseDto,
                             TargRepAllele targRepAllele) {
        Link link = linkTo(
            methodOn(TargRepAlleleController.class).findTargRepAlleleById(targRepAllele.getId()))
            .withSelfRel();
        link = link.withHref(decode(link.getHref()));
        targRepAlleleResponseDto.add(link);
    }

    private static String decode(String value) {
        try {
            return URLDecoder.decode(value, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return value;
    }
}
