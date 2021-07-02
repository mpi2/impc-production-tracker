package org.gentar.biology.project.esCellQc;

import org.springframework.stereotype.Component;

@Component
public class EsCellQcCommentServiceImpl implements EsCellQcCommentService {

    private final EsCellQcCommentRepository esCellQcCommentRepository;

    public EsCellQcCommentServiceImpl(EsCellQcCommentRepository esCellQcCommentRepository) {
        this.esCellQcCommentRepository = esCellQcCommentRepository;
    }

    @Override
    public EsCellQcComment getEsCellQcCommentByName(String name) {
        return esCellQcCommentRepository.findFirstByNameIgnoreCase(name);
    }
}
