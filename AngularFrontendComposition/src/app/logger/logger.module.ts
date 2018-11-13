import { NgModule, ModuleWithProviders } from '@angular/core';
import { LoggerService, LoggerConfigToken } from './LoggerService';
import { LoggingConfig } from './LoggerConfig';

@NgModule()
export class LoggerModule {
    static forRoot(config: LoggingConfig) : ModuleWithProviders {
      return {
        ngModule: LoggerModule,
        providers: [
          LoggerService,
          {
            provide: LoggerConfigToken,
            useValue: config
          }
        ]
      };
    }
}
