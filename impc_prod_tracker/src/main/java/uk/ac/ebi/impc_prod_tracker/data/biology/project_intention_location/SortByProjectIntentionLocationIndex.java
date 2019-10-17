package uk.ac.ebi.impc_prod_tracker.data.biology.project_intention_location;

import java.util.Comparator;

public class SortByProjectIntentionLocationIndex implements Comparator<ProjectIntentionLocation>
{
    public int compare(ProjectIntentionLocation a, ProjectIntentionLocation b)
    {
        return a.getIndex() - b.getIndex() ;
    }
}