package org.gentar.organization.work_group;

import org.gentar.organization.funder.Funder;

import java.util.Set;

public interface WorkGroupService {
    Set<Funder> getFundersByWorkGroupName(String name);

    WorkGroup getWorkGroupByName(String name);
}
