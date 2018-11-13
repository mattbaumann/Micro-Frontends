import { LoggerLevel } from "./LoggerLevel";
import { LoggerMessage } from "./LoggerMessage";
import { LoggingAppenderConfig } from "./LoggerConfig";
import { IllegalArgumentError } from "../utilities/IllegalArgumentError";
import { LoggerFormatter } from "./LoggerFormatter";
import { NextObserver } from "rxjs";

export abstract class LoggerAppender implements NextObserver<LoggerMessage> {

    public static JSON_FORMATTER : LoggerFormatter = msg => JSON.stringify(msg);
    public static COLOR_FORMATTER : LoggerFormatter = msg =>
      `${msg.timestamp.toISOString()} [${LoggerAppender.formatLevel(msg.level)}] ${msg.logger.name} ${msg.message} ${msg.cause != null ? "\n" + msg.cause : ''}`;

    public static formatLevel(level : LoggerLevel) : string {
      switch (level) {
        case LoggerLevel.TRACE: return "TRACE";
        case LoggerLevel.INFO: return "INFO";
        case LoggerLevel.WARN: return "WARN";
        case LoggerLevel.ERROR: return "ERROR";
        case LoggerLevel.FATAL: return "FATAL";
        default: throw new IllegalArgumentError("OFF state cannot be formatted.");
      }
    }

    /**
     * Holds the configuration of the appender. It's set before the
     * first appending.
     */
    _config : LoggingAppenderConfig;

    get config() : LoggingAppenderConfig {
        return this._config;
    }

    set config(config : LoggingAppenderConfig) {
        this.validate(config);
        this._config = config;
    }

    next: (value: LoggerMessage) => void = (value) => this.append(value);

    protected abstract append(message: LoggerMessage);
    protected abstract validate(config : LoggingAppenderConfig);
}

export class ServerPutAppender extends LoggerAppender {

    private request : XMLHttpRequest = new XMLHttpRequest();

    constructor() {
        super();
        this.request.timeout = 1000; // ms
        this.request.ontimeout = (ev) => {
            console.error("Failed to send Logging message because of timeout");
            ev.stopPropagation();
        };
    }

    validate(config : LoggingAppenderConfig) {
        if(config.config["url"] == null) {
            throw new IllegalArgumentError("ServerPutAppender must have url defined");
        }
        if(config.config["method"] == null) {
            throw new IllegalArgumentError("ServerPutAppender must have request method defined");
        }
    }

    protected append(message: LoggerMessage) {
        if (message.level == LoggerLevel.OFF)
            throw new IllegalArgumentError("Must not receive message with OFF level.");
        this.request.open(this._config.config["method"], this._config.config["url"]);
        this.request.send();
    }
}

export class BrowserConsoleAppender extends LoggerAppender {

    validate(_config : LoggingAppenderConfig) {}

    protected append(message: LoggerMessage) {
        var msgText = (this._config.formatter || ((m) => m))(message);
        switch (message.level) {
            case LoggerLevel.OFF:
                throw new IllegalArgumentError("Must not receive message with OFF level.");
            case LoggerLevel.TRACE:
                console.trace(msgText);
                break;
                case LoggerLevel.INFO:
                console.info(msgText);
                break;
                case LoggerLevel.WARN:
                console.warn(msgText);
                break;
                case LoggerLevel.ERROR:
                console.error(msgText);
                break;
                case LoggerLevel.FATAL:
                console.error(msgText);
                break;
            default:
                throw new IllegalArgumentError("Must not receive message with unknown level.");
        }
    }
}
