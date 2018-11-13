import { LoggerLevel } from './LoggerLevel';
import { LoggerAppender } from './LoggerAppender';

/**
 * Global configuration of the logging service.
 *
 * It holds the public configuration of the logging library.
 */
export interface LoggingConfig {
    loggers : { [key: string] : LoggerConfig };
    appenders : { [key: string] : LoggingAppenderConfig };
}

/**
 * Configuration for the logger component itself.
 */
export interface LoggerConfig {
    level : LoggerLevel;
    appender?: string;
}

/**
 * Configuration for the appender component.
 */
export interface LoggingAppenderConfig {
    appender: LoggerAppender;

    /**
     * Formatter used for formatting the message. This option is
     * highly depending on the type of appender used.
     *
     * Many <code>Appender</code>s have predefined <em>formatters</em>
     * which can be defined here.
     */
    formatter : (LoggingMessage) => string;

    /**
     * Optional configuration for formatters. See formatter
     * documentation for further information about the usage
     * of this field.
     */
    config?: Object;
}
