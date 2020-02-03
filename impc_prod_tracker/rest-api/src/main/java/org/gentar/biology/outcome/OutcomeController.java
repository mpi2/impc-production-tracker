package org.gentar.biology.outcome;

import org.gentar.helpers.LinkUtil;
import org.gentar.helpers.PaginationHelper;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequestMapping("/api/outcomes")
@CrossOrigin(origins="*")
public class OutcomeController
{
    private OutcomeServiceImpl outcomeService;
    private OutcomeMapper outcomeMapper;

    public OutcomeController(OutcomeServiceImpl outcomeService, OutcomeMapper outcomeMapper)
    {
        this.outcomeService = outcomeService;
        this.outcomeMapper = outcomeMapper;
    }

    @GetMapping
    public ResponseEntity<OutcomeDTO> findAll(Pageable pageable, PagedResourcesAssembler assembler)
    {
        List<Outcome> outcomes = outcomeService.findAll();

        Page<Outcome> paginatedContent = PaginationHelper.createPage(outcomes, pageable);
        Page<OutcomeDTO> outcomeDTOPage = paginatedContent.map(x -> outcomeMapper.toDto(x));

        PagedModel<OutcomeDTO> pr =
            assembler.toModel(
                outcomeDTOPage,
                linkTo(OutcomeController.class).withSelfRel());

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Link", LinkUtil.createLinkHeader(pr));

        return new ResponseEntity(pr, responseHeaders, HttpStatus.OK);
    }

    /**
     * Create an outcome in the system.
     * @param outcomeDTO DTO with the representation of the outcome.
     * @return The DTO with the outcome of the created outcome.
     */
    @PostMapping
    private OutcomeDTO create(@RequestBody OutcomeDTO outcomeDTO)
    {
        Outcome outcome = outcomeMapper.toEntity(outcomeDTO);
        Outcome createdOutcome = outcomeService.create(outcome);
        return outcomeMapper.toDto(createdOutcome);
    }

    @PutMapping(value = {"/{tpo}"})
    public void update(@PathVariable String tpo, @RequestBody OutcomeDTO outcomeDTO)
    {
        System.out.println(tpo);
        System.out.println(outcomeDTO);
        Outcome outcome = outcomeMapper.toEntity(outcomeDTO);
        outcomeService.update(outcome);
    }

}
