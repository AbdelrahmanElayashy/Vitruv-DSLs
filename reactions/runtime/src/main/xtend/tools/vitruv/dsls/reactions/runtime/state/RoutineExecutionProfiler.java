package tools.vitruv.dsls.reactions.runtime.state;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * RoutineExecutionProfiler tracks the execution time of routines for performance analysis.
 *
 * It stores the total duration in milliseconds that each routine has taken to execute,
 * supporting use cases such as performance tuning, debugging, and research.
 *
 * This class is intended to be used in conjunction with {@link RoutineTraceScope},
 * which measures the runtime duration of each routine and records it via this profiler.
 *
 * Example:
 * - When a routine begins execution, its start time is recorded in {@link RoutineTraceScope}.
 * - Upon routine completion, the duration is calculated and passed to this profiler.
 *
 */
public class RoutineExecutionProfiler {
    private static final RoutineExecutionProfiler INSTANCE = new RoutineExecutionProfiler();
    private final Map<String, Long> executionTimesInNano = new HashMap<>();

    public static RoutineExecutionProfiler getInstance() {
        return INSTANCE;
    }

    public synchronized void recordExecutionTime(String routineName, long durationInNano) {
        executionTimesInNano.put(routineName, durationInNano);
    }

    public synchronized long getExecutionTime(String routineName) {
        return executionTimesInNano.getOrDefault(routineName, 0L);
    }

    public synchronized Map<String, Long> getAllExecutionTimes() {
        return Collections.unmodifiableMap(executionTimesInNano);
    }

    public synchronized void clear() {
        executionTimesInNano.clear();
    }
}
