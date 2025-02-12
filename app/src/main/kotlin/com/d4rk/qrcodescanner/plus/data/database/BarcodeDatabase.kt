package com.d4rk.qrcodescanner.plus.data.database

import android.content.Context
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.d4rk.qrcodescanner.plus.data.model.Barcode
import com.d4rk.qrcodescanner.plus.data.model.ExportBarcode
import com.d4rk.qrcodescanner.plus.model.schema.BarcodeSchema
import com.google.zxing.BarcodeFormat
// Removed: import io.reactivex.Completable
// Removed: import io.reactivex.Single

class BarcodeDatabaseTypeConverter {
    @TypeConverter
    fun fromBarcodeFormat(barcodeFormat: BarcodeFormat): String {
        return barcodeFormat.name
    }

    @TypeConverter
    fun toBarcodeFormat(value: String): BarcodeFormat {
        return BarcodeFormat.valueOf(value)
    }

    @TypeConverter
    fun fromBarcodeSchema(barcodeSchema: BarcodeSchema): String {
        return barcodeSchema.name
    }

    @TypeConverter
    fun toBarcodeSchema(value: String): BarcodeSchema {
        return BarcodeSchema.valueOf(value)
    }
}

@Database(entities = [Barcode::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getBarcodeDatabase(): BarcodeDatabase
}

@Dao
interface BarcodeDatabase {
    companion object {
        private var INSTANCE: BarcodeDatabase? = null
        fun getInstance(context: Context): BarcodeDatabase {
            return INSTANCE ?: Room.databaseBuilder(
                context.applicationContext ,
                AppDatabase::class.java ,
                "db"
            ).addMigrations(object : Migration(1, 2) {
                override fun migrate(database: SupportSQLiteDatabase) {
                    database.execSQL("ALTER TABLE codes ADD COLUMN name TEXT")
                }
            }).build().getBarcodeDatabase().apply {
                INSTANCE = this
            }
        }
    }

    @Query("SELECT * FROM codes ORDER BY date DESC")
    fun getAll(): PagingSource<Int, Barcode>

    @Query("SELECT * FROM codes WHERE isFavorite = 1 ORDER BY date DESC")
    fun getFavorites(): PagingSource<Int, Barcode>

    @Query("SELECT date, format, text FROM codes ORDER BY date DESC")
    suspend fun getAllForExport(): List<ExportBarcode>

    @Query("SELECT * FROM codes WHERE format = :format AND text = :text LIMIT 1")
    suspend fun find(format: String, text: String): List<Barcode>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(barcode: Barcode): Long

    @Query("DELETE FROM codes WHERE id = :id")
    suspend fun delete(id: Long)

    @Query("DELETE FROM codes")
    suspend fun deleteAll()
}

// Extension functions for BarcodeDatabase using coroutines
suspend fun BarcodeDatabase.save(barcode: Barcode , doNotSaveDuplicates: Boolean): Long {
    return if (doNotSaveDuplicates) {
        saveIfNotPresent(barcode)
    } else {
        save(barcode)
    }
}

suspend fun BarcodeDatabase.saveIfNotPresent(barcode: Barcode): Long {
    val found = find(barcode.format.name, barcode.text)
    return if (found.isEmpty()) {
        save(barcode)
    } else {
        found[0].id
    }
}
