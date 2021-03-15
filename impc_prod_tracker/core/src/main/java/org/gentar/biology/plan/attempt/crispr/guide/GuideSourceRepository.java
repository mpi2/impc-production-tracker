package org.gentar.biology.plan.attempt.crispr.guide;

import org.springframework.data.repository.CrudRepository;

public interface GuideSourceRepository extends CrudRepository<GuideSource, Long>
{
    GuideSource findByNameIgnoreCase(String name);
}
