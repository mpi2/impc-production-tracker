package org.gentar.biology.plan.attempt.crispr.guide;

import org.springframework.data.repository.CrudRepository;

public interface GuideFormatRepository extends CrudRepository<GuideFormat, Long>
{
    GuideFormat findByNameIgnoreCase(String name);
}
