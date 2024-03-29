package org.gentar.util;

import jakarta.persistence.criteria.CriteriaBuilder;

import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Path;
import java.util.ArrayList;
import java.util.List;

public class PredicateBuilder
{
    public static Predicate addInPredicates(
        CriteriaBuilder criteriaBuilder, Path<String> path, List<String> values)
    {
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(path.in(values));
        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }

    public static Predicate addLowerLikeOrPredicates(
        CriteriaBuilder criteriaBuilder, Path<String> path, List<String> values)
    {
        List<Predicate> predicates = new ArrayList<>();
        values.forEach(x -> predicates.add(buildLoweLikePredicate(criteriaBuilder, path, x)));
        return criteriaBuilder.or(predicates.toArray(new Predicate[0]));
    }

    private static Predicate buildLoweLikePredicate(
        CriteriaBuilder criteriaBuilder, Path<String> path, String value)
    {
        return criteriaBuilder.like(criteriaBuilder.lower(path), value.toLowerCase());
    }

    public static Predicate addLowerLikeOrPredicatesId(
            CriteriaBuilder criteriaBuilder, Path<Long> path, List<String> values)
    {
        List<Predicate> predicates = new ArrayList<>();
        values.forEach(x -> predicates.add(buildLoweLikePredicateId(criteriaBuilder, path, x)));
        return criteriaBuilder.or(predicates.toArray(new Predicate[0]));
    }

    private static Predicate buildLoweLikePredicateId(
            CriteriaBuilder criteriaBuilder, Path<Long> path, String value)
    {
        return criteriaBuilder.equal(path, Long.parseLong(value));
    }

    public static Predicate notInPredicates(
        CriteriaBuilder criteriaBuilder, Path<String> path)
    {
        return criteriaBuilder.isNotNull(path);
    }
}
