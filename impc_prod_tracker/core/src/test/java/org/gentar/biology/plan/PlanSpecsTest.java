package org.gentar.biology.plan;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class PlanSpecsTest {

    PlanSpecs testInstance;

    @BeforeEach
    void setUp() {
        testInstance = new PlanSpecs();
    }

    @Test
    void withWorkUnitNamesNull() {
        Specification<Plan> specifications =
            Specification.where(PlanSpecs.withWorkUnitNames(null));
        assertNotNull(specifications);
    }

    @Test
    void withProjectTpnsNull() {

        Specification<Plan> specifications =
            Specification.where(PlanSpecs.withProjectTpns(null));
        assertNotNull(specifications);
    }

    @Test
    void withWorkGroupNamesNull() {
        Specification<Plan> specifications =
            Specification.where(PlanSpecs.withWorkGroupNames(null));
        assertNotNull(specifications);
    }

    @Test
    void withStatusNamesNull() {
        Specification<Plan> specifications =
            Specification.where(PlanSpecs.withStatusNames(null));
        assertNotNull(specifications);
    }

    @Test
    void withSummaryStatusNamesNull() {
        Specification<Plan> specifications =
            Specification.where(PlanSpecs.withSummaryStatusNames(null));
        assertNotNull(specifications);
    }

    @Test
    void withPinsNull() {
        Specification<Plan> specifications =
            Specification.where(PlanSpecs.withPins(null));
        assertNotNull(specifications);
    }

    @Test
    void withTypeNamesNull() {
        Specification<Plan> specifications =
            Specification.where(PlanSpecs.withTypeNames(null));
        assertNotNull(specifications);
    }

    @Test
    void withAttemptTypeNamesNull() {
        Specification<Plan> specifications =
            Specification.where(PlanSpecs.withAttemptTypeNames(null));
        assertNotNull(specifications);
    }

    @Test
    void withImitsMiAttemptsNull() {
        Specification<Plan> specifications =
            Specification.where(PlanSpecs.withImitsMiAttempts(null));
        assertNotNull(specifications);
    }

    @Test
    void withImitsPhenotypeAttemptsNull() {
        Specification<Plan> specifications =
            Specification.where(PlanSpecs.withImitsPhenotypeAttempts(null));
        assertNotNull(specifications);
    }

    @Test
    void withPhenotypingExternalRefsNull() {
        Specification<Plan> specifications =
            Specification.where(PlanSpecs.withPhenotypingExternalRefs(null));
        assertNotNull(specifications);
    }

    @Test
    void withDoNotCountTowardsCompletenessNull() {
        Specification<Plan> specifications =
            Specification.where(PlanSpecs.withDoNotCountTowardsCompleteness(null));
        assertNotNull(specifications);
    }
}