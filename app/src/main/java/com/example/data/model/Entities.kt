package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserProfile(
    @PrimaryKey val id: String,
    val displayName: String,
    val email: String,
    val profilePhotoUrl: String = "",
    val referralCode: String,
    val referredBy: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val status: String = "ACTIVE", // ACTIVE, PENDING_VERIFICATION, SUSPENDED
    val isVerified: Boolean = true,
    val role: String = "USER", // USER, ADMIN
    val isGoogleLinked: Boolean = true
)

@Entity(tableName = "wallets")
data class Wallet(
    @PrimaryKey val userId: String,
    val totalCoins: Long = 0,
    val availableCoins: Long = 0,
    val pendingCoins: Long = 0,
    val redeemedCoins: Long = 0,
    val todayEarnings: Long = 0,
    val todayVideosWatched: Int = 0,
    val lastVideoDate: String = "",
    val lastUpdated: Long = System.currentTimeMillis()
)

@Entity(tableName = "coin_transactions")
data class CoinTransaction(
    @PrimaryKey val id: String,
    val userId: String,
    val type: String, // CREDIT, DEBIT, RESERVE, REFUND
    val source: String, // SUPER_OFFER, PLAY_GAMES, REFERRAL, REWARDED_VIDEO, REDEMPTION_RESERVE, REDEMPTION_REFUND, ADMIN_ADJUSTMENT, SIGNUP_BONUS
    val coinAmount: Long,
    val inrAmount: Double,
    val status: String, // COMPLETED, PENDING, REJECTED, REVERSED
    val referenceId: String = "",
    val description: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "offer_providers")
data class OfferProvider(
    @PrimaryKey val id: String, // torox, pubscale, lootably, timewall, adscend
    val name: String,
    val category: String,
    val isConfigured: Boolean = true,
    val isEnabled: Boolean = true,
    val appId: String = "",
    val secretKey: String = "",
    val postbackUrl: String = "",
    val supportedCountries: String = "IN,US,GB,CA"
)

@Entity(tableName = "offers")
data class Offer(
    @PrimaryKey val id: String,
    val providerId: String,
    val title: String,
    val appName: String = "",
    val category: String, // ALL, APPS, GAMES, SURVEYS, REGISTER
    val description: String,
    val requirements: String,
    val rewardCoins: Long,
    val estimatedMinutes: Int,
    val difficulty: String = "Easy Task",
    val status: String = "AVAILABLE", // AVAILABLE, PENDING, COMPLETED, REJECTED
    val targetUrl: String = "",
    val badge: String = "HOT",
    val iconType: String = "DEFAULT"
)

@Entity(tableName = "game_items")
data class GameItem(
    @PrimaryKey val id: String,
    val name: String,
    val milestone: String,
    val rewardCoins: Long,
    val estimatedMinutes: String,
    val iconType: String, // FRUIT, BUBBLE, CARROM, TEMPLE, CRICKET
    val category: String = "Casual",
    val status: String = "AVAILABLE" // AVAILABLE, PLAYING, COMPLETED
)

@Entity(tableName = "offer_events")
data class OfferEvent(
    @PrimaryKey val id: String,
    val offerId: String,
    val userId: String,
    val providerId: String,
    val clickTimestamp: Long = System.currentTimeMillis(),
    val callbackTimestamp: Long? = null,
    val status: String = "CLICKED", // CLICKED, PENDING_CALLBACK, VERIFIED_CREDITED, REJECTED
    val payoutCoins: Long,
    val rejectionReason: String? = null
)

@Entity(tableName = "referrals")
data class Referral(
    @PrimaryKey val id: String,
    val referrerUserId: String,
    val referredUserId: String,
    val referredUserName: String,
    val status: String = "PENDING", // PENDING, COMPLETED
    val rewardCoins: Long = 350,
    val qualificationOfferCompleted: Boolean = false,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "withdrawal_requests")
data class WithdrawalRequest(
    @PrimaryKey val id: String,
    val userId: String,
    val userName: String,
    val userEmail: String,
    val paymentMethod: String, // UPI, IMPS, GOOGLE_PLAY, AMAZON, FLIPKART, PHONEPE
    val coinAmount: Long,
    val inrAmount: Double,
    val detailsJson: String,
    val status: String = "PENDING", // PENDING, UNDER_REVIEW, PAYMENT_PROCESSING, COMPLETED, REJECTED
    val requestTimestamp: Long = System.currentTimeMillis(),
    val processedTimestamp: Long? = null,
    val paymentReference: String? = null,
    val rejectionReason: String? = null
)

@Entity(tableName = "gift_card_inventory")
data class GiftCardInventory(
    @PrimaryKey val id: String,
    val brand: String, // GOOGLE_PLAY, AMAZON, FLIPKART, PHONEPE
    val denominationInr: Int,
    val cardCode: String,
    val cardPin: String? = null,
    val status: String = "AVAILABLE", // AVAILABLE, ASSIGNED, DISABLED
    val assignedToUserId: String? = null,
    val assignedWithdrawalId: String? = null,
    val assignedTimestamp: Long? = null,
    val expiryDate: String = "31-Dec-2027"
)

@Entity(tableName = "user_rewards")
data class UserReward(
    @PrimaryKey val id: String,
    val userId: String,
    val brand: String,
    val title: String,
    val denominationInr: Int,
    val voucherCode: String,
    val pin: String? = null,
    val deliveredTimestamp: Long = System.currentTimeMillis(),
    val expiryDate: String = "31-Dec-2027",
    val withdrawalId: String = "",
    val status: String = "ACTIVE"
)

@Entity(tableName = "notifications")
data class NotificationItem(
    @PrimaryKey val id: String,
    val userId: String,
    val title: String,
    val message: String,
    val type: String, // OFFER, REFERRAL, VIDEO, GAME, WITHDRAWAL, SYSTEM
    val isRead: Boolean = false,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "support_tickets")
data class SupportTicket(
    @PrimaryKey val id: String,
    val userId: String,
    val subject: String,
    val message: String,
    val category: String,
    val status: String = "OPEN", // OPEN, IN_PROGRESS, RESOLVED
    val adminReply: String? = null,
    val createdTimestamp: Long = System.currentTimeMillis(),
    val updatedTimestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "app_settings")
data class AppSetting(
    @PrimaryKey val key: String,
    val value: String,
    val description: String
)

@Entity(tableName = "admin_audit_logs")
data class AdminAuditLog(
    @PrimaryKey val id: String,
    val adminId: String,
    val action: String,
    val targetUserId: String? = null,
    val details: String,
    val timestamp: Long = System.currentTimeMillis()
)
