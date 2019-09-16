package uk.ac.ebi.impc_prod_tracker.data.biology.project;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ProjectRepository extends PagingAndSortingRepository<Project, Long>,
        JpaSpecificationExecutor<Project>
{
}
