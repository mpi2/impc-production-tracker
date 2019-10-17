package uk.ac.ebi.impc_prod_tracker.data.biology.project_intention_sequence;


import java.util.Comparator;

public class SortByProjectIntentionSequenceIndex implements Comparator<ProjectIntentionSequence>
{
    public int compare(ProjectIntentionSequence a, ProjectIntentionSequence b)
    {
        return a.getIndex() - b.getIndex() ;
    }
}
