package org.gentar.health;

import org.springframework.boot.health.contributor.Health;
import org.springframework.boot.health.contributor.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class MemoryHealthIndicator implements HealthIndicator {

    @Override
    public Health health() {

        long megaBytes = 1024 * 1024;
        long maxMemory = Runtime.getRuntime().maxMemory();

        long freeMemory = Runtime.getRuntime().freeMemory();
        long totalMemory = Runtime.getRuntime().totalMemory();
        long usedMemory = totalMemory - freeMemory;

        if (usedMemory > maxMemory * 0.9) {
            return Health.down()
                    .withDetail("error", "Memory usage exceeds threshold")
                    .withDetail("usedMemory (MB)", usedMemory / megaBytes)
                    .withDetail("freeMemory (MB)", freeMemory / megaBytes)
                    .withDetail("totalMemory (MB)", totalMemory / megaBytes)
                    .withDetail("maxMemory (MB)", maxMemory / megaBytes)
                    .build();
        }

        return Health.up()
                .withDetail("usedMemory (MB)", usedMemory / megaBytes)
                .withDetail("freeMemory (MB)", freeMemory / megaBytes)
                .withDetail("totalMemory (MB)", totalMemory / megaBytes)
                .withDetail("maxMemory (MB)", maxMemory / megaBytes)
                .build();
    }
}
