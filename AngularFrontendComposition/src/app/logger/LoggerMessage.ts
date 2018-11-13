import {LoggerLevel} from './LoggerLevel';
import {Logger} from './Logger';

export class LoggerMessage {
    constructor(public level : LoggerLevel, public message : string, public logger: Logger, public cause?: Error, public timestamp = new Date()) {}
}
