package org.gentar.mockdata;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.persistence.criteria.CollectionJoin;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Fetch;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.ListJoin;
import javax.persistence.criteria.MapJoin;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;
import javax.persistence.criteria.SetJoin;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.MapAttribute;
import javax.persistence.metamodel.PluralAttribute;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import org.gentar.audit.history.History;
import org.gentar.biology.colony.Colony;
import org.gentar.biology.gene.Gene;
import org.gentar.biology.gene_list.GeneList;
import org.gentar.biology.gene_list.record.GeneByListRecord;
import org.gentar.biology.gene_list.record.GeneListProjection;
import org.gentar.biology.gene_list.record.ListRecord;
import org.gentar.biology.gene_list.record.ListRecordType;
import org.gentar.biology.intention.project_intention.ProjectIntention;
import org.gentar.biology.intention.project_intention_gene.ProjectIntentionGene;
import org.gentar.biology.mutation.Mutation;
import org.gentar.biology.mutation.molecular_type.MolecularMutationType;
import org.gentar.biology.outcome.Outcome;
import org.gentar.biology.outcome.type.OutcomeType;
import org.gentar.biology.plan.Plan;
import org.gentar.biology.plan.attempt.AttemptType;
import org.gentar.biology.plan.attempt.AttemptTypeChecker;
import org.gentar.biology.plan.attempt.phenotyping.PhenotypingAttempt;
import org.gentar.biology.plan.attempt.phenotyping.stage.PhenotypingStage;
import org.gentar.biology.plan.attempt.phenotyping.stage.type.PhenotypingStageType;
import org.gentar.biology.plan.attempt.phenotyping.stage.type.PhenotypingStageTypeName;
import org.gentar.biology.plan.starting_point.PlanStartingPoint;
import org.gentar.biology.plan.type.PlanType;
import org.gentar.biology.plan.type.PlanTypeName;
import org.gentar.biology.project.Project;
import org.gentar.biology.project.assignment.AssignmentStatus;
import org.gentar.biology.project.assignment.AssignmentStatusStamp;
import org.gentar.biology.project.completionNote.ProjectCompletionNote;
import org.gentar.biology.project.consortium.ProjectConsortium;
import org.gentar.biology.project.esCellQc.ProjectEsCellQc;
import org.gentar.biology.project.privacy.Privacy;
import org.gentar.biology.project.projection.ProjectSearchDownloadProjection;
import org.gentar.biology.project.summary_status.ProjectSummaryStatusStamp;
import org.gentar.biology.project.type.ProjectType;
import org.gentar.biology.species.Species;
import org.gentar.biology.status.Status;
import org.gentar.organization.consortium.Consortium;
import org.gentar.organization.funder.Funder;
import org.gentar.organization.work_group.WorkGroup;
import org.gentar.organization.work_unit.WorkUnit;
import org.gentar.report.collection.gene_interest.gene.GeneInterestReportGeneProjection;
import org.gentar.report.collection.gene_interest.mutation.GeneInterestReportMutationGeneProjection;
import org.gentar.report.collection.gene_interest.outcome.GeneInterestReportOutcomeMutationProjection;
import org.gentar.report.collection.gene_interest.phenotyping_attempt.GeneInterestReportPhenotypingAttemptProjection;
import org.gentar.report.collection.gene_interest.project.GeneInterestReportProjectProjection;
import org.gentar.statemachine.ProcessData;
import org.gentar.statemachine.ProcessEvent;
import org.gentar.statemachine.ProcessState;
import org.gentar.statemachine.Processor;
import org.gentar.statemachine.TransitionEvaluation;

public class MockData {


    public static final String TPN_000000001 = "TPN:000000001";
    public static final String TPO_000000001 = "TPO:000000001";
    public static final String PIN_000000001 = "PIN:000000001";
    public static final String MIN_000000001 = "MIN:000000001";
    public static final String PSN_000000001 = "PSN:000000000001";
    public static final String TEST_COMMENT = "Test Comment";
    public static final String COMPLETION_COMMENT = "Completion Comment";
    public static final String TEST_NAME = "Test Name";
    public static final String TEST_DESCRIPTION = "Test Description";
    public static final String TEST_USER = "Test User";
    public static final String MGI_00000001 = "MGI:00000001";
    public static final String TEST_WORK_UNIT_NAME = "Test Work Unit Name";
    public static final String TEST_WORK_GROUP_NAME = "Test Work Group Name";
    public static final String TEST_OUTCOME_TYPE_NAME = "Test OutcomeType Name";
    public static final String MOLECULAR_MUTATION_TYPE_NAME = "Molecular Mutation Type Name";
    public static final String TEST_SUMMARY_STATUS = "Test Summary Status";
    public static final String TEST_ASSIGMENT_NAME = "Test Assigment Name";
    public static final String TEST_IDENTIFICATION_NUMBER = "Test Identification Number";

