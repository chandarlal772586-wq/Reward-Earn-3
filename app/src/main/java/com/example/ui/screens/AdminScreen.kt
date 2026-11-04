package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.CloudQueue
import androidx.compose.material.icons.filled.HistoryEdu
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.ManageAccounts
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AdminAuditLog
import com.example.data.model.AppSetting
import com.example.data.model.GiftCardInventory
import com.example.data.model.OfferProvider
import com.example.data.model.UserProfile
import com.example.data.model.WithdrawalRequest
import com.example.ui.components.StatusChip
import com.example.ui.components.coinsToInr
import com.example.ui.components.formatCoins
import com.example.ui.theme.BrightGold
import com.example.ui.theme.CardBorderStroke
import com.example.ui.theme.DangerCoral
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceCard
import com.example.ui.theme.DarkSurfaceVariant
import com.example.ui.theme.GoldGradient
import com.example.ui.theme.InfoSky
import com.example.ui.theme.RadiantGold
import com.example.ui.theme.RoyalPurple
import com.example.ui.theme.SuccessEmerald
import com.example.ui.theme.TextHigh
import com.example.ui.theme.TextMedium
import com.example.ui.theme.TextMuted
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun AdminScreen(
    isAuthenticated: Boolean,
    allUsers: List<UserProfile>,
    allWithdrawals: List<WithdrawalRequest>,
    allGiftCards: List<GiftCardInventory>,
    allProviders: List<OfferProvider>,
    allSettings: List<AppSetting>,
    allAuditLogs: List<AdminAuditLog>,
    totalUsersCount: Int,
    totalCoinsCredited: Long,
    totalCoinsRedeemed: Long,
    pendingWithdrawalsCount: Int,
    onBack: () -> Unit,
    onAuthenticate: (pin: String) -> Boolean,
    onSetAdminPin: (newPin: String) -> Unit = {},
    onLogout: () -> Unit,
    onApprovePayment: (requestId: String, paymentRef: String) -> Unit,
    onFulfillGiftCard: (requestId: String, cardId: String) -> Unit,
    onRejectWithdrawal: (requestId: String, reason: String) -> Unit,
    onAdjustCoins: (userId: String, amount: Long, isCredit: Boolean, reason: String) -> Unit,
    onAddGiftCard: (brand: String, denomination: Int, code: String, pin: String?) -> Unit,
    onUpdateProvider: (OfferProvider) -> Unit,
    onUpdateSetting: (key: String, value: String) -> Unit,
    onToggleUserStatus: (userId: String) -> Unit,
    modifier: Modifier = Modifier
) {
    if (!isAuthenticated) {
        AdminPinGate(onBack = onBack, onAuthenticate = onAuthenticate)
        return
    }

    var selectedAdminTab by remember { mutableIntStateOf(0) }
    // Bottom admin tabs matching Screenshot 5: Dashboard, Users, Withdrawals, Settings, Logs
    val adminNavItems = listOf(
        Pair("Dashboard", Icons.Default.AdminPanelSettings),
        Pair("Users", Icons.Default.People),
        Pair("Withdrawals", Icons.Default.AccountBalanceWallet),
        Pair("Settings", Icons.Default.Settings),
        Pair("Logs", Icons.Default.HistoryEdu)
    )

    var showPinDialog by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = DarkBackground,
        bottomBar = {
            NavigationBar(
                containerColor = DarkSurface,
                modifier = Modifier.testTag("admin_bottom_nav")
            ) {
                adminNavItems.forEachIndexed { index, (label, icon) ->
                    val selected = selectedAdminTab == index
                    NavigationBarItem(
                        selected = selected,
                        onClick = { selectedAdminTab = index },
                        icon = { Icon(icon, contentDescription = label) },
                        label = { Text(label, fontSize = 10.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = RadiantGold,
                            selectedTextColor = RadiantGold,
                            indicatorColor = RoyalPurple.copy(alpha = 0.25f),
                            unselectedIconColor = TextMuted,
                            unselectedTextColor = TextMuted
                        ),
                        modifier = Modifier.testTag("admin_tab_item_$index")
                    )
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(DarkBackground)
        ) {
            // Header matching Screenshot 5
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = onBack, modifier = Modifier.testTag("admin_back_btn")) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.White
                            )
                        }
                        Icon(
                            imageVector = Icons.Default.Shield,
                            contentDescription = null,
                            tint = RadiantGold,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Admin Panel",
                            color = TextHigh,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }

                    Surface(
                        color = Color(0xFF1B234F),
                        shape = RoundedCornerShape(20.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, RadiantGold)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Icon(Icons.Default.Lock, contentDescription = null, tint = RadiantGold, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Secure Access", color = RadiantGold, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                        }
                    }
                }
            }

            when (selectedAdminTab) {
                0 -> {
                    // Dashboard matching Screenshot 5
                    // Card 1: Create Admin Panel PIN
                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 6.dp)
                                .testTag("create_pin_card"),
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.cardColors(containerColor = DarkSurfaceCard),
                            border = androidx.compose.foundation.BorderStroke(1.dp, CardBorderStroke)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    modifier = Modifier.weight(1f),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(50.dp)
                                            .clip(CircleShape)
                                            .background(
                                                Brush.linearGradient(
                                                    listOf(Color(0xFFFFD700), Color(0xFFE67E22))
                                                )
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(Icons.Default.Shield, contentDescription = null, tint = Color.Black, modifier = Modifier.size(28.dp))
                                    }

                                    Spacer(modifier = Modifier.width(12.dp))

                                    Column {
                                        Text(
                                            text = "Create Admin Panel PIN",
                                            color = TextHigh,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 15.sp
                                        )
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                            text = "Set a secure 4-digit PIN to access admin features",
                                            color = TextMuted,
                                            fontSize = 11.sp
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.width(8.dp))

                                Button(
                                    onClick = { showPinDialog = true },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6C5CE7)),
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier.testTag("create_pin_btn")
                                ) {
                                    Text("Create PIN", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                }
                            }
                        }
                    }

                    // Admin Settings 4-Card Grid matching Screenshot 5
                    item {
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "Admin Settings",
                            color = TextHigh,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                        )
                    }

                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                AdminSettingsGridBox(
                                    title = "Earning\nConfiguration",
                                    icon = Icons.Default.Settings,
                                    bgColor = Color(0xFF1B234F),
                                    modifier = Modifier.weight(1f),
                                    onClick = { selectedAdminTab = 3 },
                                    testTag = "admin_earning_config_card"
                                )
                                AdminSettingsGridBox(
                                    title = "Offer Provider\nManagement",
                                    icon = Icons.Default.CloudQueue,
                                    bgColor = Color(0xFF1B234F),
                                    modifier = Modifier.weight(1f),
                                    onClick = { selectedAdminTab = 3 },
                                    testTag = "admin_provider_card"
                                )
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                AdminSettingsGridBox(
                                    title = "User\nManagement",
                                    icon = Icons.Default.People,
                                    bgColor = Color(0xFF1B234F),
                                    modifier = Modifier.weight(1f),
                                    onClick = { selectedAdminTab = 1 },
                                    testTag = "admin_user_mgmt_card"
                                )
                                AdminSettingsGridBox(
                                    title = "Withdrawal\nManagement",
                                    icon = Icons.Default.AccountBalanceWallet,
                                    bgColor = Color(0xFF1B234F),
                                    modifier = Modifier.weight(1f),
                                    onClick = { selectedAdminTab = 2 },
                                    testTag = "admin_withdrawal_mgmt_card"
                                )
                            }
                        }
                    }

                    // System Overview Metrics
                    item {
                        Spacer(modifier = Modifier.height(14.dp))
                        Text(
                            text = "Live Platform Stats",
                            color = TextHigh,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                        )

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            AdminStatCard("Registered Users", totalUsersCount.toString(), "Active Indian Accounts", InfoSky, Modifier.weight(1f))
                            AdminStatCard("Pending Payouts", pendingWithdrawalsCount.toString(), "Awaiting Action", if (pendingWithdrawalsCount > 0) DangerCoral else SuccessEmerald, Modifier.weight(1f))
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            AdminStatCard("Total Coins Credited", formatCoins(totalCoinsCredited), coinsToInr(totalCoinsCredited), BrightGold, Modifier.weight(1f))
                            AdminStatCard("Total Coins Redeemed", formatCoins(totalCoinsRedeemed), coinsToInr(totalCoinsRedeemed), SuccessEmerald, Modifier.weight(1f))
                        }
                    }
                }

                1 -> {
                    // Users Management
                    item {
                        Text(
                            text = "Registered Users (${allUsers.size})",
                            color = TextHigh,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                        )
                    }

                    items(allUsers) { u ->
                        AdminUserCard(
                            user = u,
                            onAdjustCoins = { amt, isCredit, reason -> onAdjustCoins(u.id, amt, isCredit, reason) },
                            onToggleBlock = { onToggleUserStatus(u.id) }
                        )
                    }
                }

                2 -> {
                    // Withdrawals Management
                    val pending = allWithdrawals.filter { it.status == "PENDING" || it.status == "UNDER_REVIEW" }
                    val completed = allWithdrawals.filter { it.status == "COMPLETED" || it.status == "REJECTED" }

                    item {
                        Text(
                            text = "Pending Withdrawals (${pending.size})",
                            color = TextHigh,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                        )
                    }

                    if (pending.isEmpty()) {
                        item {
                            Box(modifier = Modifier.fillMaxWidth().padding(24.dp), contentAlignment = Alignment.Center) {
                                Text("No pending withdrawals. All payouts completed!", color = SuccessEmerald)
                            }
                        }
                    } else {
                        items(pending) { req ->
                            AdminWithdrawalItem(
                                req = req,
                                allGiftCards = allGiftCards,
                                onApprove = { ref -> onApprovePayment(req.id, ref) },
                                onFulfillCard = { cardId -> onFulfillGiftCard(req.id, cardId) },
                                onReject = { reason -> onRejectWithdrawal(req.id, reason) }
                            )
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(14.dp))
                        Text(
                            text = "Processed Requests (${completed.size})",
                            color = TextHigh,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                        )
                    }

                    items(completed) { req ->
                        AdminPastWithdrawalItem(req = req)
                    }
                }

                3 -> {
                    // Settings & Providers
                    item {
                        AdminSettingsSection(
                            settings = allSettings,
                            onUpdate = onUpdateSetting
                        )
                    }

                    item {
                        Spacer(modifier = Modifier.height(14.dp))
                        AdminGiftCardInventorySection(
                            giftCards = allGiftCards,
                            onAddCard = onAddGiftCard
                        )
                    }

                    item {
                        Spacer(modifier = Modifier.height(14.dp))
                        Text("Partner Offer Providers", color = TextHigh, fontWeight = FontWeight.Bold, fontSize = 16.sp, modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp))
                    }

                    items(allProviders) { prov ->
                        AdminProviderCard(provider = prov, onUpdate = onUpdateProvider)
                    }
                }

                4 -> {
                    // Logs
                    item {
                        Text(
                            text = "Security Audit Logs (${allAuditLogs.size})",
                            color = TextHigh,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                        )
                    }

                    if (allAuditLogs.isEmpty()) {
                        item {
                            Box(modifier = Modifier.fillMaxWidth().padding(24.dp), contentAlignment = Alignment.Center) {
                                Text("No audit logs recorded yet", color = TextMuted)
                            }
                        }
                    } else {
                        items(allAuditLogs.reversed()) { log ->
                            val dateStr = SimpleDateFormat("dd MMM, hh:mm:ss a", Locale.getDefault()).format(Date(log.timestamp))
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 4.dp),
                                shape = RoundedCornerShape(10.dp),
                                colors = CardDefaults.cardColors(containerColor = DarkSurfaceCard),
                                border = androidx.compose.foundation.BorderStroke(0.5.dp, CardBorderStroke)
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(log.action, color = BrightGold, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                        Text(dateStr, color = TextMuted, fontSize = 10.sp)
                                    }
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(log.details, color = TextMedium, fontSize = 12.sp)
                                }
                            }
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(30.dp))
            }
        }
    }

    // Create / Change Admin PIN Dialog
    if (showPinDialog) {
        var newPin by remember { mutableStateOf("") }
        var confirmPin by remember { mutableStateOf("") }
        var error by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = { showPinDialog = false },
            containerColor = DarkSurfaceCard,
            title = { Text("Create Admin Panel PIN", color = TextHigh, fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Text("Set a secure 4-digit PIN to restrict admin console access.", color = TextMuted, fontSize = 12.sp)
                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = newPin,
                        onValueChange = { if (it.length <= 6) newPin = it.filter { c -> c.isDigit() } },
                        label = { Text("Enter 4-Digit PIN") },
                        colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White),
                        modifier = Modifier.fillMaxWidth().testTag("new_admin_pin_input")
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = confirmPin,
                        onValueChange = { if (it.length <= 6) confirmPin = it.filter { c -> c.isDigit() } },
                        label = { Text("Confirm PIN") },
                        colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White),
                        modifier = Modifier.fillMaxWidth().testTag("confirm_admin_pin_input")
                    )

                    if (error.isNotBlank()) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(error, color = DangerCoral, fontSize = 12.sp)
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newPin.length < 4) {
                            error = "PIN must be at least 4 digits"
                            return@Button
                        }
                        if (newPin != confirmPin) {
                            error = "PINs do not match"
                            return@Button
                        }
                        onSetAdminPin(newPin)
                        showPinDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = BrightGold)
                ) {
                    Text("Save PIN", color = Color.Black, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showPinDialog = false }) {
                    Text("Cancel", color = TextMuted)
                }
            }
        )
    }
}

