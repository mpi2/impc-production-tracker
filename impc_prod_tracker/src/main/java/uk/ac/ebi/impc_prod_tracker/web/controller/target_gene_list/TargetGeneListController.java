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
package uk.ac.ebi.impc_prod_tracker.web.controller.target_gene_list;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.ac.ebi.impc_prod_tracker.common.pagination.PaginationHelper;
import uk.ac.ebi.impc_prod_tracker.data.biology.target_gene_list.ConsortiumList;
import uk.ac.ebi.impc_prod_tracker.service.biology.target_gene_list.TargetGeneListService;
import uk.ac.ebi.impc_prod_tracker.web.controller.util.LinkUtil;
import uk.ac.ebi.impc_prod_tracker.web.dto.target_gene_list.TargetListsByConsortiumDTO;
import uk.ac.ebi.impc_prod_tracker.web.mapping.target_gene_list.TargetListsByConsortiumMapper;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequestMapping("/api/targetGeneList")
@CrossOrigin(origins = "*")
public class TargetGeneListController
{
    private TargetGeneListService targetGeneListService;
    private TargetListsByConsortiumMapper targetListsByConsortiumMapper;

    public TargetGeneListController(
        TargetGeneListService targetGeneListService,
        TargetListsByConsortiumMapper targetListsByConsortiumMapper)
    {
        this.targetGeneListService = targetGeneListService;
        this.targetListsByConsortiumMapper = targetListsByConsortiumMapper;
    }

    /**
     * Get all the target gene lists the user has access to.
     * @param pageable Pagination information.
     * @param assembler Allows to manage hal.
     * @return Lists by consortium.
     */
    @GetMapping
    public ResponseEntity findAll(Pageable pageable, PagedResourcesAssembler assembler)
    {
        Page<ConsortiumList> consortiumListPage = targetGeneListService.getAll(pageable);
        return buildResponseEntity(pageable, assembler, consortiumListPage);
    }

    /**
     * Get all the target gene lists the user has access to.
     * @param pageable Pagination information.
     * @param assembler Allows to manage hal.
     * @return Lists by consortium.
     */

    /**
     * Get target gene lists by consortium.
     * @param pageable Pagination information.
     * @param assembler Allows to manage hal.
     * @param consortiumName Name of the consortium.
     * @return Lists by consortium.
     */
    @GetMapping(value = {"/{consortiumName}"})
    public ResponseEntity findByConsortium(
        Pageable pageable,
        PagedResourcesAssembler assembler,
        @PathVariable("consortiumName") String consortiumName)
    {
        Page<ConsortiumList> consortiumListPage =
            targetGeneListService.getByConsortium(pageable, consortiumName);
        return buildResponseEntity(pageable, assembler, consortiumListPage);
    }

    private ResponseEntity buildResponseEntity(
        Pageable pageable,
        PagedResourcesAssembler assembler,
        Page<ConsortiumList> consortiumListPage)
    {
        List<TargetListsByConsortiumDTO> targetListsByConsortiumDTOS =
            targetListsByConsortiumMapper.consortiumListsToDtos(consortiumListPage.getContent());
        Page<TargetListsByConsortiumDTO> targetListsByConsortiumDTOSPage =
            PaginationHelper.createPage(targetListsByConsortiumDTOS, pageable);
        PagedModel pr =
            assembler.toModel(
                targetListsByConsortiumDTOSPage,
                linkTo(TargetGeneListController.class).withSelfRel());
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Link", LinkUtil.createLinkHeader(pr));

        return new ResponseEntity<>(pr, responseHeaders, HttpStatus.OK);
    }
}