    public static Project projectMockData() {
        Project project = new Project();
        project.setId(1L);
        project.setTpn(TPN_000000001);
        project.setImitsMiPlan(1L);
        project.setSummaryStatus(statusMockData());
        project.setAssignmentStatusStamps(assignmentStatusStampSetMockData());
        project.setSummaryStatusStamps(projectSummaryStatusStampSetMockData());
        project.setPlans(planSetMockData());
        project.setReactivationDate(LocalDateTime.now());
        project.setRecovery(true);
        project.setEsCellQcOnly(true);
        project.setComment(TEST_COMMENT);
        project.setPrivacy(privacyMockData());
        project.setProjectIntentions(projectIntentionListMockData());
        project.setSpecies(speciesSetMockData());
        project.setProjectConsortia(projectConsortiumSetMockData());
        project.setProjectEsCellQc(projectEsCellQcMockData());
        project.setProjectType(projectTypeMockData());
        project.setCompletionNote(ProjectCompletionNoteMockData());
        project.setCompletionComment(COMPLETION_COMMENT);

        return project;
    }

    public static Status statusMockData() {
        Status status = new Status();
        status.setId(1L);
        status.setName(TEST_NAME);
        status.setDescription(TEST_DESCRIPTION);
        status.setOrdering(1);
        status.setIsAbortionStatus(false);
        return status;
    }

    public static Set<AssignmentStatusStamp> assignmentStatusStampSetMockData() {
        Set<AssignmentStatusStamp> assignmentStatusStamps = new HashSet<>();
        AssignmentStatusStamp assignmentStatusStamp = new AssignmentStatusStamp();
        assignmentStatusStamp.setId(1L);
        assignmentStatusStamp.setAssignmentStatus(assignmentStatusMockData());
        assignmentStatusStamp.setDate(LocalDateTime.now());
        assignmentStatusStamps.add(assignmentStatusStamp);
        return assignmentStatusStamps;
    }

    public static Set<ProjectSummaryStatusStamp> projectSummaryStatusStampSetMockData() {
        Set<ProjectSummaryStatusStamp> projectSummaryStatusStamps = new HashSet<>();
        ProjectSummaryStatusStamp projectSummaryStatusStamp = new ProjectSummaryStatusStamp();
        projectSummaryStatusStamp.setId(1L);
        projectSummaryStatusStamp.setStatus(statusMockData());
        projectSummaryStatusStamp.setDate(LocalDateTime.now());
        projectSummaryStatusStamps.add(projectSummaryStatusStamp);
        return projectSummaryStatusStamps;
    }

    public static Set<Plan> planSetMockData() {
        Set<Plan> plans = new HashSet<>();
        plans.add(planMockData());
        return plans;
    }


    public static Plan planMockData() {
        Plan plan = new Plan();
        plan.setId(1L);
        plan.setPin(PIN_000000001);
        plan.setPlanType(planTypeMockData());
        plan.setAttemptType(attemptTypeMockData());
        plan.setOutcomes(outcomesSetMockData());
        plan.setWorkUnit(workUnitMockData());
        plan.setWorkGroup(workGroupMockData());
        plan.setPlanStartingPoints(Set.of(planStartingPointMockData()));
        plan.setFunders(Set.of(funderMockData()));
        return plan;
    }

    public static Privacy privacyMockData() {
        Privacy privacy = new Privacy();
        privacy.setId(1L);
        privacy.setName(TEST_NAME);
        return privacy;
    }

    public static List<ProjectIntention> projectIntentionListMockData() {
        List<ProjectIntention> projectIntentions = new ArrayList<>();
        projectIntentions.add(projectIntentionMockData());
        return projectIntentions;
    }

