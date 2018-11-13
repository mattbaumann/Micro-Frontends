import { IllegalArgumentError } from "./IllegalArgumentError";

/**
 * Validates that a number is greater or equal then the min value.
 * @param arg The argument to test
 * @param min The minimal allowed value
 * @param text Custom exception cause
 */
export function min(arg : number, min : number, text? : string) {
    if(arg < min) {
        throw new IllegalArgumentError(text || `Precondition min failed as argument (${arg}) < min (${max}).`)
    }
}

/**
 * Validates that a number is smaller or equal then the maxmum allowed value.
 * @param arg The argument to test
 * @param max The maximal allowed value
 * @param text Custom exception cause
 */
export function max(arg : number, max : number, text? : string ) {
    if(arg > max) {
        throw new IllegalArgumentError(text || `Precondition max failed as argument (${arg}) > max (${max}).`)
    }
}

/**
 * Validates that a number is positive. This test is without zero.
 * @param arg The argument to test
 * @param text Custom exception cause
 */
export function positive(arg: number, text? : string) {
    min(arg, 1, text || `Precondition 'positive number' failed as argument (${arg}) is negative`);
}

/**
 * Validates that a number is positive or zero.
 * @param arg The argument to test
 * @param text Custom exception cause
 */
export function positiveOrZero(arg: number, text? : string) {
    min(arg, 0, text || `Precondition 'positive number' failed as argument (${arg}) is negative`);
}

/**
 * Validates that a boolean is set (true).
 * @param arg The argument to test
 * @param text Custom exception cause
 */
export function set(arg: boolean, text? : string) {
    if(!arg) {
        throw new IllegalArgumentError(text || "Precondition set boolean failed as argument was false.")
    }
}

/**
 * Validates that a boolean is unset (false).
 * @param arg The argument to test
 * @param text Custom exception cause
 */
export function unset(arg: boolean, text? : string) {
    if(arg) {
        throw new IllegalArgumentError(text || "Precondition unset boolean failed as argument was true.")
    }
}

/**
 * Validates that an object is not null nor undefined.
 * @param arg The argument to test
import { IllegalArgumentError } from './IllegalArgumentError';

 * @param text Custom exception cause
 */
export function defined(arg: any, text? : string) {
    if(arg == null) {
        throw new IllegalArgumentError(text || "Precondition defined object failed as argument was undefined or null.")
    }
}

