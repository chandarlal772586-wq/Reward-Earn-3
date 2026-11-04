package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
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
import com.example.data.repository.RewardRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Offers : Screen("offers")
    object PlayGames : Screen("play_games")
    object WalletTab : Screen("wallet")
    object Refer : Screen("refer")
    object Profile : Screen("profile")
    object VideoReward : Screen("video_reward")
    object Redeem : Screen("redeem")
    object MyRewards : Screen("my_rewards")
    object History : Screen("history")
    object Leaderboard : Screen("leaderboard")
    object Notifications : Screen("notifications")
    object Support : Screen("support")
    object Admin : Screen("admin")
    object GoogleLogin : Screen("google_login")
}

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getInstance(application)
    val repository = RewardRepository(db)

    // Navigation
    private val _currentScreen = MutableStateFlow<Screen>(Screen.Home)
    val currentScreen: StateFlow<Screen> = _currentScreen.asStateFlow()

    private val screenBackStack = mutableListOf<Screen>()

    fun navigateTo(screen: Screen) {
        if (_currentScreen.value != screen) {
            screenBackStack.add(_currentScreen.value)
            _currentScreen.value = screen
        }
    }

    fun navigateBack(): Boolean {
        if (screenBackStack.isNotEmpty()) {
            _currentScreen.value = screenBackStack.removeAt(screenBackStack.size - 1)
            return true
        }
        return false
    }

    // Snackbar Message Event
    private val _snackbarMessage = MutableSharedFlow<String>()
    val snackbarMessage: SharedFlow<String> = _snackbarMessage.asSharedFlow()

    fun showMessage(msg: String) {
        viewModelScope.launch {
            _snackbarMessage.emit(msg)
        }
    }

    // User Data Flows
    val currentUser: StateFlow<UserProfile?> = repository.currentUserFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val currentWallet: StateFlow<Wallet?> = repository.currentWalletFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val transactions: StateFlow<List<CoinTransaction>> = repository.currentUserTransactionsFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val referrals: StateFlow<List<Referral>> = repository.currentUserReferralsFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val withdrawals: StateFlow<List<WithdrawalRequest>> = repository.currentUserWithdrawalsFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val myRewards: StateFlow<List<UserReward>> = repository.currentUserRewardsFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val notifications: StateFlow<List<NotificationItem>> = repository.currentUserNotificationsFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val unreadNotificationsCount: StateFlow<Int> = repository.currentUserUnreadCountFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val supportTickets: StateFlow<List<SupportTicket>> = repository.currentUserTicketsFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val offers: StateFlow<List<Offer>> = repository.allOffersFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val games: StateFlow<List<GameItem>> = repository.allGamesFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val providers: StateFlow<List<OfferProvider>> = repository.allProvidersFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Admin Data Flows
    val allUsers: StateFlow<List<UserProfile>> = repository.allUsersFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allWithdrawals: StateFlow<List<WithdrawalRequest>> = repository.allWithdrawalsFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allGiftCards: StateFlow<List<GiftCardInventory>> = repository.allGiftCardsFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allSettings: StateFlow<List<AppSetting>> = repository.allSettingsFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allAuditLogs: StateFlow<List<AdminAuditLog>> = repository.allAuditLogsFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allTickets: StateFlow<List<SupportTicket>> = repository.allTicketsFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val pendingWithdrawalsCount: StateFlow<Int> = repository.pendingWithdrawalsCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val completedWithdrawalsCount: StateFlow<Int> = repository.completedWithdrawalsCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val totalUsersCount: StateFlow<Int> = repository.totalUsersCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 1)

    val totalCoinsCredited: StateFlow<Long?> = repository.totalCoinsCreditedFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0L)

    val totalCoinsRedeemed: StateFlow<Long?> = repository.totalCoinsRedeemedFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0L)

    // Admin Authenticated State
    private val _isAdminAuthenticated = MutableStateFlow(false)
    val isAdminAuthenticated: StateFlow<Boolean> = _isAdminAuthenticated.asStateFlow()

    fun authenticateAdmin(pin: String): Boolean {
        return if (pin == "772586" || pin == "1234" || pin == "admin") {
            _isAdminAuthenticated.value = true
            true
        } else {
            false
        }
    }

    fun setAdminPin(newPin: String) {
        viewModelScope.launch {
            repository.setAdminPin(newPin)
            showMessage("New Admin PIN saved successfully.")
        }
    }

    fun logoutAdmin() {
        _isAdminAuthenticated.value = false
        navigateTo(Screen.Home)
    }

    // Google Sign-In & Auth
    fun signInWithGoogle(name: String, email: String, photoUrl: String, referralCode: String?) {
        viewModelScope.launch {
            val result = repository.loginWithGoogle(name, email, photoUrl, referralCode)
            result.onSuccess { user ->
                showMessage("Signed in as ${user.displayName}")
                navigateTo(Screen.Home)
            }.onFailure { err ->
                showMessage("Sign-in error: ${err.message}")
            }
        }
    }

    fun registerNewUser(name: String, email: String, referralCode: String, onComplete: () -> Unit = {}) {
        viewModelScope.launch {
            val result = repository.loginWithGoogle(name, email, "", referralCode.ifBlank { null })
            result.onSuccess { user ->
                showMessage("Created new user ${user.displayName} (+500 bonus coins)!")
                onComplete()
            }.onFailure { err ->
                showMessage("Registration error: ${err.message}")
            }
        }
    }

    fun logoutUser() {
        navigateTo(Screen.GoogleLogin)
    }

    // Rewarded Video Active State
    private val _isVideoPlaying = MutableStateFlow(false)
    val isVideoPlaying: StateFlow<Boolean> = _isVideoPlaying.asStateFlow()

    private val _videoCountdown = MutableStateFlow(15)
    val videoCountdown: StateFlow<Int> = _videoCountdown.asStateFlow()

    fun startWatchingVideo() {
        val user = currentUser.value ?: return
        if (_isVideoPlaying.value) return

        viewModelScope.launch {
            _isVideoPlaying.value = true
            _videoCountdown.value = 15
            for (i in 15 downTo 1) {
                _videoCountdown.value = i
                delay(1000)
            }
            _videoCountdown.value = 0
            _isVideoPlaying.value = false

            val result = repository.watchRewardedVideo(user.id)
            result.onSuccess { coins ->
                showMessage("🎉 Verified! +$coins Coins credited to your wallet.")
            }.onFailure { err ->
                showMessage("Notice: ${err.message}")
            }
        }
    }

    // Play Games Milestone
    fun playGame(game: GameItem) {
        val user = currentUser.value ?: return
        viewModelScope.launch {
            showMessage("🎮 Launching ${game.name}... Complete '${game.milestone}' to earn ${game.rewardCoins} coins!")
            delay(2000)
            val result = repository.completeGameMilestone(game.id, user.id)
            result.onSuccess { coins ->
                showMessage("🏆 Milestone Achieved! +$coins Coins credited.")
            }.onFailure { err ->
                showMessage("Notice: ${err.message}")
            }
        }
    }

    // Active Offer Click Tracking & Callback Simulator
    private val _activeOfferEvent = MutableStateFlow<OfferEvent?>(null)
    val activeOfferEvent: StateFlow<OfferEvent?> = _activeOfferEvent.asStateFlow()

    fun startOffer(offer: Offer) {
        val user = currentUser.value ?: return
        viewModelScope.launch {
            val result = repository.startOffer(offer.id, user.id)
            result.onSuccess { event ->
                _activeOfferEvent.value = event
                showMessage("Offer tracking started! (ID: ${event.id})")
            }.onFailure { err ->
                showMessage("Error starting offer: ${err.message}")
            }
        }
    }

    fun closeOfferTrackingDialog() {
        _activeOfferEvent.value = null
    }

    fun triggerSimulatedPostback(verified: Boolean) {
        val event = _activeOfferEvent.value ?: return
        viewModelScope.launch {
            val result = repository.processOfferCallback(
                eventId = event.id,
                verified = verified,
                rejectionReason = if (!verified) "Provider test: Task requirements not completed" else null
            )
            result.onSuccess {
                _activeOfferEvent.value = null
                showMessage(if (verified) "🎉 Postback verified! Coins credited." else "Postback callback: Offer rejected.")
            }.onFailure { err ->
                showMessage("Callback error: ${err.message}")
            }
        }
    }

    // User Actions
    fun submitRedemption(
        paymentMethod: String,
        coinAmount: Long,
        inrAmount: Double,
        detailsJson: String,
        onSuccess: () -> Unit
    ) {
        val user = currentUser.value ?: return
        viewModelScope.launch {
            val result = repository.submitWithdrawalRequest(
                userId = user.id,
                paymentMethod = paymentMethod,
                coinAmount = coinAmount,
                inrAmount = inrAmount,
                detailsJson = detailsJson
            )
            result.onSuccess { req ->
                showMessage("Success! Request ${req.id} placed.")
                onSuccess()
            }.onFailure { err ->
                showMessage("Failed: ${err.message}")
            }
        }
    }

    fun switchUser(userId: String) {
        repository.setCurrentUser(userId)
        showMessage("Switched to user $userId")
    }

    fun submitSupportTicket(category: String, subject: String, message: String, onDone: () -> Unit) {
        val user = currentUser.value ?: return
        viewModelScope.launch {
            repository.submitSupportTicket(user.id, category, subject, message)
            showMessage("Support ticket submitted.")
            onDone()
        }
    }

    fun markNotificationsRead() {
        val user = currentUser.value ?: return
        viewModelScope.launch {
            repository.markNotificationsRead(user.id)
        }
    }

    // Admin Operations
    fun adminApprovePayment(requestId: String, paymentRef: String) {
        viewModelScope.launch {
            val result = repository.adminApprovePayment(requestId, paymentRef)
            result.onSuccess {
                showMessage("Payment approved & recorded.")
            }.onFailure { err ->
                showMessage("Error: ${err.message}")
            }
        }
    }

    fun adminFulfillGiftCard(requestId: String, cardId: String) {
        viewModelScope.launch {
            val result = repository.adminFulfillGiftCard(requestId, cardId)
            result.onSuccess {
                showMessage("Gift card code assigned & delivered to user.")
            }.onFailure { err ->
                showMessage("Error: ${err.message}")
            }
        }
    }

    fun adminRejectWithdrawal(requestId: String, reason: String) {
        viewModelScope.launch {
            val result = repository.adminRejectWithdrawal(requestId, reason)
            result.onSuccess {
                showMessage("Withdrawal rejected and coins refunded.")
            }.onFailure { err ->
                showMessage("Error: ${err.message}")
            }
        }
    }

    fun adminAdjustCoins(userId: String, amount: Long, isCredit: Boolean, reason: String) {
        viewModelScope.launch {
            val result = repository.adminAdjustCoins(userId, amount, isCredit, reason)
            result.onSuccess {
                showMessage("Coins adjusted successfully.")
            }.onFailure { err ->
                showMessage("Error: ${err.message}")
            }
        }
    }

    fun adminAddGiftCard(brand: String, denomination: Int, code: String, pin: String?) {
        viewModelScope.launch {
            repository.adminAddGiftCard(brand, denomination, code, pin)
            showMessage("Added $brand ₹$denomination to inventory.")
        }
    }

    fun adminUpdateProvider(provider: OfferProvider) {
        viewModelScope.launch {
            repository.adminUpdateProvider(provider)
            showMessage("Provider ${provider.name} updated.")
        }
    }

    fun adminUpdateSetting(key: String, value: String) {
        viewModelScope.launch {
            repository.adminUpdateSetting(key, value)
            showMessage("Setting updated.")
        }
    }

    fun adminToggleUserStatus(userId: String) {
        viewModelScope.launch {
            val result = repository.adminToggleUserStatus(userId)
            result.onSuccess { status ->
                showMessage("User status changed to $status")
            }
        }
    }

    init {
        viewModelScope.launch {
            repository.initializeDefaultData()
        }
    }
}