    public static ProjectIntention projectIntentionMockData() {
        ProjectIntention projectIntention = new ProjectIntention();
        projectIntention.setId(1L);
        projectIntention.setProjectIntentionGene(projectIntentionGeneMockData());
        return projectIntention;
    }

    public static Set<Species> speciesSetMockData() {
        Set<Species> Species = new HashSet<>();
        return Species;
    }

    public static Set<ProjectConsortium> projectConsortiumSetMockData() {
        Set<ProjectConsortium> projectConsortiums = new HashSet<>();
        projectConsortiums.add(projectConsortiumMockData());
        return projectConsortiums;
    }

    public static ProjectConsortium projectConsortiumMockData() {
        ProjectConsortium projectConsortium = new ProjectConsortium();
        projectConsortium.setId(1L);
        projectConsortium.setConsortium(consortiumMockData());
        return projectConsortium;
    }

    public static ProjectEsCellQc projectEsCellQcMockData() {
        ProjectEsCellQc projectEsCellQc = new ProjectEsCellQc();
        return projectEsCellQc;
    }

    public static ProjectType projectTypeMockData() {
        ProjectType projectType = new ProjectType();
        return projectType;
    }

    public static ProjectCompletionNote ProjectCompletionNoteMockData() {
        ProjectCompletionNote projectCompletionNote = new ProjectCompletionNote();
        return projectCompletionNote;
    }

    public static AssignmentStatus assignmentStatusMockData() {
        AssignmentStatus AssignmentStatus = new AssignmentStatus();
        return AssignmentStatus;
    }


    public static History historyMockData() {
        History history = new History();
        history.setId(1L);
        history.setComment(TEST_COMMENT);
        history.setUser(TEST_USER);
        history.setDate(LocalDateTime.now());
        return history;
    }

    public static PlanType planTypeMockData() {
        PlanType planType = new PlanType();
        planType.setId(1L);
        planType.setName(PlanTypeName.PRODUCTION.getLabel());
        return planType;
    }

    public static AttemptType attemptTypeMockData() {
        AttemptType attemptType = new AttemptType();
        attemptType.setId(1L);
        attemptType.setName(AttemptTypeChecker.ES_CELL_TYPE);
        return attemptType;
    }

    public static Set<Outcome> outcomesSetMockData() {
        Set<Outcome> outcomes = new HashSet<>();
        outcomes.add(outcomeMockData());
        return outcomes;
    }

    public static Outcome outcomeMockData() {
        Outcome outcome = new Outcome();
        outcome.setId(1L);
        outcome.setColony(colonyMockData());
        outcome.setOutcomeType(outcomeTypeMockData());
        outcome.setTpo(TPO_000000001);
        return outcome;
    }

    public static List<ProjectIntentionGene> projectIntentionGeneListMockData() {
        List<ProjectIntentionGene> projectIntentionGenes = new ArrayList<>();
        projectIntentionGenes.add(projectIntentionGeneMockData());
        return projectIntentionGenes;
    }

    public static ProjectIntentionGene projectIntentionGeneMockData() {
        ProjectIntentionGene projectIntentionGene = new ProjectIntentionGene();
        projectIntentionGene.setId(1L);
        projectIntentionGene.setGene(geneMockData());
        return projectIntentionGene;
    }

    public static Set<Gene> geneSetMockData() {
        Set<Gene> geneList = new HashSet<>();
        geneList.add(geneMockData());
        return geneList;
    }

    public static Gene geneMockData() {
        Gene gene = new Gene();
        gene.setId(1L);
        gene.setAccId(MGI_00000001);
        gene.setSymbol("Rsph3b");
        return gene;
    }

    public static List<WorkUnit> workUnitListMockData() {
        List<WorkUnit> workUnitList = new ArrayList();
        workUnitList.add(workUnitMockData());
        return workUnitList;
    }

    public static WorkUnit workUnitMockData() {
        WorkUnit workUnit = new WorkUnit();
        workUnit.setId(1L);
        workUnit.setName(TEST_WORK_UNIT_NAME);
        return workUnit;
    }

    public static List<WorkGroup> workGroupListMockData() {
        List<WorkGroup> workGroupList = new ArrayList();
        workGroupList.add(workGroupMockData());
        return workGroupList;
    }

