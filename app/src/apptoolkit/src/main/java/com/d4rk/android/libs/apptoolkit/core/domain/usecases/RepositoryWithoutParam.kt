package com.d4rk.android.libs.apptoolkit.core.domain.usecases

/**
 * Represents a repository that fetches data without requiring any input parameters.
 *
 * This interface defines a single `invoke` operator function that, when called,
 * performs the data retrieval operation and returns the result of type [R].
 *
 * It's designed for scenarios where the data to be fetched is globally accessible
 * or determined by the repository's internal logic, without external inputs.
 *
 * @param R The type of the data returned by the repository.
 */
interface RepositoryWithoutParam<R> {
    suspend operator fun invoke() : R
}