import { LoggerMessage } from "./LoggerMessage";

export interface LoggerFormatter {
    (message: LoggerMessage) : string;
}
