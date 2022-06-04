package com.epam.esm.exception;


/**
 * The type Error code.
 *
 * @author Khabibjonov Farrukhbek
 * @project rest-api-advanced
 * <p>
 * This class includes set of constants with error codes.
 */
public final class ErrorCodeConstraints {
    /**
     * The constant NOT_FOUND_CODE.
     */
    public static final int NOT_FOUND_CODE = 40401;
    /**
     * The constant NOT_ACCEPTABLE_CODE.
     */
    public static final int NOT_ACCEPTABLE_CODE = 40601;
    /**
     * The constant BAD_REQUEST_CODE.
     */
    public static final int BAD_REQUEST_CODE = 40001;
    /**
     * The constant CONFLICT_CODE.
     */
    public static final int CONFLICT_CODE = 40901;
    /**
     * The constant INTERNAL_CODE.
     */
    public static final int INTERNAL_CODE = 50001;
    /**
     * No arg constructor.
     */
    private ErrorCodeConstraints() {
    }
}
