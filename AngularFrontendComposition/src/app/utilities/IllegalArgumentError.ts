
/**
 * Error thrown to indicate failed precondition, about the argument.
 */
export class IllegalArgumentError extends RangeError {
    constructor(message : string = "") {
        super(message);
    }
}