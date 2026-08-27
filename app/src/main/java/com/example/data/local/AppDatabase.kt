package com.example.data.local

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RoomDatabase
import androidx.room.Update
import com.example.model.BoothChecklistItem
import com.example.model.MCCViolationReport
import com.example.model.VoterReceipt
import kotlinx.coroutines.flow.Flow

@Dao
interface MCCViolationDao {
    @Query("SELECT * FROM mcc_violation_reports ORDER BY timestamp DESC")
    fun getAllReports(): Flow<List<MCCViolationReport>>

    @Query("SELECT * FROM mcc_violation_reports WHERE trackingToken = :token LIMIT 1")
    suspend fun getReportByToken(token: String): MCCViolationReport?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReport(report: MCCViolationReport)

    @Update
    suspend fun updateReport(report: MCCViolationReport)
}

@Dao
interface VoterReceiptDao {
    @Query("SELECT * FROM voter_receipts ORDER BY timestamp DESC")
    fun getAllReceipts(): Flow<List<VoterReceipt>>

    @Query("SELECT * FROM voter_receipts WHERE tokenNumber = :token LIMIT 1")
    suspend fun getReceiptByToken(token: String): VoterReceipt?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReceipt(receipt: VoterReceipt)
}

@Dao
interface BoothChecklistDao {
    @Query("SELECT * FROM booth_checklist_items")
    fun getAllItems(): Flow<List<BoothChecklistItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<BoothChecklistItem>)

    @Update
    suspend fun updateItem(item: BoothChecklistItem)
}

@Database(
    entities = [
        MCCViolationReport::class,
        VoterReceipt::class,
        BoothChecklistItem::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun mccViolationDao(): MCCViolationDao
    abstract fun voterReceiptDao(): VoterReceiptDao
    abstract fun boothChecklistDao(): BoothChecklistDao
}
