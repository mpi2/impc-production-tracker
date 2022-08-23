package org.gentar.biology.targ_rep.genbank_file;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import org.gentar.Mapper;
import org.gentar.biology.targ_rep.TargRepGenBankFileResponseDTO;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

/**
 * TargRepAlleleResponseMapper.
 */
@Component
public class TargRepGenBankFileResponseMapper
    implements Mapper<TargRepGenbankFile, TargRepGenBankFileResponseDTO> {


    @Override
    public TargRepGenBankFileResponseDTO toDto(TargRepGenbankFile entity) {
        TargRepGenBankFileResponseDTO genBankFileDTO = new TargRepGenBankFileResponseDTO();
        if (entity != null) {
            genBankFileDTO.setId(entity.getId());
            genBankFileDTO.setGenbankFilePath(entity.getGenbankFilePath());
            genBankFileDTO.setCassetteImagePath(entity.getCassetteImagePath());
            genBankFileDTO.setAlleleImagePath(entity.getAlleleImagePath());
            genBankFileDTO.setSimpleImagePath(entity.getSimpleImagePath());
            genBankFileDTO.setVectorImagePath(entity.getVectorImagePath());
            genBankFileDTO.setHost(entity.getHost());
            genBankFileDTO.setType(entity.getType());

            addSelfLink(genBankFileDTO, entity);

        }
        return genBankFileDTO;
    }

    @Override
    public TargRepGenbankFile toEntity(TargRepGenBankFileResponseDTO dto) {
        return Mapper.super.toEntity(dto);
    }

    private void addSelfLink(TargRepGenBankFileResponseDTO genBankFileResponseDTO,
                             TargRepGenbankFile targRepGenBankFile) {
        Link link = linkTo(methodOn(TargRepGenBankFileController.class)
            .findTargRepGenBankFileById(targRepGenBankFile.getId())).withSelfRel();
        link = link.withHref(decode(link.getHref()));
        genBankFileResponseDTO.add(link);
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
