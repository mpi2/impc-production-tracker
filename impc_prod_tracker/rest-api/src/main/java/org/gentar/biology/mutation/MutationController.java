package org.gentar.biology.mutation;

import org.gentar.biology.outcome.OutcomeService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins="*")
public class MutationController
{
    private MutationMapper mutationMapper;
    private OutcomeService outcomeService;
    private MutationResponseMapper mutationResponseMapper;
    private MutationService mutationService;

    public MutationController(
        MutationMapper mutationMapper,
        OutcomeService outcomeService,
        MutationResponseMapper mutationResponseMapper,
        MutationService mutationService)
    {
        this.mutationMapper = mutationMapper;
        this.outcomeService = outcomeService;
        this.mutationResponseMapper = mutationResponseMapper;
        this.mutationService = mutationService;
    }

    /**
     * Gets a mutation in an outcome.
     * @param pin Public identifier of the plan.
     * @param tpo Public identifier of the outcome.
     * @param min Public identifier of the mutation.
     * @return Mutation DTO
     */
    @GetMapping(value = {"plans/{pin}/outcomes/{tpo}/mutations/{min}"})
    public MutationResponseDTO findMutationInOutcomeById(
        @PathVariable String pin, @PathVariable String tpo, @PathVariable String min)
    {
        Mutation mutation = outcomeService.getMutationByPinTpoAndMin(pin, tpo, min);
        return mutationResponseMapper.toDto(mutation);
    }
}
