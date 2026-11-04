package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.AppBottomNav
import com.example.ui.components.OfferTrackingDialog
import com.example.ui.screens.AdminScreen
import com.example.ui.screens.GoogleLoginScreen
import com.example.ui.screens.HistoryScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.LeaderboardScreen
import com.example.ui.screens.MyRewardsScreen
import com.example.ui.screens.NotificationsScreen
import com.example.ui.screens.OffersScreen
import com.example.ui.screens.PlayGamesScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.RedeemScreen
import com.example.ui.screens.ReferScreen
import com.example.ui.screens.SupportScreen
import com.example.ui.screens.VideoRewardScreen
import com.example.ui.screens.WalletScreen
import com.example.ui.theme.RewardEarn3Theme
import com.example.ui.viewmodel.MainViewModel
import com.example.ui.viewmodel.Screen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RewardEarn3Theme {
                MainContent()
            }
        }
    }
}

@Composable
fun MainContent(viewModel: MainViewModel = viewModel()) {
    val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
    val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()
    val currentWallet by viewModel.currentWallet.collectAsStateWithLifecycle()
    val transactions by viewModel.transactions.collectAsStateWithLifecycle()
    val referrals by viewModel.referrals.collectAsStateWithLifecycle()
    val withdrawals by viewModel.withdrawals.collectAsStateWithLifecycle()
    val myRewards by viewModel.myRewards.collectAsStateWithLifecycle()
    val notifications by viewModel.notifications.collectAsStateWithLifecycle()
    val unreadCount by viewModel.unreadNotificationsCount.collectAsStateWithLifecycle()
    val supportTickets by viewModel.supportTickets.collectAsStateWithLifecycle()
    val offers by viewModel.offers.collectAsStateWithLifecycle()
    val games by viewModel.games.collectAsStateWithLifecycle()
    val providers by viewModel.providers.collectAsStateWithLifecycle()

    // Admin flows
    val isAdminAuth by viewModel.isAdminAuthenticated.collectAsStateWithLifecycle()
    val allUsers by viewModel.allUsers.collectAsStateWithLifecycle()
    val allWithdrawals by viewModel.allWithdrawals.collectAsStateWithLifecycle()
    val allGiftCards by viewModel.allGiftCards.collectAsStateWithLifecycle()
    val allSettings by viewModel.allSettings.collectAsStateWithLifecycle()
    val allAuditLogs by viewModel.allAuditLogs.collectAsStateWithLifecycle()
    val totalUsersCount by viewModel.totalUsersCount.collectAsStateWithLifecycle()
    val totalCoinsCredited by viewModel.totalCoinsCredited.collectAsStateWithLifecycle()
    val totalCoinsRedeemed by viewModel.totalCoinsRedeemed.collectAsStateWithLifecycle()
    val pendingWithdrawalsCount by viewModel.pendingWithdrawalsCount.collectAsStateWithLifecycle()

    // Video flow
    val isVideoPlaying by viewModel.isVideoPlaying.collectAsStateWithLifecycle()
    val videoCountdown by viewModel.videoCountdown.collectAsStateWithLifecycle()

    // Offer event
    val activeOfferEvent by viewModel.activeOfferEvent.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.snackbarMessage.collect { msg ->
            snackbarHostState.showSnackbar(msg)
        }
    }

    // Handle back button for sub-screens
    val isPrimaryScreen = currentScreen in listOf(Screen.Home, Screen.Offers, Screen.WalletTab, Screen.Refer, Screen.Profile)
    BackHandler(enabled = !isPrimaryScreen) {
        viewModel.navigateBack()
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (isPrimaryScreen) {
                AppBottomNav(
                    currentScreen = currentScreen,
                    onNavigate = { viewModel.navigateTo(it) }
                )
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentScreen) {
                Screen.Home -> {
                    HomeScreen(
                        user = currentUser,
                        wallet = currentWallet,
                        offers = offers,
                        transactions = transactions,
                        unreadCount = unreadCount,
                        onNavigate = { viewModel.navigateTo(it) },
                        onStartOffer = { viewModel.startOffer(it) },
                        onShowMessage = { viewModel.showMessage(it) }
                    )
                }

                Screen.Offers -> {
                    OffersScreen(
                        offers = offers,
                        providers = providers,
                        availableCoins = currentWallet?.availableCoins ?: 0L,
                        onStartOffer = { viewModel.startOffer(it) }
                    )
                }

                Screen.WalletTab -> {
                    WalletScreen(
                        wallet = currentWallet,
                        transactions = transactions,
                        onNavigate = { viewModel.navigateTo(it) }
                    )
                }

                Screen.Refer -> {
                    ReferScreen(
                        user = currentUser,
                        referrals = referrals,
                        availableCoins = currentWallet?.availableCoins ?: 0L,
                        onShowMessage = { viewModel.showMessage(it) }
                    )
                }

                Screen.Profile -> {
                    ProfileScreen(
                        user = currentUser,
                        wallet = currentWallet,
                        allUsers = allUsers,
                        onNavigate = { viewModel.navigateTo(it) },
                        onSwitchUser = { viewModel.switchUser(it) },
                        onRegisterUser = { name, email, refCode ->
                            viewModel.registerNewUser(name, email, refCode) {}
                        },
                        onShowMessage = { viewModel.showMessage(it) }
                    )
                }

                Screen.VideoReward -> {
                    VideoRewardScreen(
                        wallet = currentWallet,
                        isVideoPlaying = isVideoPlaying,
                        countdown = videoCountdown,
                        onBack = { viewModel.navigateBack() },
                        onWatchVideo = { viewModel.startWatchingVideo() }
                    )
                }

                Screen.Redeem -> {
                    RedeemScreen(
                        wallet = currentWallet,
                        onBack = { viewModel.navigateBack() },
                        onSubmitRedemption = { method, coins, inr, details ->
                            viewModel.submitRedemption(method, coins, inr, details) {
                                viewModel.navigateTo(Screen.History)
                            }
                        },
                        onShowMessage = { viewModel.showMessage(it) }
                    )
                }

                Screen.MyRewards -> {
                    MyRewardsScreen(
                        rewards = myRewards,
                        onBack = { viewModel.navigateBack() },
                        onNavigateRedeem = { viewModel.navigateTo(Screen.Redeem) },
                        onShowMessage = { viewModel.showMessage(it) }
                    )
                }

                Screen.History -> {
                    HistoryScreen(
                        transactions = transactions,
                        withdrawals = withdrawals,
                        onBack = { viewModel.navigateBack() }
                    )
                }

                Screen.Leaderboard -> {
                    LeaderboardScreen(
                        currentUser = currentUser,
                        currentWallet = currentWallet,
                        onBack = { viewModel.navigateBack() }
                    )
                }

                Screen.Notifications -> {
                    NotificationsScreen(
                        notifications = notifications,
                        onBack = { viewModel.navigateBack() },
                        onMarkAllRead = { viewModel.markNotificationsRead() }
                    )
                }

                Screen.Support -> {
                    SupportScreen(
                        tickets = supportTickets,
                        onBack = { viewModel.navigateBack() },
                        onSubmitTicket = { cat, sub, msg ->
                            viewModel.submitSupportTicket(cat, sub, msg) {}
                        }
                    )
                }

                Screen.PlayGames -> {
                    PlayGamesScreen(
                        games = games,
                        availableCoins = currentWallet?.availableCoins ?: 0L,
                        onBack = { viewModel.navigateBack() },
                        onPlayGame = { viewModel.playGame(it) }
                    )
                }

                Screen.GoogleLogin -> {
                    GoogleLoginScreen(
                        onGoogleSignIn = { name, email, photoUrl, refCode ->
                            viewModel.signInWithGoogle(name, email, photoUrl, refCode)
                        }
                    )
                }

                Screen.Admin -> {
                    AdminScreen(
                        isAuthenticated = isAdminAuth,
                        allUsers = allUsers,
                        allWithdrawals = allWithdrawals,
                        allGiftCards = allGiftCards,
                        allProviders = providers,
                        allSettings = allSettings,
                        allAuditLogs = allAuditLogs,
                        totalUsersCount = totalUsersCount,
                        totalCoinsCredited = totalCoinsCredited ?: 0L,
                        totalCoinsRedeemed = totalCoinsRedeemed ?: 0L,
                        pendingWithdrawalsCount = pendingWithdrawalsCount,
                        onBack = { viewModel.navigateBack() },
                        onAuthenticate = { pin -> viewModel.authenticateAdmin(pin) },
                        onSetAdminPin = { pin -> viewModel.setAdminPin(pin) },
                        onLogout = { viewModel.logoutAdmin() },
                        onApprovePayment = { reqId, ref -> viewModel.adminApprovePayment(reqId, ref) },
                        onFulfillGiftCard = { reqId, cardId -> viewModel.adminFulfillGiftCard(reqId, cardId) },
                        onRejectWithdrawal = { reqId, reason -> viewModel.adminRejectWithdrawal(reqId, reason) },
                        onAdjustCoins = { userId, amt, isCredit, reason -> viewModel.adminAdjustCoins(userId, amt, isCredit, reason) },
                        onAddGiftCard = { brand, denom, code, pin -> viewModel.adminAddGiftCard(brand, denom, code, pin) },
                        onUpdateProvider = { prov -> viewModel.adminUpdateProvider(prov) },
                        onUpdateSetting = { key, value -> viewModel.adminUpdateSetting(key, value) },
                        onToggleUserStatus = { userId -> viewModel.adminToggleUserStatus(userId) }
                    )
                }
            }

            // Simulated Provider Callback Dialog
            activeOfferEvent?.let { event ->
                OfferTrackingDialog(
                    event = event,
                    onClose = { viewModel.closeOfferTrackingDialog() },
                    onSimulatePostback = { verified ->
                        viewModel.triggerSimulatedPostback(verified)
                    }
                )
            }
        }
    }
}
