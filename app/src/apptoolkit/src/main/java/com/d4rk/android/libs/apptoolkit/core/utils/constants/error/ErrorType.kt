package com.d4rk.android.libs.apptoolkit.core.utils.constants.error

/**
 * @brief Enum class representing different types of errors that can occur during program execution.
 *
 * This enum provides a standardized way to identify and handle various error conditions,
 * making it easier to debug and manage errors in a consistent manner.
 *
 * Each enumerator represents a specific error scenario and should be used to
 * clearly indicate the cause of a failure or exception.
 *
 * @note Adding new error types requires updating the error handling logic in the program.
 *
 * @enum ErrorType
 * @brief Enumeration of possible error types.
 *
 * @var SECURITY_EXCEPTION
 * @brief An error related to security, such as unauthorized access or permission issues.
 *
 * @var IO_EXCEPTION
 * @brief An error related to input/output operations, such as file access issues, network errors, or stream corruption.
 *
 * @var ACTIVITY_NOT_FOUND
 * @brief An error indicating that a requested activity, resource, or component could not be found.
 *
 * @var ILLEGAL_ARGUMENT
 * @brief An error caused by an invalid or inappropriate argument passed to a function or method.
 *
 * @var SQLITE_EXCEPTION
 * @brief An error specific to SQLite database operations, such as query failures or database corruption.
 *
 * @var FILE_NOT_FOUND
 * @brief An error indicating that a specified file could not be located.
 *
 * @var UNKNOWN_ERROR
 * @brief A generic error that doesn't fit into the other defined categories. Should be avoided if possible,
 *        and more specific error types should be created instead to provide better error context.
 */
enum class ErrorType {
    SECURITY_EXCEPTION , IO_EXCEPTION , ACTIVITY_NOT_FOUND , ILLEGAL_ARGUMENT , SQLITE_EXCEPTION , FILE_NOT_FOUND , UNKNOWN_ERROR ,
}