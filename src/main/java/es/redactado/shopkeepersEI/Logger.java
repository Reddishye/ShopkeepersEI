package es.redactado.shopkeepersEI;

import es.redactado.shopkeepersEI.utils.ColorTranslator;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.ansi.ANSIComponentSerializer;

import java.io.PrintStream;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;


// this logger is not that bad eh
// todo: adapt logger format cuz rn feels like avg logback config
public class Logger {

    private static final String RESET = "\u001B[0m";
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm:ss");
    private static final ANSIComponentSerializer ANSI = ANSIComponentSerializer.ansi();

    private final String prefix;
    private final PrintStream out;
    private LogLevel minLevel;

    public enum LogLevel {
        TRACE(0, "\u001B[90m", "TRACE  ", "&#999999"),
        DEBUG(1, "\u001B[36m", "DEBUG  ", "&#00FFFF"),
        INFO(2, "\u001B[32m", "INFO   ", "&#00FF00"),
        SUCCESS(2, "\u001B[92m", "SUCCESS", "&#00FF88"),
        WARN(3, "\u001B[33m", "WARN   ", "&#FFAA00"),
        ERROR(4, "\u001B[31m", "ERROR  ", "&#FF0000"),
        FATAL(5, "\u001B[91;1m", "FATAL  ", "&#FF0000");

        final int priority;
        final String ansi;
        final String name;
        final String hex;

        LogLevel(int priority, String ansi, String name, String hex) {
            this.priority = priority;
            this.ansi = ansi;
            this.name = name;
            this.hex = hex;
        }

        boolean shouldLog(LogLevel min) {
            return this.priority >= min.priority;
        }
    }

    public Logger(String prefix) {
        this(prefix, System.out, LogLevel.INFO);
    }

    public Logger(String prefix, LogLevel minLevel) {
        this(prefix, System.out, minLevel);
    }

    public Logger(String prefix, PrintStream out, LogLevel minLevel) {
        this.prefix = prefix;
        this.out = out;
        this.minLevel = minLevel;
    }

    public void trace(String msg) {
        log(LogLevel.TRACE, msg);
    }

    public void debug(String msg) {
        log(LogLevel.DEBUG, msg);
    }

    public void info(String msg) {
        log(LogLevel.INFO, msg);
    }

    public void success(String msg) {
        log(LogLevel.SUCCESS, msg);
    }

    public void warn(String msg) {
        log(LogLevel.WARN, msg);
    }

    public void error(String msg) {
        log(LogLevel.ERROR, msg);
    }

    public void error(String msg, Throwable t) {
        log(LogLevel.ERROR, msg);
        if (t != null) t.printStackTrace(out);
    }

    public void fatal(String msg) {
        log(LogLevel.FATAL, msg);
    }

    public void fatal(String msg, Throwable t) {
        log(LogLevel.FATAL, msg);
        if (t != null) t.printStackTrace(out);
    }

    public void log(LogLevel level, String msg) {
        if (!level.shouldLog(minLevel)) return;

        StringBuilder sb = new StringBuilder(256);
        String time = LocalTime.now().format(TIME_FORMAT);

        sb.append("\u001B[90m").append(time).append(RESET).append(" ");
        sb.append(level.ansi).append(level.name).append("|").append(RESET).append(" ");

        if (prefix != null && !prefix.isEmpty()) {
            sb.append("\u001B[96m[").append(prefix).append("]").append(RESET).append(" ");
        }

        Component component = ColorTranslator.translate(msg);
        String ansiMsg = ANSI.serialize(component);
        sb.append(ansiMsg);

        out.println(sb);
    }

    public void separator() {
        out.println("\u001B[90m" + "━".repeat(80) + RESET);
    }

    public void header(String title) {
        separator();
        info("<b><gradient:#00FF88:#00FFFF>" + title + "</gradient></b>");
        separator();
    }

    public void setMinLevel(LogLevel level) {
        this.minLevel = level;
    }

    public LogLevel getMinLevel() {
        return minLevel;
    }

    public boolean isDebugEnabled() {
        return LogLevel.DEBUG.shouldLog(minLevel);
    }

    public boolean isTraceEnabled() {
        return LogLevel.TRACE.shouldLog(minLevel);
    }

    public static Logger create(String prefix) {
        return new Logger(prefix);
    }

    public static Logger create(String prefix, LogLevel level) {
        return new Logger(prefix, level);
    }
}
// redactado was also, again, for the fourth time, here