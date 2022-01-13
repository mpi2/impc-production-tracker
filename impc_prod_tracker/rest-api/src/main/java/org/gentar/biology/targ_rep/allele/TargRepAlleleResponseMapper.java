package org.gentar.biology.targ_rep.allele;

import org.gentar.Mapper;
import org.gentar.biology.targ_rep.TargRepAlleleResponseDTO;
import org.gentar.biology.targ_rep.TargRepController;
import org.gentar.biology.targ_rep.TargRepMutationSubtypeDTO;
import org.gentar.biology.targ_rep.TargRepMutationTypeDTO;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class TargRepAlleleResponseMapper implements Mapper<TargRepAllele, TargRepAlleleResponseDTO> {

    private final TargRepMutationTypeMapper targRepMutationTypeMapper;
    private final TargRepMutationSubtypeMapper targRepMutationSubtypeMapper;

    public TargRepAlleleResponseMapper(TargRepMutationTypeMapper targRepMutationTypeMapper,
                                       TargRepMutationSubtypeMapper targRepMutationSubtypeMapper)
    {
        this.targRepMutationTypeMapper = targRepMutationTypeMapper;
        this.targRepMutationSubtypeMapper = targRepMutationSubtypeMapper;
    }

    @Override
    public TargRepAlleleResponseDTO toDto(TargRepAllele targRepAllele){

        TargRepAlleleResponseDTO targRepAlleleResponseDTO = new TargRepAlleleResponseDTO();

        targRepAlleleResponseDTO.setId(targRepAllele.getId());
        targRepAlleleResponseDTO.setAssembly(targRepAllele.getAssembly());
        targRepAlleleResponseDTO.setChromosome(targRepAllele.getChromosome());
        targRepAlleleResponseDTO.setProjectDesignId(targRepAllele.getProjectDesignId());
        targRepAlleleResponseDTO.setSubtypeDescription(targRepAllele.getSubtypeDescription());
        targRepAlleleResponseDTO.setHomologyArmStart(targRepAllele.getHomologyArmStart());
        targRepAlleleResponseDTO.setHomologyArmEnd(targRepAllele.getHomologyArmEnd());
        targRepAlleleResponseDTO.setCassetteStart(targRepAllele.getCassetteStart());
        targRepAlleleResponseDTO.setCassetteEnd(targRepAllele.getCassetteEnd());
        targRepAlleleResponseDTO.setLoxpStart(targRepAllele.getLoxpStart());
        targRepAlleleResponseDTO.setLoxpEnd(targRepAllele.getLoxpEnd());
        targRepAlleleResponseDTO.setCassette(targRepAllele.getCassette());
        targRepAlleleResponseDTO.setCassetteType(targRepAllele.getCassetteType());
        targRepAlleleResponseDTO.setBackbone(targRepAllele.getBackbone());
        targRepAlleleResponseDTO.setFloxedStartExon(targRepAllele.getFloxedStartExon());
        targRepAlleleResponseDTO.setFloxedEndExon(targRepAllele.getFloxedEndExon());

        setTargRepMutationTypeDTO(targRepAlleleResponseDTO, targRepAllele);
        setTargRepMutationSubtypeDTO(targRepAlleleResponseDTO, targRepAllele);

        addSelfLink(targRepAlleleResponseDTO, targRepAllele);

        return targRepAlleleResponseDTO;
    }

    @Override
    public TargRepAllele toEntity(TargRepAlleleResponseDTO targRepAlleleResponseDTO)
    {
        TargRepAllele targRepAllele = new TargRepAllele();
        targRepAllele.setId(targRepAlleleResponseDTO.getId());
        targRepAllele.setAssembly(targRepAlleleResponseDTO.getAssembly());
        targRepAllele.setChromosome(targRepAlleleResponseDTO.getChromosome());
        targRepAllele.setProjectDesignId(targRepAlleleResponseDTO.getProjectDesignId());
        targRepAllele.setSubtypeDescription(targRepAlleleResponseDTO.getSubtypeDescription());
        targRepAllele.setHomologyArmStart(targRepAlleleResponseDTO.getHomologyArmStart());
        targRepAllele.setHomologyArmEnd(targRepAlleleResponseDTO.getHomologyArmEnd());
        targRepAllele.setCassetteStart(targRepAlleleResponseDTO.getCassetteStart());
        targRepAllele.setCassetteEnd(targRepAlleleResponseDTO.getCassetteEnd());
        targRepAllele.setLoxpStart(targRepAlleleResponseDTO.getLoxpStart());
        targRepAllele.setLoxpEnd(targRepAlleleResponseDTO.getLoxpEnd());
        targRepAllele.setCassette(targRepAlleleResponseDTO.getCassette());
        targRepAllele.setCassetteType(targRepAlleleResponseDTO.getCassetteType());
        targRepAllele.setBackbone(targRepAlleleResponseDTO.getBackbone());
        targRepAllele.setFloxedStartExon(targRepAlleleResponseDTO.getFloxedStartExon());
        targRepAllele.setFloxedEndExon(targRepAlleleResponseDTO.getFloxedEndExon());

        setTargRepMutationTypeToEntity(targRepAllele, targRepAlleleResponseDTO);
        setTargRepMutationSubtypeToEntity(targRepAllele, targRepAlleleResponseDTO);

        return targRepAllele;
    }

    private void setTargRepMutationTypeDTO(
            TargRepAlleleResponseDTO targRepAlleleResponseDTO, TargRepAllele targRepAllele)
    {
        TargRepMutationTypeDTO targRepMutationTypeDTO =
                targRepMutationTypeMapper.toDto(targRepAllele.getMutationType());
        targRepAlleleResponseDTO.setMutationType(targRepMutationTypeDTO);
    }

    private void setTargRepMutationSubtypeDTO(
            TargRepAlleleResponseDTO targRepAlleleResponseDTO, TargRepAllele targRepAllele)
    {
        TargRepMutationSubtypeDTO targRepMutationSubtypeDTO =
                targRepMutationSubtypeMapper.toDto(targRepAllele.getMutationSubtype());
        targRepAlleleResponseDTO.setMutationSubtype(targRepMutationSubtypeDTO);
    }

    private void setTargRepMutationTypeToEntity(TargRepAllele targRepAllele,
                                                TargRepAlleleResponseDTO targRepAlleleResponseDTO)
    {
        targRepAllele.setMutationType(
                targRepMutationTypeMapper.toEntity(targRepAlleleResponseDTO.getMutationType()));
    }

    private void setTargRepMutationSubtypeToEntity(TargRepAllele targRepAllele,
                                                   TargRepAlleleResponseDTO targRepAlleleResponseDTO)
    {
        targRepAllele.setMutationSubtype(
                targRepMutationSubtypeMapper.toEntity(targRepAlleleResponseDTO.getMutationSubtype()));
    }


    private void addSelfLink(TargRepAlleleResponseDTO targRepAlleleResponseDTO, TargRepAllele targRepAllele)
    {
        Link link = linkTo(methodOn(TargRepController.class).findTargRepAlleleById(targRepAllele.getId())).withSelfRel();
        targRepAlleleResponseDTO.add(link);
    }

}
