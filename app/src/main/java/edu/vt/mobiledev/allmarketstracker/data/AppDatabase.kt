package edu.vt.mobiledev.allmarketstracker.data

import android.content.Context
import androidx.room.*
import androidx.room.RoomDatabase
import edu.vt.mobiledev.allmarketstracker.model.PortfolioTransaction
import edu.vt.mobiledev.allmarketstracker.model.Converters

@Database(
    entities = [PortfolioTransaction::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun portfolioDao(): PortfolioDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "crypto_portfolio_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
