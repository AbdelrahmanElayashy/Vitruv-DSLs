package tools.vitruv.dsls.reactions.runtime.structure

import java.util.logging.Logger

import java.util.logging.FileHandler
import java.util.logging.Level
import java.util.logging.Formatter
import java.util.logging.LogRecord
import java.text.SimpleDateFormat
import java.util.Date
import java.io.IOException
import tools.vitruv.dsls.reactions.runtime.state.ReactionExecutionState
import tools.vitruv.change.correspondence.infrastructure.tracing.CorrespondenceTraceRecorder
import java.util.Map
import tools.vitruv.change.correspondence.infrastructure.tracing.CorrespondenceTraceRecorder.TraceLogEntry
import java.util.List
import java.util.Collections
import tools.vitruv.dsls.reactions.runtime.state.RoutineContext
import tools.vitruv.dsls.reactions.runtime.state.RoutineExecutionProfiler

abstract class AbstractLogStep {

    protected static final String FILE_LOG_NAME = "target/log-reactions.txt"
    protected static final boolean ENABLE_LOGGING = !Boolean.getBoolean("disable.reactions.logging")
    
    val ReactionExecutionState executionState
    val Logger fileLogger

    new(ReactionExecutionState executionState) {
        this.executionState = executionState
        this.fileLogger = initLogger()
    }

    private def Logger initLogger() {
    	if (!ENABLE_LOGGING) return null;
        val logger = Logger.getLogger("ReactionLogger")

        if (logger.getHandlers().length == 0) {
            try {
                val fh = new FileHandler(FILE_LOG_NAME, true)
                fh.level = Level.ALL
                fh.formatter = new Formatter() {
                    override format(LogRecord record) {
                        val timestamp = new SimpleDateFormat("MMM dd, yyyy hh:mm:ss a").format(new Date(record.getMillis))
                        return String.format("%s %s: %s%n", timestamp, record.getLevel, record.getMessage)
                    }
                }
                logger.addHandler(fh)
                logger.level = Level.ALL
            } catch (IOException e) {
                e.printStackTrace
            }
        }

        return logger
    }

    protected def log(String message, Level level) {
        fileLogger.log(level, message)
    }
    
	protected def int getTraceCount() {
	    val routineName = RoutineContext.get();
	    if (routineName === null) {
	        return -1;
	    }
	    return CorrespondenceTraceRecorder.getInstance().getTraceCount(routineName);
	}
	
	protected def List<TraceLogEntry> getTraceEntries() {
	    val routineName = RoutineContext.get();
	    if (routineName === null) {
	        return Collections.emptyList();
	    }
	    return CorrespondenceTraceRecorder.getInstance().getTraceEntries(routineName);
	}
	
	protected def Map<String, Integer> getAllTraceCounts() {
	    return CorrespondenceTraceRecorder.getInstance().getAllTraceCounts();
	}

	protected def String getExecutionTimeFormattedMs() {
	    val routineName = RoutineContext.get();
	    if (routineName === null) {
	        return "n/a";
	    }
	    val durationInNano = RoutineExecutionProfiler.getInstance().getExecutionTime(routineName);
	    val durationInMs = durationInNano / 1_000_000.0;
	    return String.format("%.2f ms", durationInMs);
	}
}