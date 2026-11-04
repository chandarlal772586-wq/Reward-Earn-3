package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
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
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE id = :userId LIMIT 1")
    fun getUserFlow(userId: String): Flow<UserProfile?>

    @Query("SELECT * FROM users WHERE id = :userId LIMIT 1")
    suspend fun getUser(userId: String): UserProfile?

    @Query("SELECT * FROM users WHERE referralCode = :code LIMIT 1")
    suspend fun getUserByReferralCode(code: String): UserProfile?

    @Query("SELECT * FROM users ORDER BY createdAt DESC")
    fun getAllUsersFlow(): Flow<List<UserProfile>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserProfile)

    @Update
    suspend fun updateUser(user: UserProfile)

    @Query("SELECT COUNT(*) FROM users")
    fun getUserCountFlow(): Flow<Int>
}

@Dao
interface WalletDao {
    @Query("SELECT * FROM wallets WHERE userId = :userId LIMIT 1")
    fun getWalletFlow(userId: String): Flow<Wallet?>

    @Query("SELECT * FROM wallets WHERE userId = :userId LIMIT 1")
    suspend fun getWallet(userId: String): Wallet?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWallet(wallet: Wallet)

    @Update
    suspend fun updateWallet(wallet: Wallet)

    @Query("SELECT SUM(totalCoins) FROM wallets")
    fun getTotalCoinsCreditedFlow(): Flow<Long?>

    @Query("SELECT SUM(redeemedCoins) FROM wallets")
    fun getTotalCoinsRedeemedFlow(): Flow<Long?>
}

@Dao
interface TransactionDao {
    @Query("SELECT * FROM coin_transactions WHERE userId = :userId ORDER BY timestamp DESC")
    fun getUserTransactionsFlow(userId: String): Flow<List<CoinTransaction>>

    @Query("SELECT * FROM coin_transactions ORDER BY timestamp DESC")
    fun getAllTransactionsFlow(): Flow<List<CoinTransaction>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(txn: CoinTransaction)
}

@Dao
interface OfferDao {
    @Query("SELECT * FROM offer_providers ORDER BY name ASC")
    fun getAllProvidersFlow(): Flow<List<OfferProvider>>

    @Query("SELECT * FROM offer_providers WHERE isEnabled = 1 ORDER BY name ASC")
    fun getEnabledProvidersFlow(): Flow<List<OfferProvider>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProviders(providers: List<OfferProvider>)

    @Update
    suspend fun updateProvider(provider: OfferProvider)

    @Query("SELECT * FROM offers ORDER BY rewardCoins DESC")
    fun getAllOffersFlow(): Flow<List<Offer>>

    @Query("SELECT * FROM offers WHERE providerId = :providerId")
    fun getOffersByProviderFlow(providerId: String): Flow<List<Offer>>

    @Query("SELECT * FROM offers WHERE id = :offerId LIMIT 1")
    suspend fun getOfferById(offerId: String): Offer?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOffers(offers: List<Offer>)

    @Update
    suspend fun updateOffer(offer: Offer)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOfferEvent(event: OfferEvent)

    @Query("SELECT * FROM offer_events WHERE userId = :userId ORDER BY clickTimestamp DESC")
    fun getUserOfferEventsFlow(userId: String): Flow<List<OfferEvent>>

    @Query("SELECT * FROM offer_events WHERE id = :eventId LIMIT 1")
    suspend fun getOfferEventById(eventId: String): OfferEvent?

    @Update
    suspend fun updateOfferEvent(event: OfferEvent)
}

@Dao
interface ReferralDao {
    @Query("SELECT * FROM referrals WHERE referrerUserId = :userId ORDER BY timestamp DESC")
    fun getUserReferralsFlow(userId: String): Flow<List<Referral>>

    @Query("SELECT COUNT(*) FROM referrals WHERE referrerUserId = :userId")
    fun getReferralCountFlow(userId: String): Flow<Int>

    @Query("SELECT COUNT(*) FROM referrals WHERE referrerUserId = :userId AND status = 'COMPLETED'")
    fun getSuccessfulReferralCountFlow(userId: String): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReferral(referral: Referral)

    @Update
    suspend fun updateReferral(referral: Referral)
}