    public static WorkGroup workGroupMockData() {
        WorkGroup workGroup = new WorkGroup();
        workGroup.setId(1L);
        workGroup.setName(TEST_WORK_GROUP_NAME);
        return workGroup;
    }

    public static Consortium consortiumMockData() {
        Consortium consortium = new Consortium();
        consortium.setId(1L);
        consortium.setName(TEST_NAME);
        consortium.setDescription(TEST_DESCRIPTION);
        return consortium;
    }

    public static Colony colonyMockData() {
        Colony colony = new Colony();
        colony.setId(1L);
        colony.setName(TEST_NAME);
        return colony;
    }

    public static List<ProjectSearchDownloadProjection> projectSearchDownloadProjectionListMockData() {
        List<ProjectSearchDownloadProjection> projectSearchDownloadProjectionList =
            new ArrayList<>();
        projectSearchDownloadProjectionList.add(projectSearchDownloadProjectionMockData());
        return projectSearchDownloadProjectionList;
    }

    public static ProjectSearchDownloadProjection projectSearchDownloadProjectionMockData() {
        ProjectSearchDownloadProjection projectSearchDownloadProjection =
            new ProjectSearchDownloadProjection() {
                @Override
                public String getTpn() {
                    return TPN_000000001;
                }

                @Override
                public String getGeneOrLocation() {
                    return MGI_00000001;
                }

                @Override
                public String getMgi() {
                    return MGI_00000001;
                }

                @Override
                public String getMutationIntentions() {
                    return null;
                }

                @Override
                public String getWorkUnit() {
                    return workUnitMockData().getName();
                }

                @Override
                public String getWorkGroup() {
                    return workGroupMockData().getName();
                }

                @Override
                public String getProjectAssignment() {
                    return null;
                }

                @Override
                public String getProjectSummaryStatus() {
                    return null;
                }

                @Override
                public String getPhenotypingExternalRef() {
                    return null;
                }

                @Override
                public String getColonyName() {
                    return colonyMockData().getName();
                }

                @Override
                public String getPrivacy() {
                    return privacyMockData().getName();
                }

                @Override
                public String getConsortia() {
                    return consortiumMockData().getName();
                }
            };

        return projectSearchDownloadProjection;
    }


    public static OutcomeType outcomeTypeMockData() {
        OutcomeType outcomeType = new OutcomeType();
        outcomeType.setId(1L);
        outcomeType.setName(TEST_OUTCOME_TYPE_NAME);
        return outcomeType;
    }

    public static MolecularMutationType molecularMutationTypeMockData() {
        MolecularMutationType molecularMutationType = new MolecularMutationType();
        molecularMutationType.setId(1L);
        molecularMutationType.setName(MOLECULAR_MUTATION_TYPE_NAME);
        return molecularMutationType;
    }

    public static Set<Mutation> mutationSetMockData() {
        Set<Mutation> mutations = new HashSet<>();
        mutations.add(mutationMockData());
        return mutations;
    }

    public static Mutation mutationMockData() {
        Mutation mutation = new Mutation();
        mutation.setId(1L);
        mutation.setMin(MIN_000000001);
        return mutation;
    }

    public static List<GeneList> geneListListMockData() {
        List<GeneList> geneLists = new ArrayList<>();
        geneLists.add(geneListMockData());
        return geneLists;
    }

    public static GeneList geneListMockData() {
        GeneList geneList = new GeneList();
        geneList.setId(1L);
        return geneList;
    }

    public static ListRecord listRecordMockData() {
        ListRecord listRecord = new ListRecord();
        listRecord.setId(1L);
        return listRecord;
    }

    public static ListRecordType listRecordTypeMockData() {
        ListRecordType listRecordType = new ListRecordType();
        listRecordType.setId(1L);
        return listRecordType;
    }

    public static GeneByListRecord geneByListRecordMockData() {
        GeneByListRecord geneByListRecord = new GeneByListRecord();
        geneByListRecord.setId(1L);
        geneByListRecord.setAccId(MGI_00000001);
        return geneByListRecord;
    }

