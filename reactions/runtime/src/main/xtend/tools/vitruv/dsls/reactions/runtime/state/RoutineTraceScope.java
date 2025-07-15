package tools.vitruv.dsls.reactions.runtime.state;

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

    public RoutineTraceScope(String routineName) {
        RoutineContext.open(routineName);
    }

    @Override
    public void close() {
        RoutineContext.close();
    }
}