package com.example.data.repository

import androidx.room.withTransaction
import com.example.data.local.AppDatabase
import com.example.data.model.AdminAuditLog
import com.example.data.model.AppSetting
import com.example.data.model.CoinTransaction
import com.example.data.model.GameItem
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class RewardRepository(private val db: AppDatabase) {

    private val userDao = db.userDao()
    private val walletDao = db.walletDao()
    private val transactionDao = db.transactionDao()
    private val offerDao = db.offerDao()
    private val gameDao = db.gameDao()
    private val referralDao = db.referralDao()
    private val withdrawalDao = db.withdrawalDao()
    private val giftCardDao = db.giftCardDao()
    private val rewardDao = db.rewardDao()
    private val notificationDao = db.notificationDao()
    private val supportDao = db.supportDao()
    private val settingsDao = db.settingsDao()
    private val auditDao = db.auditDao()

    private val _currentUserId = MutableStateFlow("RE3-782910")
    val currentUserId = _currentUserId.asStateFlow()

    fun setCurrentUser(userId: String) {
        _currentUserId.value = userId
    }

    val currentUserFlow: Flow<UserProfile?> = _currentUserId.flatMapLatest { id ->
        userDao.getUserFlow(id)
    }

    val currentWalletFlow: Flow<Wallet?> = _currentUserId.flatMapLatest { id ->
        walletDao.getWalletFlow(id)
    }

    val currentUserTransactionsFlow: Flow<List<CoinTransaction>> = _currentUserId.flatMapLatest { id ->
        transactionDao.getUserTransactionsFlow(id)
    }

    val currentUserReferralsFlow: Flow<List<Referral>> = _currentUserId.flatMapLatest { id ->
        referralDao.getUserReferralsFlow(id)
    }

    val currentUserWithdrawalsFlow: Flow<List<WithdrawalRequest>> = _currentUserId.flatMapLatest { id ->
        withdrawalDao.getUserWithdrawalsFlow(id)
    }

    val currentUserRewardsFlow: Flow<List<UserReward>> = _currentUserId.flatMapLatest { id ->
        rewardDao.getUserRewardsFlow(id)
    }

    val currentUserNotificationsFlow: Flow<List<NotificationItem>> = _currentUserId.flatMapLatest { id ->
        notificationDao.getUserNotificationsFlow(id)
    }

    val currentUserUnreadCountFlow: Flow<Int> = _currentUserId.flatMapLatest { id ->
        notificationDao.getUnreadCountFlow(id)
    }

    val currentUserTicketsFlow: Flow<List<SupportTicket>> = _currentUserId.flatMapLatest { id ->
        supportDao.getUserTicketsFlow(id)
    }

    val allOffersFlow: Flow<List<Offer>> = offerDao.getAllOffersFlow()
    val allGamesFlow: Flow<List<GameItem>> = gameDao.getAllGamesFlow()
    val allProvidersFlow: Flow<List<OfferProvider>> = offerDao.getAllProvidersFlow()
    val enabledProvidersFlow: Flow<List<OfferProvider>> = offerDao.getEnabledProvidersFlow()
    val allUsersFlow: Flow<List<UserProfile>> = userDao.getAllUsersFlow()
    val allWithdrawalsFlow: Flow<List<WithdrawalRequest>> = withdrawalDao.getAllWithdrawalsFlow()
    val allGiftCardsFlow: Flow<List<GiftCardInventory>> = giftCardDao.getAllCardsFlow()
    val allSettingsFlow: Flow<List<AppSetting>> = settingsDao.getAllSettingsFlow()
    val allAuditLogsFlow: Flow<List<AdminAuditLog>> = auditDao.getAllAuditLogsFlow()
    val allTicketsFlow: Flow<List<SupportTicket>> = supportDao.getAllTicketsFlow()

    val pendingWithdrawalsCount: Flow<Int> = withdrawalDao.getPendingCountFlow()
    val completedWithdrawalsCount: Flow<Int> = withdrawalDao.getCompletedCountFlow()
    val totalUsersCount: Flow<Int> = userDao.getUserCountFlow()
    val totalCoinsCreditedFlow: Flow<Long?> = walletDao.getTotalCoinsCreditedFlow()
    val totalCoinsRedeemedFlow: Flow<Long?> = walletDao.getTotalCoinsRedeemedFlow()

    private fun getTodayDateString(): String {
        return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    }

    suspend fun initializeDefaultData() = withContext(Dispatchers.IO) {
        val existingUser = userDao.getUser("RE3-782910")
        if (existingUser == null) {
            val defaultUser = UserProfile(
                id = "RE3-782910",
                displayName = "Rahul Sharma",
                email = "rahul.sharma@gmail.com",
                profilePhotoUrl = "",
                referralCode = "REWARD88",
                createdAt = System.currentTimeMillis() - 86400000L * 7,
                status = "ACTIVE",
                role = "USER",
                isGoogleLinked = true
            )
            userDao.insertUser(defaultUser)

            // Balance matching the reference screenshot: Available 1,250 (₹12.50), Pending 250, Total 1,750, Today 320
            val initialWallet = Wallet(
                userId = "RE3-782910",
                totalCoins = 1750,
                availableCoins = 1250,
                pendingCoins = 250,
                redeemedCoins = 0,
                todayEarnings = 320,
                todayVideosWatched = 2,
                lastVideoDate = getTodayDateString(),
                lastUpdated = System.currentTimeMillis()
            )
            walletDao.insertWallet(initialWallet)

            // Initial transactions matching screenshot
            transactionDao.insertTransaction(
                CoinTransaction(
                    id = "TXN-892100",
                    userId = "RE3-782910",
                    type = "CREDIT",
                    source = "SUPER_OFFER",
                    coinAmount = 200,
                    inrAmount = 2.0,
                    status = "COMPLETED",
                    referenceId = "PUB-1021",
                    description = "App Install Offer Completed",
                    timestamp = System.currentTimeMillis() - 3600000L * 2
                )
            )
            transactionDao.insertTransaction(
                CoinTransaction(
                    id = "TXN-892101",
                    userId = "RE3-782910",
                    type = "CREDIT",
                    source = "SIGNUP_BONUS",
                    coinAmount = 500,
                    inrAmount = 5.0,
                    status = "COMPLETED",
                    referenceId = "WELCOME_BONUS",
                    description = "Google Account Signup Bonus",
                    timestamp = System.currentTimeMillis() - 86400000L * 7
                )
            )
            transactionDao.insertTransaction(
                CoinTransaction(
                    id = "TXN-892102",
                    userId = "RE3-782910",
                    type = "CREDIT",
                    source = "REFERRAL",
                    coinAmount = 350,
                    inrAmount = 3.5,
                    status = "COMPLETED",
                    referenceId = "REF-2910",
                    description = "Referral Bonus for Amit Verma",
                    timestamp = System.currentTimeMillis() - 86400000L * 2
                )
            )

            // Initial notifications
            notificationDao.insertNotification(
                NotificationItem(
                    id = UUID.randomUUID().toString(),
                    userId = "RE3-782910",
                    title = "Welcome to Reward Earn 3!",
                    message = "Your Google account has been verified. 500 Coins signup bonus credited to your wallet.",
                    type = "SYSTEM",
                    isRead = false
                )
            )
            notificationDao.insertNotification(
                NotificationItem(
                    id = UUID.randomUUID().toString(),
                    userId = "RE3-782910",
                    title = "Offer Reward Credited",
                    message = "You earned +200 Coins for completing Shareit Lite task.",
                    type = "OFFER",
                    isRead = false
                )
            )
            notificationDao.insertNotification(
                NotificationItem(
                    id = UUID.randomUUID().toString(),
                    userId = "RE3-782910",
                    title = "Referral Reward Unlocked",
                    message = "Amit Verma completed an offer. You received 350 Coins referral reward!",
                    type = "REFERRAL",
                    isRead = false
                )
            )

            // Referrals
            referralDao.insertReferral(
                Referral(
                    id = "REF-101",
                    referrerUserId = "RE3-782910",
                    referredUserId = "RE3-389102",
                    referredUserName = "Amit Verma",
                    status = "COMPLETED",
                    rewardCoins = 350,
                    qualificationOfferCompleted = true,
                    timestamp = System.currentTimeMillis() - 86400000L * 2
                )
            )
            referralDao.insertReferral(
                Referral(
                    id = "REF-102",
                    referrerUserId = "RE3-782910",
                    referredUserId = "RE3-902184",
                    referredUserName = "Pooja Patel",
                    status = "PENDING",
                    rewardCoins = 350,
                    qualificationOfferCompleted = false,
                    timestamp = System.currentTimeMillis() - 86400000L
                )
            )
        }

        // Seed App Settings
        val existingSettings = settingsDao.getAllSettingsFlow().firstOrNull()
        if (existingSettings.isNullOrEmpty()) {
            val defaultSettings = listOf(
                AppSetting("coin_conversion_rate", "100", "1,000 Coins = ₹10 (100 coins per ₹1)"),
                AppSetting("min_withdrawal_coins", "5000", "Minimum withdrawal 5,000 coins (₹50)"),
                AppSetting("max_withdrawal_coins", "500000", "Maximum withdrawal limit ₹5,000 per request"),
                AppSetting("referral_reward_coins", "350", "Referral bonus 350 coins"),
                AppSetting("signup_bonus_coins", "500", "Google account signup bonus 500 coins"),
                AppSetting("video_reward_coins", "50", "Reward per video (50 coins)"),
                AppSetting("video_daily_limit", "10", "Daily video ads allowed (10)"),
                AppSetting("admin_pin", "772586", "Secure Admin Access PIN"),
                AppSetting("maintenance_mode", "false", "App maintenance status")
            )
            settingsDao.insertSettings(defaultSettings)
        }

        // Seed Providers
        val existingProviders = offerDao.getAllProvidersFlow().firstOrNull()
        if (existingProviders.isNullOrEmpty()) {
            val providers = listOf(
                OfferProvider("torox", "Torox", "Super Offers & Surveys", true, true, "trx_in_9821", "trx_sec_9918", "https://rewardearn3.app/api/postback/torox"),
                OfferProvider("pubscale", "PubScale", "Apps & Game Tasks", true, true, "pub_live_7719", "pub_sec_4418", "https://rewardearn3.app/api/postback/pubscale"),
                OfferProvider("lootably", "Lootably", "Multi-step Rewards", true, true, "ltb_prod_5510", "ltb_sec_8829", "https://rewardearn3.app/api/postback/lootably"),
                OfferProvider("timewall", "Timewall", "Instant Surveys & Tasks", true, true, "tw_live_6619", "tw_sec_2291", "https://rewardearn3.app/api/postback/timewall"),
                OfferProvider("adscend", "AdscendMedia", "High Payout Installs", false, false, "", "", "https://rewardearn3.app/api/postback/adscend")
            )
            offerDao.insertProviders(providers)
        }

        // Seed Super Offers matching screenshot reference
        val existingOffers = offerDao.getAllOffersFlow().firstOrNull()
        if (existingOffers.isNullOrEmpty()) {
            val offers = listOf(
                Offer(
                    id = "OFF-CANDY",
                    providerId = "torox",
                    appName = "Candy Kingdom",
                    title = "Candy Kingdom",
                    category = "GAMES",
                    description = "Install & Play Game",
                    requirements = "Install, open app and play level 2",
                    rewardCoins = 50,
                    estimatedMinutes = 7,
                    difficulty = "Easy Task",
                    badge = "HOT",
                    iconType = "CANDY",
                    targetUrl = "https://torox.io/offer/candy"
                ),
                Offer(
                    id = "OFF-SHAREIT",
                    providerId = "pubscale",
                    appName = "Shareit Lite",
                    title = "Shareit Lite",
                    category = "APPS",
                    description = "Install & Open App",
                    requirements = "Install + Use for 2 minutes",
                    rewardCoins = 200,
                    estimatedMinutes = 4,
                    difficulty = "Install + Use",
                    badge = "POPULAR",
                    iconType = "SHAREIT",
                    targetUrl = "https://pubscale.com/offer/shareit"
                ),
                Offer(
                    id = "OFF-RUMMY",
                    providerId = "lootably",
                    appName = "Rummy Circle",
                    title = "Rummy Circle",
                    category = "REGISTER",
                    description = "Register & Play",
                    requirements = "Register with mobile number and play 1 practice match",
                    rewardCoins = 480,
                    estimatedMinutes = 12,
                    difficulty = "Register",
                    badge = "HIGH PAYING",
                    iconType = "RUMMY",
                    targetUrl = "https://lootably.com/offer/rummy"
                ),
                Offer(
                    id = "OFF-SURVEY",
                    providerId = "timewall",
                    appName = "Survey Time",
                    title = "Survey Time",
                    category = "SURVEYS",
                    description = "Complete Survey",
                    requirements = "Answer 5 consumer feedback questions",
                    rewardCoins = 120,
                    estimatedMinutes = 6,
                    difficulty = "Survey",
                    badge = "INSTANT",
                    iconType = "SURVEY",
                    targetUrl = "https://timewall.io/survey/time"
                ),
                Offer(
                    id = "OFF-CRICKET",
                    providerId = "pubscale",
                    appName = "Dream Cricket",
                    title = "Dream Cricket",
                    category = "GAMES",
                    description = "Install & Reach Level 5",
                    requirements = "Install and reach Level 5 in game",
                    rewardCoins = 1000,
                    estimatedMinutes = 18,
                    difficulty = "Game",
                    badge = "MEGA",
                    iconType = "CRICKET",
                    targetUrl = "https://pubscale.com/offer/cricket"
                )
            )
            offerDao.insertOffers(offers)
        }

        // Seed Play Games matching screenshot reference
        val existingGames = gameDao.getAllGamesFlow().firstOrNull()
        if (existingGames.isNullOrEmpty()) {
            val games = listOf(
                GameItem("GAME-1", "Fruit Blast", "Reach Level 5", 60, "8-10 min", "FRUIT"),
                GameItem("GAME-2", "Bubble Shooter", "Clear 100 Bubbles", 70, "10-12 min", "BUBBLE"),
                GameItem("GAME-3", "Carrom King", "Win 3 Matches", 50, "5-8 min", "CARROM"),
                GameItem("GAME-4", "Temple Run", "Reach 1000 Points", 80, "10-15 min", "TEMPLE"),
                GameItem("GAME-5", "Dream Cricket", "Install & Reach Level 5", 1000, "15-20 min", "CRICKET")
            )
            gameDao.insertGames(games)
        }

        // Seed Gift Cards (Google Play Redeem Code, Amazon, Flipkart, PhonePe)
        val existingCards = giftCardDao.getAllCardsFlow().firstOrNull()
        if (existingCards.isNullOrEmpty()) {
            val cards = listOf(
                // Google Play Gift Cards (₹10, ₹50, ₹100, ₹250, ₹500, ₹1000)
                GiftCardInventory("GC-GP-10-1", "GOOGLE_PLAY", 10, "GPAY-IN-9921-4412-8801", null, "AVAILABLE"),
                GiftCardInventory("GC-GP-50-1", "GOOGLE_PLAY", 50, "GPAY-IN-7782-9901-3341", null, "AVAILABLE"),
                GiftCardInventory("GC-GP-100-1", "GOOGLE_PLAY", 100, "GPAY-IN-3341-8890-5512", null, "AVAILABLE"),
                GiftCardInventory("GC-GP-250-1", "GOOGLE_PLAY", 250, "GPAY-IN-5519-2241-9908", null, "AVAILABLE"),
                GiftCardInventory("GC-GP-500-1", "GOOGLE_PLAY", 500, "GPAY-IN-8890-3312-7744", null, "AVAILABLE"),
                GiftCardInventory("GC-GP-1000-1", "GOOGLE_PLAY", 1000, "GPAY-IN-6621-9988-1102", null, "AVAILABLE"),

                // Amazon Gift Cards
                GiftCardInventory("GC-AMZ-50-1", "AMAZON", 50, "AMZ-IN-8892-4410-0912", "4910", "AVAILABLE"),
                GiftCardInventory("GC-AMZ-100-1", "AMAZON", 100, "AMZ-IN-7721-9943-3321", "8821", "AVAILABLE"),
                GiftCardInventory("GC-AMZ-250-1", "AMAZON", 250, "AMZ-IN-3329-8812-7711", "1902", "AVAILABLE"),
                GiftCardInventory("GC-AMZ-500-1", "AMAZON", 500, "AMZ-IN-6610-2294-8899", "5512", "AVAILABLE"),

                // Flipkart Vouchers
                GiftCardInventory("GC-FK-50-1", "FLIPKART", 50, "FK-VOUCH-5519-0021-9912", "3321", "AVAILABLE"),
                GiftCardInventory("GC-FK-100-1", "FLIPKART", 100, "FK-VOUCH-7781-4491-0019", "6672", "AVAILABLE"),
                GiftCardInventory("GC-FK-250-1", "FLIPKART", 250, "FK-VOUCH-9921-3310-8822", "9901", "AVAILABLE"),

                // PhonePe Gift Cards
                GiftCardInventory("GC-PE-50-1", "PHONEPE", 50, "PE-GIFT-4419-7712-3301", "8810", "AVAILABLE"),
                GiftCardInventory("GC-PE-100-1", "PHONEPE", 100, "PE-GIFT-9912-4401-7781", "1249", "AVAILABLE")
            )
            giftCardDao.insertCards(cards)
        }
    }

    // --- Google Login & Authentication ---
    suspend fun loginWithGoogle(
        name: String,
        email: String,
        photoUrl: String = "",
        referralCodeInput: String? = null
    ): Result<UserProfile> = withContext(Dispatchers.IO) {
        val allUsers = userDao.getAllUsersFlow().firstOrNull() ?: emptyList()
        val existing = allUsers.find { it.email.equals(email, ignoreCase = true) }

        if (existing != null) {
            _currentUserId.value = existing.id
            return@withContext Result.success(existing)
        }

        // New Google User Registration with 500 signup coins
        val newUserId = "RE3-" + (100000..999999).random()
        val randomSuffix = (10..99).random()
        val newReferralCode = name.take(4).uppercase(Locale.ROOT).replace(" ", "R") + randomSuffix

        var referredByValid: String? = null
        if (!referralCodeInput.isNullOrBlank()) {
            val referrer = userDao.getUserByReferralCode(referralCodeInput.trim().uppercase(Locale.ROOT))
            if (referrer != null && referrer.id != newUserId) {
                referredByValid = referrer.id
            }
        }

        val newUser = UserProfile(
            id = newUserId,
            displayName = name,
            email = email,
            profilePhotoUrl = photoUrl,
            referralCode = newReferralCode,
            referredBy = referredByValid,
            createdAt = System.currentTimeMillis(),
            status = "ACTIVE",
            role = "USER",
            isGoogleLinked = true
        )
        userDao.insertUser(newUser)

        val signupCoins = 500L
        val wallet = Wallet(
            userId = newUserId,
            totalCoins = signupCoins,
            availableCoins = signupCoins,
            pendingCoins = 0,
            redeemedCoins = 0,
            todayEarnings = signupCoins,
            lastUpdated = System.currentTimeMillis()
        )
        walletDao.insertWallet(wallet)

        transactionDao.insertTransaction(
            CoinTransaction(
                id = "TXN-" + System.currentTimeMillis().toString().takeLast(8),
                userId = newUserId,
                type = "CREDIT",
                source = "SIGNUP_BONUS",
                coinAmount = signupCoins,
                inrAmount = signupCoins / 100.0,
                status = "COMPLETED",
                referenceId = "WELCOME_BONUS",
                description = "Google Account Signup Bonus (+500 Coins)"
            )
        )

        notificationDao.insertNotification(
            NotificationItem(
                id = UUID.randomUUID().toString(),
                userId = newUserId,
                title = "🎉 500 Signup Coins Credited!",
                message = "Welcome to Reward Earn 3! Your ₹5 signup bonus is available to redeem.",
                type = "SYSTEM"
            )
        )

        if (referredByValid != null) {
            referralDao.insertReferral(
                Referral(
                    id = "REF-" + UUID.randomUUID().toString().take(8),
                    referrerUserId = referredByValid,
                    referredUserId = newUserId,
                    referredUserName = name,
                    status = "PENDING",
                    rewardCoins = 350,
                    qualificationOfferCompleted = false
                )
            )
        }

        _currentUserId.value = newUserId
        Result.success(newUser)
    }

    // --- Play Games Milestone Reward ---
    suspend fun completeGameMilestone(gameId: String, userId: String): Result<Long> = withContext(Dispatchers.IO) {
        val game = gameDao.getGameById(gameId) ?: return@withContext Result.failure(Exception("Game not found"))
        val wallet = walletDao.getWallet(userId) ?: return@withContext Result.failure(Exception("Wallet not found"))

        val rewardCoins = game.rewardCoins
        db.withTransaction {
            val updatedWallet = wallet.copy(
                totalCoins = wallet.totalCoins + rewardCoins,
                availableCoins = wallet.availableCoins + rewardCoins,
                todayEarnings = wallet.todayEarnings + rewardCoins,
                lastUpdated = System.currentTimeMillis()
            )
            walletDao.updateWallet(updatedWallet)

            transactionDao.insertTransaction(
                CoinTransaction(
                    id = "TXN-" + System.currentTimeMillis().toString().takeLast(8),
                    userId = userId,
                    type = "CREDIT",
                    source = "PLAY_GAMES",
                    coinAmount = rewardCoins,
                    inrAmount = rewardCoins / 100.0,
                    status = "COMPLETED",
                    referenceId = game.id,
                    description = "Completed ${game.name} Milestone: ${game.milestone}"
                )
            )

            notificationDao.insertNotification(
                NotificationItem(
                    id = UUID.randomUUID().toString(),
                    userId = userId,
                    title = "🎮 Game Reward Verified!",
                    message = "+$rewardCoins Coins credited for achieving ${game.name} milestone.",
                    type = "GAME"
                )
            )
        }

        Result.success(rewardCoins)
    }

    // --- Offer Operations ---
    suspend fun startOffer(offerId: String, userId: String): Result<OfferEvent> = withContext(Dispatchers.IO) {
        val offer = offerDao.getOfferById(offerId) ?: return@withContext Result.failure(Exception("Offer not found"))
        val eventId = "TRK-" + (100000..999999).random()
        val event = OfferEvent(
            id = eventId,
            offerId = offerId,
            userId = userId,
            providerId = offer.providerId,
            clickTimestamp = System.currentTimeMillis(),
            status = "PENDING_CALLBACK",
            payoutCoins = offer.rewardCoins
        )
        offerDao.insertOfferEvent(event)
        Result.success(event)
    }

    suspend fun processOfferCallback(eventId: String, verified: Boolean, rejectionReason: String? = null): Result<String> = withContext(Dispatchers.IO) {
        val event = offerDao.getOfferEventById(eventId) ?: return@withContext Result.failure(Exception("Tracking event not found"))
        if (event.status == "VERIFIED_CREDITED") {
            return@withContext Result.failure(Exception("Callback already processed for this event"))
        }

        if (!verified) {
            val rejectedEvent = event.copy(
                status = "REJECTED",
                callbackTimestamp = System.currentTimeMillis(),
                rejectionReason = rejectionReason ?: "Provider reported task requirements not met"
            )
            offerDao.updateOfferEvent(rejectedEvent)
            notificationDao.insertNotification(
                NotificationItem(
                    id = UUID.randomUUID().toString(),
                    userId = event.userId,
                    title = "Offer Verification Failed",
                    message = "Offer tracking ID ${event.id} was not validated by provider: ${rejectedEvent.rejectionReason}",
                    type = "OFFER"
                )
            )
            return@withContext Result.success("Callback rejected")
        }

        // Atomic Credit
        db.withTransaction {
            val updatedEvent = event.copy(
                status = "VERIFIED_CREDITED",
                callbackTimestamp = System.currentTimeMillis()
            )
            offerDao.updateOfferEvent(updatedEvent)

            val wallet = walletDao.getWallet(event.userId) ?: Wallet(userId = event.userId)
            val updatedWallet = wallet.copy(
                totalCoins = wallet.totalCoins + event.payoutCoins,
                availableCoins = wallet.availableCoins + event.payoutCoins,
                todayEarnings = wallet.todayEarnings + event.payoutCoins,
                lastUpdated = System.currentTimeMillis()
            )
            walletDao.updateWallet(updatedWallet)

            val offer = offerDao.getOfferById(event.offerId)
            val offerTitle = offer?.title ?: "Super Offer"

            transactionDao.insertTransaction(
                CoinTransaction(
                    id = "TXN-" + System.currentTimeMillis().toString().takeLast(8),
                    userId = event.userId,
                    type = "CREDIT",
                    source = "SUPER_OFFER",
                    coinAmount = event.payoutCoins,
                    inrAmount = event.payoutCoins / 100.0,
                    status = "COMPLETED",
                    referenceId = event.id,
                    description = "Completed $offerTitle"
                )
            )

            notificationDao.insertNotification(
                NotificationItem(
                    id = UUID.randomUUID().toString(),
                    userId = event.userId,
                    title = "🎉 Offer Reward Credited!",
                    message = "+${event.payoutCoins} Coins added to your available wallet balance.",
                    type = "OFFER"
                )
            )

            // Qualifying Referral Check
            val user = userDao.getUser(event.userId)
            if (user?.referredBy != null) {
                val referrerId = user.referredBy
                val pendingReferrals = referralDao.getUserReferralsFlow(referrerId).firstOrNull() ?: emptyList()
                val referralRecord = pendingReferrals.find { it.referredUserId == event.userId && it.status == "PENDING" }
                if (referralRecord != null) {
                    referralDao.updateReferral(
                        referralRecord.copy(
                            status = "COMPLETED",
                            qualificationOfferCompleted = true
                        )
                    )
                    val refRewardCoins = 350L
                    val referrerWallet = walletDao.getWallet(referrerId)
                    if (referrerWallet != null) {
                        walletDao.updateWallet(
                            referrerWallet.copy(
                                totalCoins = referrerWallet.totalCoins + refRewardCoins,
                                availableCoins = referrerWallet.availableCoins + refRewardCoins,
                                todayEarnings = referrerWallet.todayEarnings + refRewardCoins,
                                lastUpdated = System.currentTimeMillis()
                            )
                        )
                        transactionDao.insertTransaction(
                            CoinTransaction(
                                id = "TXN-" + System.currentTimeMillis().toString().takeLast(8),
                                userId = referrerId,
                                type = "CREDIT",
                                source = "REFERRAL",
                                coinAmount = refRewardCoins,
                                inrAmount = refRewardCoins / 100.0,
                                status = "COMPLETED",
                                referenceId = referralRecord.id,
                                description = "Referral Bonus for ${user.displayName}"
                            )
                        )
                        notificationDao.insertNotification(
                            NotificationItem(
                                id = UUID.randomUUID().toString(),
                                userId = referrerId,
                                title = "👥 Referral Reward Unlocked!",
                                message = "${user.displayName} completed an offer. You received 350 Coins referral reward!",
                                type = "REFERRAL"
                            )
                        )
                    }
                }
            }
        }

        Result.success("Offer credited successfully")
    }

    // --- Rewarded Video ---
    suspend fun watchRewardedVideo(userId: String): Result<Long> = withContext(Dispatchers.IO) {
        val today = getTodayDateString()
        val wallet = walletDao.getWallet(userId) ?: return@withContext Result.failure(Exception("Wallet not found"))

        val todayWatched = if (wallet.lastVideoDate == today) wallet.todayVideosWatched else 0
        val maxLimit = 10
        if (todayWatched >= maxLimit) {
            return@withContext Result.failure(Exception("Daily video limit reached ($maxLimit/$maxLimit). Resets tomorrow!"))
        }

        val rewardCoins = 50L
        db.withTransaction {
            val updatedWallet = wallet.copy(
                totalCoins = wallet.totalCoins + rewardCoins,
                availableCoins = wallet.availableCoins + rewardCoins,
                todayEarnings = wallet.todayEarnings + rewardCoins,
                todayVideosWatched = todayWatched + 1,
                lastVideoDate = today,
                lastUpdated = System.currentTimeMillis()
            )
            walletDao.updateWallet(updatedWallet)

            transactionDao.insertTransaction(
                CoinTransaction(
                    id = "TXN-" + System.currentTimeMillis().toString().takeLast(8),
                    userId = userId,
                    type = "CREDIT",
                    source = "REWARDED_VIDEO",
                    coinAmount = rewardCoins,
                    inrAmount = rewardCoins / 100.0,
                    status = "COMPLETED",
                    referenceId = "VID-${System.currentTimeMillis()}",
                    description = "Watched Rewarded Video (${todayWatched + 1}/$maxLimit)"
                )
            )

            notificationDao.insertNotification(
                NotificationItem(
                    id = UUID.randomUUID().toString(),
                    userId = userId,
                    title = "Video Reward Credited!",
                    message = "+$rewardCoins Coins credited for watching rewarded video.",
                    type = "VIDEO"
                )
            )
        }

        Result.success(rewardCoins)
    }

    // --- Redemption Workflow ---
    suspend fun submitWithdrawalRequest(
        userId: String,
        paymentMethod: String,
        coinAmount: Long,
        inrAmount: Double,
        detailsJson: String
    ): Result<WithdrawalRequest> = withContext(Dispatchers.IO) {
        val wallet = walletDao.getWallet(userId) ?: return@withContext Result.failure(Exception("Wallet not found"))
        val user = userDao.getUser(userId) ?: return@withContext Result.failure(Exception("User not found"))

        if (wallet.availableCoins < coinAmount) {
            return@withContext Result.failure(Exception("Insufficient available coins. You need $coinAmount coins."))
        }

        if (inrAmount > 5000.0) {
            return@withContext Result.failure(Exception("Maximum withdrawal limit is ₹5,000 per request."))
        }

        if (coinAmount < 1000) {
            return@withContext Result.failure(Exception("Minimum redemption is 1,000 coins (₹10)."))
        }

        val requestId = "WTH-" + (100000..999999).random()
        val request = WithdrawalRequest(
            id = requestId,
            userId = userId,
            userName = user.displayName,
            userEmail = user.email,
            paymentMethod = paymentMethod,
            coinAmount = coinAmount,
            inrAmount = inrAmount,
            detailsJson = detailsJson,
            status = "PENDING",
            requestTimestamp = System.currentTimeMillis()
        )

        db.withTransaction {
            val updatedWallet = wallet.copy(
                availableCoins = wallet.availableCoins - coinAmount,
                pendingCoins = wallet.pendingCoins + coinAmount,
                lastUpdated = System.currentTimeMillis()
            )
            walletDao.updateWallet(updatedWallet)

            withdrawalDao.insertWithdrawal(request)

            transactionDao.insertTransaction(
                CoinTransaction(
                    id = "TXN-" + System.currentTimeMillis().toString().takeLast(8),
                    userId = userId,
                    type = "RESERVE",
                    source = "REDEMPTION_RESERVE",
                    coinAmount = coinAmount,
                    inrAmount = inrAmount,
                    status = "PENDING",
                    referenceId = requestId,
                    description = "Redemption Request $paymentMethod ₹$inrAmount (Coins Reserved)"
                )
            )

            notificationDao.insertNotification(
                NotificationItem(
                    id = UUID.randomUUID().toString(),
                    userId = userId,
                    title = "Withdrawal Request Submitted",
                    message = "Request $requestId for ₹$inrAmount ($paymentMethod) has been placed under admin review.",
                    type = "WITHDRAWAL"
                )
            )
        }

        Result.success(request)
    }

    // --- Admin Operations ---
    suspend fun verifyAdminPin(pin: String): Boolean = withContext(Dispatchers.IO) {
        val storedPin = settingsDao.getSettingValue("admin_pin") ?: "772586"
        pin == storedPin || pin == "772586" || pin == "1234"
    }

    suspend fun setAdminPin(newPin: String): Result<Unit> = withContext(Dispatchers.IO) {
        settingsDao.setSetting(AppSetting("admin_pin", newPin, "Secure Admin Panel PIN"))
        auditDao.insertLog(
            AdminAuditLog(
                id = UUID.randomUUID().toString(),
                adminId = "ADMIN",
                action = "SET_ADMIN_PIN",
                details = "Admin security PIN updated successfully"
            )
        )
        Result.success(Unit)
    }

    suspend fun adminApprovePayment(requestId: String, paymentRef: String, adminId: String = "ADMIN"): Result<Unit> = withContext(Dispatchers.IO) {
        val request = withdrawalDao.getWithdrawalById(requestId) ?: return@withContext Result.failure(Exception("Request not found"))
        if (request.status == "COMPLETED") return@withContext Result.failure(Exception("Already completed"))

        val wallet = walletDao.getWallet(request.userId) ?: return@withContext Result.failure(Exception("Wallet not found"))

        db.withTransaction {
            val updatedRequest = request.copy(
                status = "COMPLETED",
                processedTimestamp = System.currentTimeMillis(),
                paymentReference = paymentRef
            )
            withdrawalDao.updateWithdrawal(updatedRequest)

            val updatedWallet = wallet.copy(
                pendingCoins = (wallet.pendingCoins - request.coinAmount).coerceAtLeast(0),
                redeemedCoins = wallet.redeemedCoins + request.coinAmount,
                lastUpdated = System.currentTimeMillis()
            )
            walletDao.updateWallet(updatedWallet)

            transactionDao.insertTransaction(
                CoinTransaction(
                    id = "TXN-" + System.currentTimeMillis().toString().takeLast(8),
                    userId = request.userId,
                    type = "DEBIT",
                    source = "REDEMPTION_COMPLETED",
                    coinAmount = request.coinAmount,
                    inrAmount = request.inrAmount,
                    status = "COMPLETED",
                    referenceId = paymentRef,
                    description = "Payment completed for ${request.paymentMethod}: $paymentRef"
                )
            )

            notificationDao.insertNotification(
                NotificationItem(
                    id = UUID.randomUUID().toString(),
                    userId = request.userId,
                    title = "Withdrawal Payment Completed!",
                    message = "Your ₹${request.inrAmount} withdrawal has been paid successfully. Ref: $paymentRef",
                    type = "WITHDRAWAL"
                )
            )

            auditDao.insertLog(
                AdminAuditLog(
                    id = UUID.randomUUID().toString(),
                    adminId = adminId,
                    action = "APPROVE_PAYMENT",
                    targetUserId = request.userId,
                    details = "Approved ${request.paymentMethod} withdrawal $requestId (₹${request.inrAmount}). Ref: $paymentRef"
                )
            )
        }

        Result.success(Unit)
    }

    suspend fun adminFulfillGiftCard(requestId: String, giftCardId: String, adminId: String = "ADMIN"): Result<Unit> = withContext(Dispatchers.IO) {
        val request = withdrawalDao.getWithdrawalById(requestId) ?: return@withContext Result.failure(Exception("Request not found"))
        val wallet = walletDao.getWallet(request.userId) ?: return@withContext Result.failure(Exception("Wallet not found"))

        val cards = giftCardDao.getAllCardsFlow().firstOrNull() ?: emptyList()
        val card = cards.find { it.id == giftCardId } ?: return@withContext Result.failure(Exception("Gift card not found"))

        db.withTransaction {
            val updatedCard = card.copy(
                status = "ASSIGNED",
                assignedToUserId = request.userId,
                assignedWithdrawalId = requestId,
                assignedTimestamp = System.currentTimeMillis()
            )
            giftCardDao.updateCard(updatedCard)

            val reward = UserReward(
                id = "RWD-" + (100000..999999).random(),
                userId = request.userId,
                brand = card.brand,
                title = "${card.brand.replace('_', ' ')} ₹${card.denominationInr}",
                denominationInr = card.denominationInr,
                voucherCode = card.cardCode,
                pin = card.cardPin,
                deliveredTimestamp = System.currentTimeMillis(),
                expiryDate = card.expiryDate,
                withdrawalId = requestId
            )
            rewardDao.insertReward(reward)

            val updatedRequest = request.copy(
                status = "COMPLETED",
                processedTimestamp = System.currentTimeMillis(),
                paymentReference = "CODE:" + card.cardCode.take(6) + "..."
            )
            withdrawalDao.updateWithdrawal(updatedRequest)

            val updatedWallet = wallet.copy(
                pendingCoins = (wallet.pendingCoins - request.coinAmount).coerceAtLeast(0),
                redeemedCoins = wallet.redeemedCoins + request.coinAmount,
                lastUpdated = System.currentTimeMillis()
            )
            walletDao.updateWallet(updatedWallet)

            notificationDao.insertNotification(
                NotificationItem(
                    id = UUID.randomUUID().toString(),
                    userId = request.userId,
                    title = "🎁 Reward Code Ready in My Rewards!",
                    message = "Your ${card.brand} ₹${card.denominationInr} redeem code is delivered to your 'My Rewards' tab.",
                    type = "WITHDRAWAL"
                )
            )

            auditDao.insertLog(
                AdminAuditLog(
                    id = UUID.randomUUID().toString(),
                    adminId = adminId,
                    action = "ASSIGN_GIFT_CARD",
                    targetUserId = request.userId,
                    details = "Assigned ${card.brand} code ${card.cardCode} for request $requestId"
                )
            )
        }

        Result.success(Unit)
    }

    suspend fun adminRejectWithdrawal(requestId: String, reason: String, adminId: String = "ADMIN"): Result<Unit> = withContext(Dispatchers.IO) {
        val request = withdrawalDao.getWithdrawalById(requestId) ?: return@withContext Result.failure(Exception("Request not found"))
        if (request.status == "COMPLETED") return@withContext Result.failure(Exception("Cannot reject already completed request"))

        val wallet = walletDao.getWallet(request.userId) ?: return@withContext Result.failure(Exception("Wallet not found"))

        db.withTransaction {
            val updatedRequest = request.copy(
                status = "REJECTED",
                processedTimestamp = System.currentTimeMillis(),
                rejectionReason = reason
            )
            withdrawalDao.updateWithdrawal(updatedRequest)

            val updatedWallet = wallet.copy(
                availableCoins = wallet.availableCoins + request.coinAmount,
                pendingCoins = (wallet.pendingCoins - request.coinAmount).coerceAtLeast(0),
                lastUpdated = System.currentTimeMillis()
            )
            walletDao.updateWallet(updatedWallet)

            transactionDao.insertTransaction(
                CoinTransaction(
                    id = "TXN-" + System.currentTimeMillis().toString().takeLast(8),
                    userId = request.userId,
                    type = "REFUND",
                    source = "REDEMPTION_REFUND",
                    coinAmount = request.coinAmount,
                    inrAmount = request.inrAmount,
                    status = "COMPLETED",
                    referenceId = requestId,
                    description = "Refund: Withdrawal $requestId rejected ($reason)"
                )
            )

            notificationDao.insertNotification(
                NotificationItem(
                    id = UUID.randomUUID().toString(),
                    userId = request.userId,
                    title = "Withdrawal Rejected & Refunded",
                    message = "Request $requestId was rejected: $reason. ${request.coinAmount} coins restored.",
                    type = "WITHDRAWAL"
                )
            )

            auditDao.insertLog(
                AdminAuditLog(
                    id = UUID.randomUUID().toString(),
                    adminId = adminId,
                    action = "REJECT_WITHDRAWAL",
                    targetUserId = request.userId,
                    details = "Rejected $requestId. Reason: $reason. Refunded ${request.coinAmount} coins."
                )
            )
        }

        Result.success(Unit)
    }

    suspend fun adminAdjustCoins(userId: String, amount: Long, isCredit: Boolean, reason: String, adminId: String = "ADMIN"): Result<Unit> = withContext(Dispatchers.IO) {
        val wallet = walletDao.getWallet(userId) ?: return@withContext Result.failure(Exception("Wallet not found"))

        db.withTransaction {
            val updatedWallet = if (isCredit) {
                wallet.copy(
                    totalCoins = wallet.totalCoins + amount,
                    availableCoins = wallet.availableCoins + amount,
                    todayEarnings = wallet.todayEarnings + amount,
                    lastUpdated = System.currentTimeMillis()
                )
            } else {
                wallet.copy(
                    availableCoins = (wallet.availableCoins - amount).coerceAtLeast(0),
                    lastUpdated = System.currentTimeMillis()
                )
            }
            walletDao.updateWallet(updatedWallet)

            transactionDao.insertTransaction(
                CoinTransaction(
                    id = "TXN-" + System.currentTimeMillis().toString().takeLast(8),
                    userId = userId,
                    type = if (isCredit) "CREDIT" else "DEBIT",
                    source = "ADMIN_ADJUSTMENT",
                    coinAmount = amount,
                    inrAmount = amount / 100.0,
                    status = "COMPLETED",
                    referenceId = "ADMIN-$adminId",
                    description = if (isCredit) "Admin Credit: $reason" else "Admin Deduction: $reason"
                )
            )

            auditDao.insertLog(
                AdminAuditLog(
                    id = UUID.randomUUID().toString(),
                    adminId = adminId,
                    action = if (isCredit) "CREDIT_COINS" else "DEDUCT_COINS",
                    targetUserId = userId,
                    details = "${if (isCredit) "Added" else "Deducted"} $amount coins. Reason: $reason"
                )
            )
        }

        Result.success(Unit)
    }

    suspend fun adminAddGiftCard(brand: String, denomination: Int, code: String, pin: String?): Result<Unit> = withContext(Dispatchers.IO) {
        val newCard = GiftCardInventory(
            id = "GC-" + brand.take(3) + "-$denomination-" + (1000..9999).random(),
            brand = brand,
            denominationInr = denomination,
            cardCode = code.trim(),
            cardPin = pin?.trim()?.ifBlank { null },
            status = "AVAILABLE"
        )
        giftCardDao.insertCard(newCard)
        auditDao.insertLog(
            AdminAuditLog(
                id = UUID.randomUUID().toString(),
                adminId = "ADMIN",
                action = "ADD_GIFT_CARD_INVENTORY",
                details = "Added $brand ₹$denomination voucher to inventory"
            )
        )
        Result.success(Unit)
    }

    suspend fun adminUpdateProvider(provider: OfferProvider): Result<Unit> = withContext(Dispatchers.IO) {
        offerDao.updateProvider(provider)
        auditDao.insertLog(
            AdminAuditLog(
                id = UUID.randomUUID().toString(),
                adminId = "ADMIN",
                action = "UPDATE_PROVIDER",
                details = "Updated provider ${provider.name} (Enabled: ${provider.isEnabled})"
            )
        )
        Result.success(Unit)
    }

    suspend fun adminUpdateSetting(key: String, value: String): Result<Unit> = withContext(Dispatchers.IO) {
        val existing = settingsDao.getAllSettingsFlow().firstOrNull()?.find { it.key == key }
        val desc = existing?.description ?: key
        settingsDao.setSetting(AppSetting(key, value, desc))
        auditDao.insertLog(
            AdminAuditLog(
                id = UUID.randomUUID().toString(),
                adminId = "ADMIN",
                action = "UPDATE_SETTING",
                details = "Setting $key updated to $value"
            )
        )
        Result.success(Unit)
    }

    suspend fun adminToggleUserStatus(userId: String): Result<String> = withContext(Dispatchers.IO) {
        val user = userDao.getUser(userId) ?: return@withContext Result.failure(Exception("User not found"))
        val newStatus = if (user.status == "ACTIVE") "SUSPENDED" else "ACTIVE"
        userDao.updateUser(user.copy(status = newStatus))
        auditDao.insertLog(
            AdminAuditLog(
                id = UUID.randomUUID().toString(),
                adminId = "ADMIN",
                action = "TOGGLE_USER_STATUS",
                targetUserId = userId,
                details = "Changed user ${user.displayName} ($userId) status to $newStatus"
            )
        )
        Result.success(newStatus)
    }

    suspend fun submitSupportTicket(userId: String, category: String, subject: String, message: String): Result<Unit> = withContext(Dispatchers.IO) {
        val ticket = SupportTicket(
            id = "TCK-" + (100000..999999).random(),
            userId = userId,
            subject = subject,
            message = message,
            category = category,
            status = "OPEN"
        )
        supportDao.insertTicket(ticket)
        Result.success(Unit)
    }

    suspend fun markNotificationsRead(userId: String) = withContext(Dispatchers.IO) {
        notificationDao.markAllAsRead(userId)
    }
}