    public static GeneListProjection geneListProjectionsMockData() {
        return new GeneListProjection() {
            @Override
            public Long getId() {
                return 1L;
            }

            @Override
            public String getNote() {
                return null;
            }

            @Override
            public String getVisible() {
                return null;
            }

            @Override
            public String getSymbol() {
                return null;
            }

            @Override
            public String getAccId() {
                return null;
            }

            @Override
            public String getTpn() {
                return null;
            }

            @Override
            public String getAssignmentStatus() {
                return null;
            }

            @Override
            public String getPrivacy() {
                return null;
            }

            @Override
            public String getSummaryStatus() {
                return null;
            }
        };
    }

    public static ProcessEvent processEventMockData() {
        ProcessEvent processEvent = new ProcessEvent() {
            @Override
            public Class<? extends Processor> getNextStepProcessor() {
                return null;
            }

            @Override
            public String getName() {
                return "abandonWhenCreated";
            }

            @Override
            public String getDescription() {
                return TEST_DESCRIPTION;
            }

            @Override
            public ProcessState getInitialState() {
                return null;
            }

            @Override
            public ProcessState getEndState() {
                return null;
            }

            @Override
            public boolean isTriggeredByUser() {
                return false;
            }

            @Override
            public String getTriggerNote() {
                return null;
            }
        };

        return processEvent;
    }

    public static TransitionEvaluation transitionEvaluationMockData() {
        TransitionEvaluation transitionEvaluation = new TransitionEvaluation();
        transitionEvaluation.setTransition(processEventMockData());
        return transitionEvaluation;
    }

