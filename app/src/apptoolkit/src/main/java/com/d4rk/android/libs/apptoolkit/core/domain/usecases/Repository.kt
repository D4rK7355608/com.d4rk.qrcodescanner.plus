package com.d4rk.android.libs.apptoolkit.core.domain.usecases

/**
 * Interface representing a repository that fetches data of type [R] based on a parameter of type [T].
 *
 * This interface is designed for operations that involve retrieving data from a source,
 * such as a database, network, or cache, based on some input criteria.
 *
 * **Primarily intended for use by Use Cases**, this interface acts as an abstraction
 * layer between the data source and the Use Case, ensuring the Use Case remains
 * independent of the specific data retrieval mechanism.
 *
 * The primary function is the `invoke` operator, which provides a concise way to
 * execute the data fetching operation.  This allows for easy invocation like a function: `val result = myRepository(param)`.
 *
 * @param T The type of the parameter used to fetch the data.
 * @param R The type of the data being fetched.
 */
interface Repository<T , R> {
    suspend operator fun invoke(param : T) : R
}