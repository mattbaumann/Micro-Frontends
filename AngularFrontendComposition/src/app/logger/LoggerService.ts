import { LoggerMessage } from "./LoggerMessage";
import { LoggerLevel } from "./LoggerLevel";
import { Logger } from "./Logger";
import { Inject, Injectable, InjectionToken } from "@angular/core";
import { LoggingConfig } from "./LoggerConfig";
import { BrowserConsoleAppender, LoggerAppender } from "./LoggerAppender";
import { Observable, Observer } from 'rxjs';
import { filter } from "rxjs/internal/operators";

export const LoggerConfigToken = new InjectionToken<LoggingConfig>("LoggerServiceConfig");

// Real logger class that processes the logging requests.
export class DefaultLogger implements Logger {

  private _observer: Observer<LoggerMessage>;
  private readonly _name: string;
  private readonly _level: LoggerLevel;
  private readonly _appender: LoggerAppender;
  private readonly _parent: Logger;

  constructor(name: string, level: LoggerLevel, appender: LoggerAppender, parent: Logger) {
    this._name = (name).replace("..", ".");
    this._level = level;
    this._appender = appender;
    this._parent = parent;

    let stream = new Observable<LoggerMessage>(observer => this._observer = observer).pipe(
      filter(message => message != null),
      filter(message => message.level >= this.level),
      filter(message => message.level !== LoggerLevel.OFF)
    );
    if (appender != null) {
      stream.subscribe(appender);
    }
  }

  get name(): string {
    return this._name;
  }

  get level(): LoggerLevel {
    return this._level;
  }

  get appender(): LoggerAppender {
    return this._appender;
  }

  get parent(): Logger {
    return this._parent;
  }

  public log(message: LoggerMessage) {
    if (this._parent != null)
      this._parent.log(message);
    if(this._appender != null)
      this._observer.next(message);
  }

  error(message: string, cause?: Error) {
    this.log(new LoggerMessage(LoggerLevel.ERROR, message, this, cause));
  }

  fatal(message: string, cause?: Error) {
    this.log(new LoggerMessage(LoggerLevel.FATAL, message, this, cause));
  }

  info(message: string, cause?: Error) {
    this.log(new LoggerMessage(LoggerLevel.INFO, message, this, cause));
  }

  trace(message: string, cause?: Error) {
    this.log(new LoggerMessage(LoggerLevel.TRACE, message, this, cause));
  }

  warn(message: string, cause?: Error) {
    this.log(new LoggerMessage(LoggerLevel.WARN, message, this, cause));
  }
}

// Logger class that will process the log.
@Injectable()
export class LoggerService {

  private static readonly ROOT_NAME: string = "<root>";

  private config: LoggingConfig;
  private appenders: Map<string, LoggerAppender> = new Map<string, LoggerAppender>();
  private loggers: Map<string, Logger> = new Map<string, Logger>();

  constructor(@Inject(LoggerConfigToken) config: LoggingConfig) {
    this.config = config;
    // Prepare appender objects
    Object.keys(this.config.appenders).forEach(key => {
      let config = this.config.appenders[ key ];
      let appender = config.appender;
      appender.config = config;
      this.appenders.set(key, appender);
    });
    // root logger
    let root_config = config.loggers[ LoggerService.ROOT_NAME ];
    this.loggers.set(LoggerService.ROOT_NAME,
      root_config != null
        ? new DefaultLogger(LoggerService.ROOT_NAME, root_config.level, this.appenders.get(root_config.appender), null)
        : new DefaultLogger(LoggerService.ROOT_NAME, LoggerLevel.ERROR, new BrowserConsoleAppender(), null)
    );
  }

  public getLogger(name: string): Logger {
    // Ensure root is part of logger hierarchy
    let canonicalName = name.startsWith(LoggerService.ROOT_NAME) ? name : LoggerService.ROOT_NAME + "." + name;
    let cachedLogger = this.loggers.get(canonicalName);
    if (cachedLogger != null)
      return cachedLogger;

    // Build new logger
    let parent = this.getLogger(canonicalName.substring(0, canonicalName.lastIndexOf('.')));
    let config = this.config.loggers[ canonicalName ];
    let logger = config != null
      ? new DefaultLogger(canonicalName, config.level, this.appenders.get(config.appender), parent)
      : new DefaultLogger(canonicalName, parent.level, parent.appender, parent);
    this.loggers.set(canonicalName, logger);
    return logger;
  }

  public getRoot(): Logger {
    return this.getLogger(LoggerService.ROOT_NAME);
  }
}