@Composable
fun AdminSettingsGridBox(
    title: String,
    icon: ImageVector,
    bgColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    testTag: String
) {
    Card(
        modifier = modifier
            .height(110.dp)
            .clickable { onClick() }
            .testTag(testTag),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor),
        border = androidx.compose.foundation.BorderStroke(1.dp, CardBorderStroke)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF2A3875)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = BrightGold,
                    modifier = Modifier.size(22.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = title,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                lineHeight = 15.sp
            )
        }
    }
}

@Composable
fun AdminPinGate(
    onBack: () -> Unit,
    onAuthenticate: (pin: String) -> Boolean
) {
    var pin by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(72.dp)
                .clip(CircleShape)
                .background(GoldGradient),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Shield,
                contentDescription = null,
                tint = Color.Black,
                modifier = Modifier.size(40.dp)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text("Admin Security Access", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 22.sp)
        Text(
            "Enter 4 or 6 digit PIN to unlock Admin Console",
            color = TextMuted,
            fontSize = 13.sp,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = pin,
            onValueChange = { if (it.length <= 6) pin = it },
            label = { Text("Admin PIN") },
            visualTransformation = androidx.compose.ui.text.input.PasswordVisualTransformation(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedBorderColor = BrightGold
            ),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("admin_pin_input")
        )

        if (error.isNotBlank()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(error, color = DangerCoral, fontSize = 13.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (onAuthenticate(pin)) {
                    error = ""
                } else {
                    error = "Invalid PIN. Default PIN: 772586 or 1234"
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = BrightGold),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .testTag("admin_login_btn")
        ) {
            Text("Unlock Admin Console", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 15.sp)
        }

        Spacer(modifier = Modifier.height(12.dp))

        TextButton(onClick = onBack) {
            Text("Back to Dashboard", color = TextMuted)
        }
    }
}

