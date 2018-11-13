import { NgModule } from '@angular/core';
import { LoggingConfig } from './logger/LoggerConfig';
import { LoggerModule } from './logger/logger.module';
import { BrowserConsoleAppender, LoggerAppender } from './logger/LoggerAppender';
import { LoggerLevel } from './logger/LoggerLevel';


const loggingConfig: LoggingConfig = {
    appenders: {
        "default": {
            appender: new BrowserConsoleAppender(),
            formatter: LoggerAppender.COLOR_FORMATTER
        }
    },
    loggers: {
        "<root>": {
            appender: "default",
            level: LoggerLevel.TRACE
        },
      "<root>.recipe": {
          level: LoggerLevel.WARN
      },
      "<root>.recipe.list": {
          level: LoggerLevel.TRACE
      }
    }
};

@NgModule({
  imports: [LoggerModule.forRoot(loggingConfig)],
  exports: [LoggerModule]
})
export class AppLoggingModule { }
