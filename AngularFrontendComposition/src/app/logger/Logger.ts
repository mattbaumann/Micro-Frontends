import { LoggerMessage } from "./LoggerMessage";
import { LoggerLevel } from "./LoggerLevel";
import { LoggerAppender } from "./LoggerAppender";

/**
 * The logger implements the public API for sending messages. It generates,
 * filters and dispatches messages to the correct appender.
 */
export interface Logger {
  level: LoggerLevel;
  name: string;
  appender: LoggerAppender;

  trace(message : string, cause?: Error);
  info(message : string, cause?: Error);
  warn(message : string, cause?: Error);
  error(message : string, cause?: Error);
  fatal(message : string, cause?: Error);

  log(message : LoggerMessage)
}
