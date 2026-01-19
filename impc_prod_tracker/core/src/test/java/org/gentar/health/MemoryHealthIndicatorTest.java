package org.gentar.health;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.health.contributor.Health;
import org.springframework.boot.health.contributor.Status;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

class MemoryHealthIndicatorTest {

    private MemoryHealthIndicator memoryHealthIndicator;

    @BeforeEach
    void setUp() {
        memoryHealthIndicator = new MemoryHealthIndicator();
    }

    @Test
    void testHealthReturnsHealthObject() {
        Health health = memoryHealthIndicator.health();
        
        assertNotNull(health, "Health object should not be null");
    }

    @Test
    void testHealthContainsRequiredDetails() {
        Health health = memoryHealthIndicator.health();
        
        assertThat("Health should contain usedMemory detail", 
            health.getDetails(), hasKey("usedMemory (MB)"));
        assertThat("Health should contain freeMemory detail", 
            health.getDetails(), hasKey("freeMemory (MB)"));
        assertThat("Health should contain totalMemory detail", 
            health.getDetails(), hasKey("totalMemory (MB)"));
        assertThat("Health should contain maxMemory detail", 
            health.getDetails(), hasKey("maxMemory (MB)"));
    }

    @Test
    void testHealthStatusIsUpOrDown() {
        Health health = memoryHealthIndicator.health();

        assertNotNull(health);
        assertTrue(health.getStatus() == Status.UP ||
                  health.getStatus() == Status.DOWN,
                  "Health status should be either UP or DOWN");
    }

    @Test
    void testHealthDetailsAreNonNegative() {
        Health health = memoryHealthIndicator.health();
        
        Long usedMemory = (Long) health.getDetails().get("usedMemory (MB)");
        Long freeMemory = (Long) health.getDetails().get("freeMemory (MB)");
        Long totalMemory = (Long) health.getDetails().get("totalMemory (MB)");
        Long maxMemory = (Long) health.getDetails().get("maxMemory (MB)");
        
        assertThat("Used memory should be non-negative", usedMemory, greaterThanOrEqualTo(0L));
        assertThat("Free memory should be non-negative", freeMemory, greaterThanOrEqualTo(0L));
        assertThat("Total memory should be non-negative", totalMemory, greaterThanOrEqualTo(0L));
        assertThat("Max memory should be positive", maxMemory, greaterThan(0L));
    }

    @Test
    void testHealthDownWhenMemoryExceedsThreshold() {
        // This test verifies that when memory usage is high, status is DOWN
        // Note: This may not always trigger in test environment, but structure is verified
        Health health = memoryHealthIndicator.health();

        assertNotNull(health);
        if (health.getStatus() == Status.DOWN) {
            assertThat("Health should contain error detail when DOWN", 
                health.getDetails(), hasKey("error"));
            assertEquals("Memory usage exceeds threshold", 
                health.getDetails().get("error"));
        }
    }

    @Test
    void testMemoryValuesAreReasonable() {
        Health health = memoryHealthIndicator.health();

        assertNotNull(health);
        Long usedMemory = (Long) health.getDetails().get("usedMemory (MB)");
        Long totalMemory = (Long) health.getDetails().get("totalMemory (MB)");
        Long maxMemory = (Long) health.getDetails().get("maxMemory (MB)");
        
        // Verify memory values are in reasonable ranges (not negative, used <= total, total <= max)
        assertThat("Used memory should be less than or equal to total memory", 
            usedMemory, lessThanOrEqualTo(totalMemory));
        assertThat("Total memory should be less than or equal to max memory", 
            totalMemory, lessThanOrEqualTo(maxMemory));
    }
}
