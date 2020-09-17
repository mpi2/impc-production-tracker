package org.gentar.organization.funder;

import java.util.List;
import java.util.Map;

public interface FunderService
{
    Funder getFunderByName(String name);

    Map<String, List<String>> getFunderNamesByWorkGroupNamesMap();

    List<Funder> getAll();
}
