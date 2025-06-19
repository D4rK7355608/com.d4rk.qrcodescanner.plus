package com.d4rk.qrcodescanner.plus.core.domain.model.network

import com.d4rk.android.libs.apptoolkit.core.domain.model.network.Error

sealed interface Errors : Error {

    enum class Network : Errors {
        REQUEST_TIMEOUT , NO_INTERNET , SERIALIZATION
    }

    enum class UseCase : Errors {
        NO_DATA , ILLEGAL_ARGUMENT ,
    }

    enum class Database : Errors {
        DATABASE_OPERATION_FAILED
    }
}
