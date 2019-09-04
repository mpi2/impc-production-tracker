package uk.ac.ebi.impc_prod_tracker.data.biology.project;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import java.util.List;

public interface ProjectRepository extends PagingAndSortingRepository<Project, Long>,
        JpaSpecificationExecutor<Project>
{
    List<Project> findAll();

    Project findProjectByTpn(String tpn);

}
