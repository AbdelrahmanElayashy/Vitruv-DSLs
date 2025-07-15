package tools.vitruv.dsls.reactions.runtime.state;

import tools.vitruv.change.correspondence.infrastructure.tracing.CorrespondenceTraceRecorder;

/**
 * RoutineContext provides a thread-local mechanism to track the name of the currently executing routine.
 *
 * It is primarily used by {@link RoutineTraceScope} and {@link CorrespondenceTraceRecorder}
 * to associate correspondence trace operations (e.g., add, get, remove) with a specific routine.
 *
 * Usage:
 * - `RoutineTraceScope` automatically sets and clears the current routine name.
 *
 * This separation allows tracing to be done transparently without modifying routine signatures
 * or passing routine identifiers explicitly.
 *
 * Thread safety:
 * - Uses a ThreadLocal variable to ensure isolation across if in future, parallel routine executions.
 */
public class RoutineContext {
    private static final ThreadLocal<String> currentRoutine = new ThreadLocal<>();

    public static void open(String routineName) {
        currentRoutine.set(routineName);
        if (routineName != null) {
        	CorrespondenceTraceRecorder.getInstance().clearRoutineData(routineName);
        	CorrespondenceTraceRecorder.getInstance().setRoutineName(routineName);
        }
    }

    public static void close() {
        currentRoutine.remove();
    }

    public static String get() {
        return currentRoutine.get() != null ? currentRoutine.get() : "UNKNOWN_ROUTINE";
    }
}