    public static Root<Plan> rootMockData() {
        Root<Plan> root = new Root<Plan>() {
            @Override
            public EntityType<Plan> getModel() {
                return null;
            }

            @Override
            public Set<Join<Plan, ?>> getJoins() {
                return null;
            }

            @Override
            public boolean isCorrelated() {
                return false;
            }

            @Override
            public From<Plan, Plan> getCorrelationParent() {
                return null;
            }

            @Override
            public <Y> Join<Plan, Y> join(SingularAttribute<? super Plan, Y> singularAttribute) {
                return null;
            }

            @Override
            public <Y> Join<Plan, Y> join(SingularAttribute<? super Plan, Y> singularAttribute,
                                          JoinType joinType) {
                return null;
            }

            @Override
            public <Y> CollectionJoin<Plan, Y> join(
                CollectionAttribute<? super Plan, Y> collectionAttribute) {
                return null;
            }

            @Override
            public <Y> SetJoin<Plan, Y> join(SetAttribute<? super Plan, Y> setAttribute) {
                return null;
            }

            @Override
            public <Y> ListJoin<Plan, Y> join(ListAttribute<? super Plan, Y> listAttribute) {
                return null;
            }

            @Override
            public <K, V> MapJoin<Plan, K, V> join(MapAttribute<? super Plan, K, V> mapAttribute) {
                return null;
            }

            @Override
            public <Y> CollectionJoin<Plan, Y> join(
                CollectionAttribute<? super Plan, Y> collectionAttribute, JoinType joinType) {
                return null;
            }

            @Override
            public <Y> SetJoin<Plan, Y> join(SetAttribute<? super Plan, Y> setAttribute,
                                             JoinType joinType) {
                return null;
            }

            @Override
            public <Y> ListJoin<Plan, Y> join(ListAttribute<? super Plan, Y> listAttribute,
                                              JoinType joinType) {
                return null;
            }

            @Override
            public <K, V> MapJoin<Plan, K, V> join(MapAttribute<? super Plan, K, V> mapAttribute,
                                                   JoinType joinType) {
                return null;
            }

            @Override
            public <X, Y> Join<X, Y> join(String s) {
                return null;
            }

            @Override
            public <X, Y> CollectionJoin<X, Y> joinCollection(String s) {
                return null;
            }

            @Override
            public <X, Y> SetJoin<X, Y> joinSet(String s) {
                return null;
            }

            @Override
            public <X, Y> ListJoin<X, Y> joinList(String s) {
                return null;
            }

            @Override
            public <X, K, V> MapJoin<X, K, V> joinMap(String s) {
                return null;
            }

            @Override
            public <X, Y> Join<X, Y> join(String s, JoinType joinType) {
                return null;
            }

            @Override
            public <X, Y> CollectionJoin<X, Y> joinCollection(String s, JoinType joinType) {
                return null;
            }

            @Override
            public <X, Y> SetJoin<X, Y> joinSet(String s, JoinType joinType) {
                return null;
            }

            @Override
            public <X, Y> ListJoin<X, Y> joinList(String s, JoinType joinType) {
                return null;
            }

            @Override
            public <X, K, V> MapJoin<X, K, V> joinMap(String s, JoinType joinType) {
                return null;
            }

            @Override
            public Path<?> getParentPath() {
                return null;
            }

            @Override
            public <Y> Path<Y> get(SingularAttribute<? super Plan, Y> singularAttribute) {
                return null;
            }

            @Override
            public <E, C extends Collection<E>> Expression<C> get(
                PluralAttribute<Plan, C, E> pluralAttribute) {
                return null;
            }

            @Override
            public <K, V, M extends Map<K, V>> Expression<M> get(
                MapAttribute<Plan, K, V> mapAttribute) {
                return null;
            }

            @Override
            public Expression<Class<? extends Plan>> type() {
                return null;
            }

            @Override
            public <Y> Path<Y> get(String s) {
                return null;
            }

            @Override
            public Predicate isNull() {
                return null;
            }

            @Override
            public Predicate isNotNull() {
                return null;
            }

            @Override
            public Predicate in(Object... objects) {
                return null;
            }

            @Override
            public Predicate in(Expression<?>... expressions) {
                return null;
            }

            @Override
            public Predicate in(Collection<?> collection) {
                return null;
            }

            @Override
            public Predicate in(Expression<Collection<?>> expression) {
                return null;
            }

            @Override
            public <X> Expression<X> as(Class<X> aClass) {
                return null;
            }

            @Override
            public Set<Fetch<Plan, ?>> getFetches() {
                return null;
            }

            @Override
            public <Y> Fetch<Plan, Y> fetch(SingularAttribute<? super Plan, Y> singularAttribute) {
                return null;
            }

            @Override
            public <Y> Fetch<Plan, Y> fetch(SingularAttribute<? super Plan, Y> singularAttribute,
                                            JoinType joinType) {
                return null;
            }

            @Override
            public <Y> Fetch<Plan, Y> fetch(PluralAttribute<? super Plan, ?, Y> pluralAttribute) {
                return null;
            }

            @Override
            public <Y> Fetch<Plan, Y> fetch(PluralAttribute<? super Plan, ?, Y> pluralAttribute,
                                            JoinType joinType) {
                return null;
            }

            @Override
            public <X, Y> Fetch<X, Y> fetch(String s) {
                return null;
            }

            @Override
            public <X, Y> Fetch<X, Y> fetch(String s, JoinType joinType) {
                return null;
            }

            @Override
            public Selection<Plan> alias(String s) {
                return null;
            }

            @Override
            public boolean isCompoundSelection() {
                return false;
            }

            @Override
            public List<Selection<?>> getCompoundSelectionItems() {
                return null;
            }

            @Override
            public Class<? extends Plan> getJavaType() {
                return null;
            }

            @Override
            public String getAlias() {
                return null;
            }
        };
        return root;
    }

    public static GeneInterestReportProjectProjection geneInterestReportProjectProjectionMockData() {
        return new GeneInterestReportProjectProjection() {
            @Override
            public Long getProjectId() {
                return 1L;
            }

            @Override
            public String getProjectTpn() {
                return TPN_000000001;
            }

            @Override
            public String getAssignmentName() {
                return TEST_ASSIGMENT_NAME;
            }

            @Override
            public String getGeneAccId() {
                return MGI_00000001;
            }

            @Override
            public String getGeneSymbol() {
                return MGI_00000001;
            }

            @Override
            public String getPlanIdentificationNumber() {
                return "1";
            }

            @Override
            public String getPlanSummaryStatus() {
                return TEST_SUMMARY_STATUS;
            }
        };
    }

