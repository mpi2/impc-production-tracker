package org.gentar.organization.work_unit;

import org.gentar.exceptions.UserOperationFailedException;
import org.gentar.organization.work_group.WorkGroup;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Set;

@Component
public class WorkUnitServiceImpl implements WorkUnitService
{
    private static final String WORK_UNIT_NOT_EXISTS_ERROR = "Work unit %s does not exist.";

    private WorkUnitRepository workUnitRepository;
    @PersistenceContext
    private EntityManager entityManager;

    public WorkUnitServiceImpl(WorkUnitRepository workUnitRepository)
    {
        this.workUnitRepository = workUnitRepository;
    }

    public List<WorkUnit> getAllWorkUnits()
    {
        return workUnitRepository.findAll();
    }

    public Set<WorkGroup> getWorkGroupsByWorkUnitName(String name)
    {
        WorkUnit workUnit = workUnitRepository.findWorkUnitByNameIgnoreCase(name);
        if (workUnit != null)
        {
            return workUnit.getWorkGroups();

        }
        return null;
    }


    public WorkUnit getWorkUnitByName(String name)
    {
        return workUnitRepository.findWorkUnitByNameIgnoreCase(name);
    }

    public WorkUnit getWorkUnitByNameOrThrowException(String name)
    {
        WorkUnit workUnit = getWorkUnitByName(name);
        if (workUnit == null)
        {
            throw new UserOperationFailedException(
                    String.format(WORK_UNIT_NOT_EXISTS_ERROR, name));
        }
        return workUnit;
    }

    @Transactional
    public WorkUnit getWorkUnitWithConsortia(Long id)
    {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<WorkUnit> query = cb.createQuery(WorkUnit.class);
        Root<WorkUnit> root = query.from(WorkUnit.class);
        query.select(root);
        root.fetch(WorkUnit_.consortia);
        query.where(cb.equal(root.get("id"), id));
        WorkUnit workUnit = null;
        try
        {
            workUnit = entityManager.createQuery(query).getSingleResult();
        } catch (NoResultException nre)
        {

        }

        return workUnit;
    }
}
