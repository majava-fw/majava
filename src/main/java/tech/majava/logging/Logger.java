/*
 *  majava - tech.majava.logging.Logger
 *  Copyright (C) 2021  Majksa
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package tech.majava.logging;

import tech.majava.logging.errors.ErrorsSaver;
import tech.majava.logging.errors.FileErrorsSaver;
import tech.majava.logging.events.LogEvent;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogBuilder;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.message.Message;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.net.URI;

/**
 * <p><b>Class {@link Logger}</b></p>
 *
 * @author majksa
 * @version 1.0.0
 * @since 1.0.0
 */
public class Logger {

    @Getter(AccessLevel.MODULE)
    private final org.apache.logging.log4j.Logger logger;
    @Nullable
    private final ErrorsSaver errorsSaver;
    private final LogEventHandler logEventHandler;
    @Setter
    private Level level = Level.ERROR;

    @SneakyThrows
    public Logger(@Nonnull org.apache.logging.log4j.Logger logger, @Nullable URI errors, @Nonnull LogEventHandler logEventHandler) {
        this.logger = logger;
        this.logEventHandler = logEventHandler;
        if (errors == null) {
            errorsSaver = null;
        } else {
            errorsSaver = new FileErrorsSaver(new File(errors));
        }
    }

    /**
     * Creates a new {@link org.apache.logging.log4j.LogBuilder} on Debug leve;
     *
     * @return the {@link org.apache.logging.log4j.LogBuilder}
     */
    public LogBuilder atDebug() {
        return atLevel(Level.DEBUG);
    }

    /**
     * Creates a new {@link org.apache.logging.log4j.LogBuilder} on Trace level
     *
     * @return the {@link org.apache.logging.log4j.LogBuilder}
     */
    public LogBuilder atTrace() {
        return atLevel(Level.TRACE);
    }

    /**
     * Creates a new {@link org.apache.logging.log4j.LogBuilder} on Info level
     *
     * @return the {@link org.apache.logging.log4j.LogBuilder}
     */
    public LogBuilder atInfo() {
        return atLevel(Level.INFO);
    }

    /**
     * Creates a new {@link org.apache.logging.log4j.LogBuilder} on Warn level
     *
     * @return the {@link org.apache.logging.log4j.LogBuilder}
     */
    public LogBuilder atWarn() {
        return atLevel(Level.WARN);
    }

    /**
     * Creates a new {@link org.apache.logging.log4j.LogBuilder} on Error level
     *
     * @return the {@link org.apache.logging.log4j.LogBuilder}
     */
    public LogBuilder atError() {
        return atLevel(Level.ERROR);
    }

    /**
     * Creates a new {@link org.apache.logging.log4j.LogBuilder} on Fatal level
     *
     * @return the {@link org.apache.logging.log4j.LogBuilder}
     */
    public LogBuilder atFatal() {
        return atLevel(Level.FATAL);
    }

    /**
     * Creates a new {@link org.apache.logging.log4j.LogBuilder} on all levels
     *
     * @return the {@link org.apache.logging.log4j.LogBuilder}
     */
    public LogBuilder always() {
        return atLevel(Level.ALL);
    }

    /**
     * Creates a new {@link org.apache.logging.log4j.LogBuilder} on a level
     *
     * @return the {@link org.apache.logging.log4j.LogBuilder}
     */
    public LogBuilder atLevel(Level level) {
        return new LogBuilderImpl(this, level);
    }

    /**
     * Logs the message in {@link #logger}
     *
     * @param level     The logging Level to check.
     * @param marker    A Marker or null.
     * @param location  The location of the caller.
     * @param message   The message format.
     * @param throwable the {@code Throwable} to log, including its stack trace.
     */
    void logMessage(Level level, Marker marker, StackTraceElement location, Message message, Throwable throwable) {
        if (!level.isMoreSpecificThan(this.level)) {
            return;
        }
        logger.logMessage(level, marker, LogBuilderImpl.FQCN, location, message, throwable);
        final LogEvent event = LogEvent.from(level, marker, location, message, throwable);
        if (event != null) {
            if (logEventHandler.isRunning()) {
                logEventHandler.runEvent(event);
            }
        }
        if (throwable != null && errorsSaver != null) {
            errorsSaver.save(throwable);
        }
    }

}
