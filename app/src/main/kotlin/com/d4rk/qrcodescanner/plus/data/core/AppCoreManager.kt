@file:Suppress("DEPRECATION")

package com.d4rk.qrcodescanner.plus.data.core

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.database.sqlite.SQLiteException
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.multidex.MultiDexApplication
import androidx.room.Room
import androidx.room.migration.Migration
import com.d4rk.android.libs.apptoolkit.data.client.KtorClient
import com.d4rk.qrcodescanner.plus.data.core.ads.AdsCoreManager
import com.d4rk.qrcodescanner.plus.data.core.datastore.DataStoreCoreManager
import com.d4rk.qrcodescanner.plus.data.database.AppDatabase
import com.d4rk.qrcodescanner.plus.data.datastore.DataStore
import com.d4rk.qrcodescanner.plus.utils.error.ErrorHandler.handleInitializationFailure
import io.ktor.client.HttpClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope

class AppCoreManager : MultiDexApplication() , Application.ActivityLifecycleCallbacks , LifecycleObserver {

    private val dataStoreCoreManager : DataStoreCoreManager by lazy {
        DataStoreCoreManager(context = this)
    }

    private val adsCoreManager : AdsCoreManager by lazy {
        AdsCoreManager(context = this)
    }

    private var currentActivity : Activity? = null

    override fun onCreate() {
        super.onCreate()
        instance = this
        registerActivityLifecycleCallbacks(this)
        ProcessLifecycleOwner.get().lifecycle.addObserver(observer = this)
        CoroutineScope(context = Dispatchers.IO).launch {
            initializeApp()
        }
    }

    private suspend fun initializeApp() = supervisorScope  {
        val ktor : Deferred<Unit> = async { initializeKtorClient() }
        val dataBase : Deferred<Unit> = async { initializeDatabase() }
        val dataStore : Deferred<Unit> = async { initializeDataStore() }

        ktor.await()
        dataBase.await()
        dataStore.await()

        adsCoreManager.initializeAds()

        initializeAds()
        finalizeInitialization()
    }

    private suspend fun initializeKtorClient() {
        runCatching {
            coroutineScope {
                val tasks : List<Deferred<Unit>> =
                        listOf(element = async(context = Dispatchers.IO) {
                            ktorClient = KtorClient().createClient()
                        })
                tasks.awaitAll()
            }
        }.onFailure {
            handleInitializationFailure(
                message = "Ktor client initialization failed" ,
                exception = it as Exception ,
                applicationContext = applicationContext
            )
        }
    }

    private suspend fun initializeDatabase() {
        runCatching {
            database = Room.databaseBuilder(
                context = this@AppCoreManager,
                klass = AppDatabase::class.java,
                name = "Android Studio Tutorials"
            )
                    //.addMigrations(migrations = getMigrations())
                    .fallbackToDestructiveMigration()
                    .fallbackToDestructiveMigrationOnDowngrade()
                    .build()

            database.openHelper.writableDatabase
        }.onFailure {
            handleDatabaseError(exception = it as Exception)
        }
    }

/*
    private fun getMigrations() : Array<Migration> {
        return arrayOf(
            MIGRATION_1_2 ,
            MIGRATION_2_3 ,
        )
    }
*/

    private suspend fun initializeDataStore() {
        runCatching {
            dataStore = DataStore.getInstance(context = this@AppCoreManager)
            dataStoreCoreManager.initializeDataStore()
        }.onFailure {
            handleInitializationFailure(
                message = "DataStore initialization failed" ,
                exception = it as Exception ,
                applicationContext = applicationContext
            )
        }
    }

    private fun initializeAds() {
        runCatching {
            adsCoreManager.initializeAds()
        }.onFailure {
            handleInitializationFailure(
                message = "Ads initialization failed" ,
                exception = it as Exception ,
                applicationContext = applicationContext
            )
        }
    }

    private fun finalizeInitialization() {
        markAppAsLoaded()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onMoveToForeground() {
        currentActivity?.let { adsCoreManager.showAdIfAvailable(activity = it) }
    }

    private suspend fun handleDatabaseError(exception : Exception) {
        if (exception is SQLiteException || (exception is IllegalStateException && exception.message?.contains(other = "Migration failed") == true)) {
            eraseDatabase()
        }
    }

    private suspend fun eraseDatabase() {
        runCatching {
            deleteDatabase("Android Studio Tutorials")
        }.onSuccess {
            initializeDatabase()
        }.onFailure {
            logDatabaseError(exception = it as Exception)
        }
    }

    private fun logDatabaseError(exception : Exception) {
        Log.e("AppCoreManager" , "Database error: ${exception.message}" , exception)
    }

    private fun markAppAsLoaded() {
        isAppLoaded = true
    }

    fun isAppLoaded() : Boolean {
        return isAppLoaded
    }

    override fun onActivityCreated(activity : Activity , savedInstanceState : Bundle?) {}

    override fun onActivityStarted(activity : Activity) {
        if (! adsCoreManager.isShowingAd) {
            currentActivity = activity
        }
    }

    override fun onActivityResumed(activity : Activity) {}
    override fun onActivityPaused(activity : Activity) {}
    override fun onActivityStopped(activity : Activity) {}
    override fun onActivitySaveInstanceState(activity : Activity , outState : Bundle) {}
    override fun onActivityDestroyed(activity : Activity) {}

    companion object {

        lateinit var dataStore : DataStore
            private set

        lateinit var database : AppDatabase
            private set

        @SuppressLint("StaticFieldLeak")
        lateinit var instance : AppCoreManager
            private set

        lateinit var ktorClient : HttpClient
            private set

        var isAppLoaded : Boolean = false
            private set
    }
}