@Dao
interface WithdrawalDao {
    @Query("SELECT * FROM withdrawal_requests WHERE userId = :userId ORDER BY requestTimestamp DESC")
    fun getUserWithdrawalsFlow(userId: String): Flow<List<WithdrawalRequest>>

    @Query("SELECT * FROM withdrawal_requests ORDER BY requestTimestamp DESC")
    fun getAllWithdrawalsFlow(): Flow<List<WithdrawalRequest>>

    @Query("SELECT * FROM withdrawal_requests WHERE id = :id LIMIT 1")
    suspend fun getWithdrawalById(id: String): WithdrawalRequest?

    @Query("SELECT COUNT(*) FROM withdrawal_requests WHERE status = 'PENDING'")
    fun getPendingCountFlow(): Flow<Int>

    @Query("SELECT COUNT(*) FROM withdrawal_requests WHERE status = 'COMPLETED'")
    fun getCompletedCountFlow(): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWithdrawal(request: WithdrawalRequest)

    @Update
    suspend fun updateWithdrawal(request: WithdrawalRequest)
}

@Dao
interface GiftCardDao {
    @Query("SELECT * FROM gift_card_inventory ORDER BY id DESC")
    fun getAllCardsFlow(): Flow<List<GiftCardInventory>>

    @Query("SELECT * FROM gift_card_inventory WHERE status = 'AVAILABLE' AND brand = :brand AND denominationInr = :denomination LIMIT 1")
    suspend fun getAvailableCard(brand: String, denomination: Int): GiftCardInventory?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCards(cards: List<GiftCardInventory>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCard(card: GiftCardInventory)

    @Update
    suspend fun updateCard(card: GiftCardInventory)
}

@Dao
interface RewardDao {
    @Query("SELECT * FROM user_rewards WHERE userId = :userId ORDER BY deliveredTimestamp DESC")
    fun getUserRewardsFlow(userId: String): Flow<List<UserReward>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReward(reward: UserReward)
}

@Dao
interface NotificationDao {
    @Query("SELECT * FROM notifications WHERE userId = :userId ORDER BY timestamp DESC")
    fun getUserNotificationsFlow(userId: String): Flow<List<NotificationItem>>

    @Query("SELECT COUNT(*) FROM notifications WHERE userId = :userId AND isRead = 0")
    fun getUnreadCountFlow(userId: String): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotification(notification: NotificationItem)

    @Query("UPDATE notifications SET isRead = 1 WHERE userId = :userId")
    suspend fun markAllAsRead(userId: String)
}

@Dao
interface SupportDao {
    @Query("SELECT * FROM support_tickets WHERE userId = :userId ORDER BY createdTimestamp DESC")
    fun getUserTicketsFlow(userId: String): Flow<List<SupportTicket>>

    @Query("SELECT * FROM support_tickets ORDER BY createdTimestamp DESC")
    fun getAllTicketsFlow(): Flow<List<SupportTicket>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTicket(ticket: SupportTicket)

    @Update
    suspend fun updateTicket(ticket: SupportTicket)
}

@Dao
interface SettingsDao {
    @Query("SELECT * FROM app_settings")
    fun getAllSettingsFlow(): Flow<List<AppSetting>>

    @Query("SELECT value FROM app_settings WHERE `key` = :key LIMIT 1")
    suspend fun getSettingValue(key: String): String?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSettings(settings: List<AppSetting>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun setSetting(setting: AppSetting)
}

@Dao
interface AuditDao {
    @Query("SELECT * FROM admin_audit_logs ORDER BY timestamp DESC")
    fun getAllAuditLogsFlow(): Flow<List<AdminAuditLog>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLog(log: AdminAuditLog)
}

@Dao
interface GameDao {
    @Query("SELECT * FROM game_items ORDER BY rewardCoins ASC")
    fun getAllGamesFlow(): Flow<List<com.example.data.model.GameItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGames(games: List<com.example.data.model.GameItem>)

    @Query("SELECT * FROM game_items WHERE id = :id LIMIT 1")
    suspend fun getGameById(id: String): com.example.data.model.GameItem?

    @androidx.room.Update
    suspend fun updateGame(game: com.example.data.model.GameItem)
}
