package org.gentar.biology.project;

import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

@Primary
public interface ProjectRepository extends
    PagingAndSortingRepository<Project, Long>, JpaSpecificationExecutor<Project>
{
    @Query("SELECT max(p.tpn) FROM Project p")
    String getMaxTpn();

    Project findByTpn(String tpn);
}
