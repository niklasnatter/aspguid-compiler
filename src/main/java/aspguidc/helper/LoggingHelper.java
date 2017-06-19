package aspguidc.helper;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.*;

/**
 * Helper class which provides static methods regarding to the logger of the application.
 * The application uses the logger to print program events, warnings and errors.
 */
public class LoggingHelper {
    /**
     * Set the configuration which is used in the program to the global logger.
     * <p>
     * The configuration which is set uses two logging handlers. Log records the level SEVERE are printed to stderr,
     * log records with an other level are printed to stdout.
     * <p>
     * The configuration which is set uses a custom log formatter for better readability of the program output.
     */
    public static void initializeLoggerConfiguration() {
        Logger.getGlobal().setUseParentHandlers(false);
        Formatter logFormatter = new SimpleFormatter() {
            @Override
            public synchronized String format(LogRecord record) {
                StringBuilder sb = new StringBuilder();
                if (record.getLevel().equals(Level.SEVERE)) sb.append("[[[error]]] ");
                if (record.getLevel().equals(Level.WARNING)) sb.append("[[[warning]]] ");
                sb.append(record.getMessage());
                if (record.getThrown() != null) {
                    StringWriter sw = new StringWriter();
                    PrintWriter pw = new PrintWriter(sw);
                    record.getThrown().printStackTrace(pw);
                    pw.close();
                    sb.append(" ").append(sw.toString());
                }
                sb.append("\n");
                return sb.toString();
            }
        };

        StreamHandler stderrHandler = getAutoFlushStreamHandler(System.err, logFormatter);
        stderrHandler.setFilter(record -> record.getLevel().equals(Level.SEVERE));
        Logger.getGlobal().addHandler(stderrHandler);

        StreamHandler stdoutHandler = getAutoFlushStreamHandler(System.out, logFormatter);
        stdoutHandler.setFilter(record -> !record.getLevel().equals(Level.SEVERE));
        Logger.getGlobal().addHandler(stdoutHandler);
    }

    /**
     * Return a stream handler which flushes every log record immediately to the given output stream and
     * uses the given formatter to format a log record.
     * <p>
     * This method is used to create the logging handlers which are used in the application, to enable real-time
     * feedback.
     *
     * @param out       output stream which is used to print a log record
     * @param formatter formatter which is used to format a log record
     * @return stream handler which flushes log records immediately
     */
    private static StreamHandler getAutoFlushStreamHandler(OutputStream out, Formatter formatter) {
        return new StreamHandler(out, formatter) {
            @Override
            public synchronized void publish(LogRecord record) {
                super.publish(record);
                this.flush();
            }
        };
    }
}
