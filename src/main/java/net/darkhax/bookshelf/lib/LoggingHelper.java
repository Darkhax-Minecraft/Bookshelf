/**
 * This class was created by <Darkhax>. It is distributed as part of Bookshelf. You can find
 * the original source here: https://github.com/Darkhax-Minecraft/Bookshelf
 *
 * Bookshelf is Open Source and distributed under the GNU Lesser General Public License version
 * 2.1.
 */
package net.darkhax.bookshelf.lib;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.SystemUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * A wrapper to the Log4J wrapper, which adds some extra utility, such as the
 * stacktrace being added to warning messages.
 */
public class LoggingHelper {

    /**
     * The logger delegate.
     */
    private final Logger logger;

    /**
     * Constructs the helper using a logger name.
     *
     * @param name The name of the logger to create.
     */
    public LoggingHelper (String name) {

        this(LogManager.getLogger(name));
    }

    /**
     * Constructs the helper using a Log4J logger.
     *
     * @param logger The logger to use as a delegate.
     */
    public LoggingHelper (Logger logger) {

        this.logger = logger;
    }

    /**
     * Logs an exception or error. This is intended for when you are catching an
     * error and should be used over {@link Throwable#printStackTrace()}.
     *
     * @param t The Throwable object to catch.
     */
    public void catching (Throwable t) {

        this.logger.catching(t);
    }

    /**
     * Logs a debug message. Debug messages are not printed to the console, but they
     * are added to the console file.
     *
     * @param message The message to print. Likely uses log4J's format which is {}
     *        for parameters.
     * @param params The parameters for the messages. This can be used to insert
     *        info directly to the message, or completely ignored.
     */
    public void debug (String message, Object... params) {

        this.logger.debug(message, params);
    }

    /**
     * Logs an error message. Error messages are printed to the console and the log
     * file.
     *
     * @param message The message to print. Likely uses log4J's format which is {}
     *        for parameters.
     * @param params The parameters for the messages. This can be used to insert
     *        info directly to the message, or completely ignored.
     */
    public void error (String message, Object... params) {

        this.logger.error(message, params);
    }

    /**
     * Logs a fatal error message. This should be used to log errors which will
     * prevent the game from working as expected and are likely to cause a crash.
     * Fatal messages are printed to the console and the log file.
     *
     * @param message The message to print. Likely uses log4J's format which is {}
     *        for parameters.
     * @param params The parameters for the messages. This can be used to insert
     *        info directly to the message, or completely ignored.
     */
    public void fatal (String message, Object... params) {

        this.logger.fatal(message, params);
    }

    /**
     * Logs a generic info message. Info messages are printed to the console and the
     * log file.
     *
     * @param message The message to print. Likely uses log4J's format which is {}
     *        for parameters.
     * @param params The parameters for the messages. This can be used to insert
     *        info directly to the message, or completely ignored.
     */
    public void info (String message, Object... params) {

        this.logger.info(message, params);
    }

    /**
     * Logs a message at any Level.
     *
     * @param level The level of the message to log.
     * @param message The message to print. Likely uses log4J's format which is {}
     *        for parameters.
     * @param params The parameters for the messages. This can be used to insert
     *        info directly to the message, or completely ignored.
     */
    public void log (Level level, String message, Object... params) {

        this.logger.log(level, message, params);
    }

    /**
     * Logs a trace message. This is for fine grained debug messages. Trace messages
     * are not printed to the console, but they are added to the console file.
     *
     * @param message The message to print. Likely uses log4J's format which is {}
     *        for parameters.
     * @param params The parameters for the messages. This can be used to insert
     *        info directly to the message, or completely ignored.
     */
    public void trace (String message, Object... params) {

        this.logger.trace(message, params);
    }

    /**
     * Logs a warning message. These messages will also include a stacktrace.
     * Warning messages are printed to the console and the log file.
     *
     * @param message The message to print. Likely uses log4J's format which is {}
     *        for parameters.
     * @param params The parameters for the messages. This can be used to insert
     *        info directly to the message, or completely ignored.
     */
    public void warn (String message, Object... params) {

        this.warn(new LogWarningException(), message, params);
    }

    /**
     * Logs a warning message. These messages will also include a stacktrace.
     * Warning messages are printed to the console and the log file.
     *
     * @param message The message to print. Likely uses log4J's format which is {}
     *        for parameters.
     * @param params The parameters for the messages. This can be used to insert
     *        info directly to the message, or completely ignored.
     */
    public void warn (Throwable t, String message, Object... params) {

        this.logger.warn(message, params);
        this.logger.catching(t);
    }

    /**
     * Gets the internal logger delegate.
     *
     * @return The internal logger.
     */
    public Logger getLogger () {

        return this.logger;
    }

    /**
     * Creates a noticeable warning, similar to the ones created by the FMLLog.
     *
     * @param trace If true, a small stack trace will be included in the error
     *        message.
     * @param lines Each entry will be printed as part of the error message. If any
     *        entry is longer than 78 chars it will be auto wrapped into multiple
     *        lines.
     */
    public void noticableWarning (boolean trace, List<String> lines) {

        this.error("********************************************************************************");

        for (final String line : lines) {

            for (final String subline : wrapString(line, 78, false, new ArrayList<String>())) {

                this.error("* " + subline);
            }
        }

        if (trace) {

            final StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();

            for (int i = 2; i < 8 && i < stackTrace.length; i++) {
                this.warn("*  at {}{}", stackTrace[i].toString(), i == 7 ? "..." : "");
            }
        }

        this.error("********************************************************************************");
    }

    public static List<String> wrapString (String string, int lnLength, boolean wrapLongWords, List<String> list) {

        final String lines[] = WordUtils.wrap(string, lnLength, null, wrapLongWords).split(SystemUtils.LINE_SEPARATOR);

        for (final String line : lines) {
            list.add(line);
        }

        return list;
    }

    /**
     * A wrapper for RuntimeException, used to print the current stacktrace.
     */
    private static class LogWarningException extends RuntimeException {

        private static final long serialVersionUID = 1L;

        public LogWarningException () {

            super("An error was encountered. This is how!");
        }
    }
}