    public static GeneInterestReportGeneProjection geneInterestReportGeneProjectionMockData() {
        return new GeneInterestReportGeneProjection() {
            @Override
            public Long getProjectId() {
                return 1L;
            }

            @Override
            public String getProjectTpn() {
                return TPN_000000001;
            }

            @Override
            public String getAssignmentName() {
                return TEST_ASSIGMENT_NAME;
            }

            @Override
            public String getPlanIdentificationNumber() {
                return TEST_IDENTIFICATION_NUMBER;
            }

            @Override
            public String getPlanSummaryStatus() {
                return TEST_SUMMARY_STATUS;
            }

            @Override
            public Long getOutcomeId() {
                return 1L;
            }

            @Override
            public String getMutationIdentificationNumber() {
                return TEST_IDENTIFICATION_NUMBER;
            }

            @Override
            public String getMutationSymbol() {
                return null;
            }

            @Override
            public String getGeneAccId() {
                return MGI_00000001;
            }

            @Override
            public String getGeneSymbol() {
                return MGI_00000001;
            }
        };
    }

    public static PhenotypingStage phenotypingStageMockData() {
        PhenotypingStage phenotypingStage = new PhenotypingStage();
        phenotypingStage.setId(1L);
        phenotypingStage.setPsn(PSN_000000001);
        phenotypingStage.setPhenotypingAttempt(phenotypingAttemptMockData());
        phenotypingStage.setPhenotypingStageType(phenotypingStageTypeMockData());
        return phenotypingStage;
    }

    public static PhenotypingAttempt phenotypingAttemptMockData() {
        PhenotypingAttempt phenotypingAttempt = new PhenotypingAttempt();
        phenotypingAttempt.setId(1L);
        phenotypingAttempt.setPlan(planMockData());
        return phenotypingAttempt;
    }

    public static PhenotypingStageType phenotypingStageTypeMockData() {
        PhenotypingStageType phenotypingStageType = new PhenotypingStageType();
        phenotypingStageType.setId(1L);
        phenotypingStageType.setName(PhenotypingStageTypeName.EARLY_ADULT.getLabel());
        return phenotypingStageType;
    }

    public static PlanStartingPoint planStartingPointMockData() {
        PlanStartingPoint planStartingPoint = new PlanStartingPoint();
        planStartingPoint.setId(1L);
        planStartingPoint.setOutcome(outcomeMockData());
        return planStartingPoint;
    }

    public static Funder funderMockData() {
        Funder funder = new Funder();
        funder.setId(1L);
        return funder;
    }

    public static GeneInterestReportPhenotypingAttemptProjection geneInterestReportPhenotypingAttemptProjectionMockData() {
      return  new GeneInterestReportPhenotypingAttemptProjection() {
          @Override
          public Long getProjectId() {
              return 1L;
          }

          @Override
          public String getProjectTpn() {
              return TPN_000000001;
          }

          @Override
          public String getPlanIdentificationNumber() {
              return TEST_IDENTIFICATION_NUMBER;
          }

          @Override
          public String getPhenotypingWorkUnit() {
              return null;
          }

          @Override
          public String getPhenotypingWorkGroup() {
              return null;
          }

          @Override
          public String getCohortProductionWorkUnit() {
              return null;
          }

          @Override
          public String getPhenotypingStageStatus() {
              return "Created";
          }

          @Override
          public Long getOutcomeId() {
              return 1L;
          }
      };
    }

    public  static GeneInterestReportOutcomeMutationProjection geneInterestReportOutcomeMutationProjectionMockData(){
        return new GeneInterestReportOutcomeMutationProjection() {
            @Override
            public Long getOutcomeId() {
                return 1L;
            }

            @Override
            public Long getMutationId() {
                return 1L;
            }

            @Override
            public String getSymbol() {
                return MGI_00000001;
            }
        };
    }


    public static GeneInterestReportMutationGeneProjection geneInterestReportMutationGeneProjectionMockData(){
        return new GeneInterestReportMutationGeneProjection() {
            @Override
            public Long getMutationId() {
                return 1L;
            }

            @Override
            public Long getGeneId() {
                return 1L;
            }

            @Override
            public Gene getGene() {
                return geneMockData();
            }
        };
    }

    public static ProcessData processDataMockData(){
        return new ProcessData() {
            @Override
            public ProcessEvent getProcessDataEvent() {
                return null;
            }

            @Override
            public void setProcessDataEvent(ProcessEvent processEvent) {

            }

            @Override
            public Status getProcessDataStatus() {
                return null;
            }

            @Override
            public void setProcessDataStatus(Status status) {

            }
        };
    }
}
