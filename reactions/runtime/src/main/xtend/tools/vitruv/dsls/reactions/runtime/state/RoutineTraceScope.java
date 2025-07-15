package tools.vitruv.dsls.reactions.runtime.state;

import tools.vitruv.change.correspondence.infrastructure.tracing.CorrespondenceTraceRecorder;

/**
 *
 * This class is used in a try-with-resources block inside the generated routine execution
 * method. When opened, it sets the current routine name into a thread-local context (RoutineContext),
 * and upon closing, it clears the routine-specific trace count and trace entries recorded
 * in CorrespondenceTraceRecorder.
 *
 * This enables per-routine trace tracking without requiring trace data to be manually passed.
 *
 * Originally added to support logging and performance measurement of routine-level correspondence
 * operations, e.g., for debugging or research purposes.
 */
public class RoutineTraceScope implements AutoCloseable {

    private final String routineName;
    private final long startTimeNano;

    public RoutineTraceScope(String routineName) {
        RoutineContext.open(routineName);
        this.routineName = routineName;
        CorrespondenceTraceRecorder.getInstance().clearRoutineData(routineName);
        CorrespondenceTraceRecorder.getInstance().setRoutineName(routineName);
        this.startTimeNano = System.nanoTime();
    }

    @Override
    public void close() {
        long durationInNano = System.nanoTime() - this.startTimeNano;
        RoutineExecutionProfiler.getInstance().recordExecutionTime(routineName, durationInNano);
        RoutineContext.close();
    }
}
