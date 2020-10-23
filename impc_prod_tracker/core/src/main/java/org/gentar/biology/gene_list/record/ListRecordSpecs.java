package org.gentar.biology.gene_list.record;

import org.gentar.biology.gene_list.GeneList;
import org.gentar.biology.gene_list.GeneList_;
import org.gentar.organization.consortium.Consortium;
import org.gentar.organization.consortium.Consortium_;
import org.springframework.data.jpa.domain.Specification;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.SetJoin;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.criteria.Predicate;

public class ListRecordSpecs
{
    public static Specification<ListRecord> withConsortiumName(String consortiumName)
    {
        return (Specification<ListRecord>) (root, query, criteriaBuilder) -> {
            if (consortiumName == null)
            {
                return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            }

            List<Predicate> predicates = new ArrayList<>();
            Path<GeneList> geneListPath = root.join(ListRecord_.geneList);
            Path<Consortium> consortiumPath = geneListPath.get(GeneList_.consortium);

            Path<String> consortiumNamePath = consortiumPath.get(Consortium_.name);

            predicates.add(consortiumNamePath.in(consortiumName));

            query.distinct(true);
            return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }

    public static Specification<ListRecord> withAccIds(List<String> accIds)
    {
        return (Specification<ListRecord>) (root, query, criteriaBuilder) -> {
            if (accIds == null || accIds.isEmpty())
            {
                return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            }

            List<Predicate> predicates = new ArrayList<>();
            SetJoin<ListRecord, GeneByListRecord> recordGeneByRecordJoin =
                root.join(ListRecord_.genesByRecord);

            Path<String> accId = recordGeneByRecordJoin.get(GeneByListRecord_.accId);

            predicates.add(accId.in(accIds));

            query.distinct(true);
            return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }
}