@Composable
fun AdminStatCard(
    title: String,
    value: String,
    subtitle: String,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurfaceCard),
        border = androidx.compose.foundation.BorderStroke(1.dp, CardBorderStroke)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(title, color = TextMuted, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(value, color = accentColor, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold)
            Spacer(modifier = Modifier.height(2.dp))
            Text(subtitle, color = TextMedium, fontSize = 11.sp)
        }
    }
}

@Composable
fun AdminUserCard(
    user: UserProfile,
    onAdjustCoins: (amount: Long, isCredit: Boolean, reason: String) -> Unit,
    onToggleBlock: () -> Unit
) {
    var showAdjustDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 5.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurfaceCard),
        border = androidx.compose.foundation.BorderStroke(0.5.dp, CardBorderStroke)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(user.displayName, color = TextHigh, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Text("${user.id} • ${user.email}", color = TextMuted, fontSize = 11.sp)
                }
                StatusChip(user.status)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Ref Code: ${user.referralCode}", color = BrightGold, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                Row {
                    TextButton(onClick = { showAdjustDialog = true }) {
                        Text("Adjust Coins", color = RadiantGold, fontSize = 12.sp)
                    }
                    TextButton(onClick = onToggleBlock) {
                        Text(if (user.status == "ACTIVE") "Block" else "Unblock", color = if (user.status == "ACTIVE") DangerCoral else SuccessEmerald, fontSize = 12.sp)
                    }
                }
            }
        }
    }

    if (showAdjustDialog) {
        var amountText by remember { mutableStateOf("") }
        var isCredit by remember { mutableStateOf(true) }
        var reason by remember { mutableStateOf("Admin adjustment") }

        AlertDialog(
            onDismissRequest = { showAdjustDialog = false },
            containerColor = DarkSurfaceCard,
            title = { Text("Adjust Coins - ${user.displayName}", color = TextHigh, fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(
                            onClick = { isCredit = true },
                            colors = ButtonDefaults.buttonColors(containerColor = if (isCredit) SuccessEmerald else DarkSurfaceVariant),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Credit (+)")
                        }
                        Button(
                            onClick = { isCredit = false },
                            colors = ButtonDefaults.buttonColors(containerColor = if (!isCredit) DangerCoral else DarkSurfaceVariant),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Debit (-)")
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = amountText,
                        onValueChange = { amountText = it.filter { c -> c.isDigit() } },
                        label = { Text("Coins Amount") },
                        colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = reason,
                        onValueChange = { reason = it },
                        label = { Text("Reason") },
                        colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val amt = amountText.toLongOrNull() ?: 0L
                        if (amt > 0) {
                            onAdjustCoins(amt, isCredit, reason)
                            showAdjustDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = BrightGold)
                ) {
                    Text("Submit", color = Color.Black, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showAdjustDialog = false }) {
                    Text("Cancel", color = TextMuted)
                }
            }
        )
    }
}

