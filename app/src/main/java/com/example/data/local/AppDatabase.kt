package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.model.AdminAuditLog
import com.example.data.model.AppSetting
import com.example.data.model.CoinTransaction
import com.example.data.model.GiftCardInventory
import com.example.data.model.NotificationItem
import com.example.data.model.Offer
import com.example.data.model.OfferEvent
import com.example.data.model.OfferProvider
import com.example.data.model.Referral
import com.example.data.model.SupportTicket
import com.example.data.model.UserProfile
import com.example.data.model.UserReward
import com.example.data.model.Wallet
import com.example.data.model.WithdrawalRequest

@Database(
    entities = [
        UserProfile::class,
        Wallet::class,
        CoinTransaction::class,
        OfferProvider::class,
        Offer::class,
        OfferEvent::class,
        Referral::class,
        WithdrawalRequest::class,
        GiftCardInventory::class,
        UserReward::class,
        NotificationItem::class,
        SupportTicket::class,
        AppSetting::class,
        AdminAuditLog::class,
        com.example.data.model.GameItem::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun walletDao(): WalletDao
    abstract fun transactionDao(): TransactionDao
    abstract fun offerDao(): OfferDao
    abstract fun gameDao(): GameDao
    abstract fun referralDao(): ReferralDao
    abstract fun withdrawalDao(): WithdrawalDao
    abstract fun giftCardDao(): GiftCardDao
    abstract fun rewardDao(): RewardDao
    abstract fun notificationDao(): NotificationDao
    abstract fun supportDao(): SupportDao
    abstract fun settingsDao(): SettingsDao
    abstract fun auditDao(): AuditDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "reward_earn_3.db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
