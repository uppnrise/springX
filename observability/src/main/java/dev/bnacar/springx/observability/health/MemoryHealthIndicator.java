package dev.bnacar.springx.observability.health;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;

/**
 * Health indicator that monitors JVM memory usage.
 * Reports the current memory usage and status based on configurable thresholds.
 */
public class MemoryHealthIndicator implements HealthIndicator {

    private final double warningThreshold;
    private final double criticalThreshold;

    /**
     * Constructs a new MemoryHealthIndicator with the specified thresholds.
     *
     * @param warningThreshold the warning threshold as a percentage (0.0-1.0)
     * @param criticalThreshold the critical threshold as a percentage (0.0-1.0)
     */
    public MemoryHealthIndicator(double warningThreshold, double criticalThreshold) {
        this.warningThreshold = warningThreshold;
        this.criticalThreshold = criticalThreshold;
    }

    @Override
    public Health health() {
        MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
        MemoryUsage heapMemory = memoryBean.getHeapMemoryUsage();
        MemoryUsage nonHeapMemory = memoryBean.getNonHeapMemoryUsage();

        long maxMemory = heapMemory.getMax();
        long usedMemory = heapMemory.getUsed();

        double memoryUsageRatio = (double) usedMemory / maxMemory;

        Health.Builder builder = Health.up()
                .withDetail("heap.used", formatSize(usedMemory))
                .withDetail("heap.max", formatSize(maxMemory))
                .withDetail("heap.usage", String.format("%.2f%%", memoryUsageRatio * 100))
                .withDetail("non-heap.used", formatSize(nonHeapMemory.getUsed()))
                .withDetail("non-heap.max", formatSize(nonHeapMemory.getMax()));

        if (memoryUsageRatio >= criticalThreshold) {
            return builder.down()
                    .withDetail("message", "Memory usage is critical")
                    .build();
        } else if (memoryUsageRatio >= warningThreshold) {
            return builder.status("WARNING")
                    .withDetail("message", "Memory usage is high")
                    .build();
        } else {
            return builder.withDetail("message", "Memory usage is normal")
                    .build();
        }
    }

    /**
     * Formats a size in bytes to a human-readable string.
     *
     * @param bytes the size in bytes
     * @return the formatted size
     */
    private String formatSize(long bytes) {
        if (bytes < 0) {
            return "N/A";
        }

        double kilobytes = bytes / 1024.0;
        if (kilobytes < 1024) {
            return String.format("%.2f KB", kilobytes);
        }

        double megabytes = kilobytes / 1024.0;
        if (megabytes < 1024) {
            return String.format("%.2f MB", megabytes);
        }

        double gigabytes = megabytes / 1024.0;
        return String.format("%.2f GB", gigabytes);
    }
}