@Composable
fun AdminWithdrawalItem(
    req: WithdrawalRequest,
    allGiftCards: List<GiftCardInventory>,
    onApprove: (paymentRef: String) -> Unit,
    onFulfillCard: (cardId: String) -> Unit,
    onReject: (reason: String) -> Unit
) {
    var showApproveDialog by remember { mutableStateOf(false) }
    var showRejectDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 5.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurfaceCard),
        border = androidx.compose.foundation.BorderStroke(0.5.dp, CardBorderStroke)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("${req.paymentMethod} • ₹${req.inrAmount}", color = BrightGold, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text("User: ${req.userId} • ${formatCoins(req.coinAmount)} coins", color = TextMuted, fontSize = 11.sp)
                }
                StatusChip(req.status)
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(req.detailsJson, color = TextMedium, fontSize = 12.sp)

            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = { showRejectDialog = true }) {
                    Text("Reject & Refund", color = DangerCoral, fontSize = 12.sp)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = { showApproveDialog = true },
                    colors = ButtonDefaults.buttonColors(containerColor = SuccessEmerald),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Approve Payout", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }

    if (showApproveDialog) {
        var txnRef by remember { mutableStateOf("UPI-IMPS-${System.currentTimeMillis().toString().takeLast(6)}") }
        val matchingCards = allGiftCards.filter { it.status == "AVAILABLE" && it.denominationInr == req.inrAmount.toInt() }

        AlertDialog(
            onDismissRequest = { showApproveDialog = false },
            containerColor = DarkSurfaceCard,
            title = { Text("Approve Withdrawal", color = TextHigh, fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Text("Confirm payment of ₹${req.inrAmount} for ${req.paymentMethod}.", color = TextMedium, fontSize = 12.sp)
                    Spacer(modifier = Modifier.height(10.dp))
                    if (req.paymentMethod == "GIFT_CARD" && matchingCards.isNotEmpty()) {
                        Text("Available Gift Card in Inventory:", color = RadiantGold, fontSize = 12.sp)
                        val card = matchingCards.first()
                        Text("Code: ${card.cardCode} (PIN: ${card.cardPin ?: "N/A"})", color = TextHigh, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    } else {
                        OutlinedTextField(
                            value = txnRef,
                            onValueChange = { txnRef = it },
                            label = { Text("Bank / UPI Reference ID") },
                            colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (req.paymentMethod == "GIFT_CARD" && matchingCards.isNotEmpty()) {
                            onFulfillCard(matchingCards.first().id)
                        } else {
                            onApprove(txnRef)
                        }
                        showApproveDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = SuccessEmerald)
                ) {
                    Text("Confirm Approval", color = Color.White, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showApproveDialog = false }) {
                    Text("Cancel", color = TextMuted)
                }
            }
        )
    }

    if (showRejectDialog) {
        var reason by remember { mutableStateOf("KYC details mismatch / Unverified payment address") }

        AlertDialog(
            onDismissRequest = { showRejectDialog = false },
            containerColor = DarkSurfaceCard,
            title = { Text("Reject & Refund Coins", color = DangerCoral, fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Text("The locked coins (${formatCoins(req.coinAmount)}) will be automatically refunded to the user's available balance.", color = TextMuted, fontSize = 12.sp)
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = reason,
                        onValueChange = { reason = it },
                        label = { Text("Rejection Reason") },
                        colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onReject(reason)
                        showRejectDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = DangerCoral)
                ) {
                    Text("Reject & Refund", color = Color.White, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showRejectDialog = false }) {
                    Text("Cancel", color = TextMuted)
                }
            }
        )
    }
}

@Composable
fun AdminPastWithdrawalItem(req: WithdrawalRequest) {
    val dateStr = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()).format(Date(req.requestTimestamp))
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurfaceCard),
        border = androidx.compose.foundation.BorderStroke(0.5.dp, CardBorderStroke)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("${req.paymentMethod} • ₹${req.inrAmount}", color = TextHigh, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Text("${req.userId} • $dateStr", color = TextMuted, fontSize = 11.sp)
                req.paymentReference?.let { ref ->
                    Text("Ref: $ref", color = SuccessEmerald, fontSize = 11.sp)
                }
                req.rejectionReason?.let { rj ->
                    Text("Reason: $rj", color = DangerCoral, fontSize = 11.sp)
                }
            }
            StatusChip(req.status)
        }
    }
}

