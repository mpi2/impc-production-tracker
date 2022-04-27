package org.gentar.biology.targ_rep.mutation;

import java.util.List;
import org.gentar.biology.targ_rep.allele.TargRepAllele;
import org.gentar.biology.targ_rep.es_cell.TargRepEsCell;
import org.gentar.biology.targ_rep.es_cell.TargRepEsCellRepository;
import org.gentar.biology.targ_rep.es_cell.TargRepEsCellService;
import org.gentar.exceptions.NotFoundException;
import org.gentar.exceptions.UserOperationFailedException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;


/**
 * TargRepEsCellServiceImpl.
 */
@Component
public class TargRepEsCellMutationServiceImpl implements TargRepEsCellMutationService {

    private final TargRepEsCellMutationRepository mutationRepository;

    public TargRepEsCellMutationServiceImpl(TargRepEsCellMutationRepository mutationRepository) {
        this.mutationRepository = mutationRepository;
    }

    @Override
    public TargRepEsCellMutation getTargRepEsCellMutationByEsCellId(Long esCellId) {
        return mutationRepository.findByEsCellId(esCellId);
    }
}
