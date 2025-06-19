package com.d4rk.android.libs.apptoolkit.app.main.domain.usecases

import android.app.Activity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.DataState
import com.d4rk.android.libs.apptoolkit.core.domain.model.network.Errors
import com.d4rk.android.libs.apptoolkit.core.domain.usecases.Repository
import com.d4rk.android.libs.apptoolkit.core.utils.extensions.toError
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class PerformInAppUpdateUseCase(private val appUpdateManager : AppUpdateManager , private val updateResultLauncher : ActivityResultLauncher<IntentSenderRequest>) : Repository<Unit , Flow<DataState<Int , Errors>>> {

    override suspend fun invoke(param : Unit) : Flow<DataState<Int , Errors>> = flow {
        runCatching {
            val appUpdateInfo : AppUpdateInfo = appUpdateManager.appUpdateInfo.await()
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {/*
                val stalenessDays : Int = appUpdateInfo.clientVersionStalenessDays() ?: 0
                val updateType : Int = if (stalenessDays > 90) {
                    AppUpdateType.IMMEDIATE
                }
                else {
                    AppUpdateType.IMMEDIATE
                }
                */
                val updateType : Int = AppUpdateType.IMMEDIATE
                val appUpdateOptions : AppUpdateOptions = AppUpdateOptions.newBuilder(updateType).build()
                val didStart : Boolean = appUpdateManager.startUpdateFlowForResult(
                    appUpdateInfo , updateResultLauncher , appUpdateOptions
                )
                if (didStart) return@runCatching Activity.RESULT_OK
            }
            Activity.RESULT_CANCELED
        }.onSuccess { result : Int ->
            emit(value = DataState.Success(data = result))
        }.onFailure { throwable ->
            emit(value = DataState.Error(error = throwable.toError(default = Errors.UseCase.FAILED_TO_UPDATE_APP)))
        }
    }
}