@Composable
fun AdminSettingsSection(
    settings: List<AppSetting>,
    onUpdate: (key: String, value: String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurfaceCard),
        border = androidx.compose.foundation.BorderStroke(0.5.dp, CardBorderStroke)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text("System & Earning Configuration", color = BrightGold, fontWeight = FontWeight.Bold, fontSize = 15.sp)
            Spacer(modifier = Modifier.height(10.dp))
            settings.forEach { st ->
                var editingValue by remember(st.value) { mutableStateOf(st.value) }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(st.description, color = TextHigh, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                        Text(st.key, color = TextMuted, fontSize = 10.sp)
                    }
                    OutlinedTextField(
                        value = editingValue,
                        onValueChange = {
                            editingValue = it
                            onUpdate(st.key, it)
                        },
                        colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White),
                        modifier = Modifier.width(100.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun AdminGiftCardInventorySection(
    giftCards: List<GiftCardInventory>,
    onAddCard: (brand: String, denomination: Int, code: String, pin: String?) -> Unit
) {
    var showAddDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurfaceCard),
        border = androidx.compose.foundation.BorderStroke(0.5.dp, CardBorderStroke)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Gift Card Inventory (${giftCards.size})", color = BrightGold, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                Button(
                    onClick = { showAddDialog = true },
                    colors = ButtonDefaults.buttonColors(containerColor = RoyalPurple),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("+ Add Voucher", color = Color.White, fontSize = 11.sp)
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            giftCards.take(5).forEach { gc ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 3.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("${gc.brand} ₹${gc.denominationInr}", color = TextHigh, fontSize = 12.sp)
                    Text(if (gc.status == "AVAILABLE") "Available" else "Claimed", color = if (gc.status == "AVAILABLE") SuccessEmerald else TextMuted, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }

    if (showAddDialog) {
        var brand by remember { mutableStateOf("Google Play") }
        var denom by remember { mutableStateOf("50") }
        var code by remember { mutableStateOf("") }
        var pin by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            containerColor = DarkSurfaceCard,
            title = { Text("Add Digital Gift Card", color = TextHigh, fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    OutlinedTextField(
                        value = brand,
                        onValueChange = { brand = it },
                        label = { Text("Brand (e.g. Google Play, Amazon)") },
                        colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = denom,
                        onValueChange = { denom = it.filter { c -> c.isDigit() } },
                        label = { Text("Denomination (₹)") },
                        colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = code,
                        onValueChange = { code = it },
                        label = { Text("Voucher Code") },
                        colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = pin,
                        onValueChange = { pin = it },
                        label = { Text("Security PIN (Optional)") },
                        colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val d = denom.toIntOrNull() ?: 50
                        if (code.isNotBlank()) {
                            onAddCard(brand, d, code, pin.ifBlank { null })
                            showAddDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = BrightGold)
                ) {
                    Text("Add Card", color = Color.Black, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddDialog = false }) {
                    Text("Cancel", color = TextMuted)
                }
            }
        )
    }
}

@Composable
fun AdminProviderCard(
    provider: OfferProvider,
    onUpdate: (OfferProvider) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurfaceCard),
        border = androidx.compose.foundation.BorderStroke(0.5.dp, CardBorderStroke)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(provider.name, color = TextHigh, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Text("Secret Key: ${provider.secretKey}", color = TextMuted, fontSize = 10.sp)
            }
            Button(
                onClick = { onUpdate(provider.copy(isEnabled = !provider.isEnabled)) },
                colors = ButtonDefaults.buttonColors(containerColor = if (provider.isEnabled) SuccessEmerald else DangerCoral),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(if (provider.isEnabled) "Active" else "Disabled", color = Color.White, fontSize = 11.sp)
            }
        }
    }
}

