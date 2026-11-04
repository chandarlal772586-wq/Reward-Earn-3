package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Policy
import androidx.compose.material.icons.filled.Redeem
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.UserProfile
import com.example.data.model.Wallet
import com.example.ui.components.CoinBadge
import com.example.ui.components.StatusChip
import com.example.ui.components.coinsToInr
import com.example.ui.components.formatCoins
import com.example.ui.theme.BrightGold
import com.example.ui.theme.CardBorderStroke
import com.example.ui.theme.DangerCoral
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DarkSurfaceCard
import com.example.ui.theme.DarkSurfaceVariant
import com.example.ui.theme.InfoSky
import com.example.ui.theme.PurpleGradient
import com.example.ui.theme.RadiantGold
import com.example.ui.theme.RoyalPurple
import com.example.ui.theme.SuccessEmerald
import com.example.ui.theme.TextHigh
import com.example.ui.theme.TextMedium
import com.example.ui.theme.TextMuted
import com.example.ui.viewmodel.Screen
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ProfileScreen(
    user: UserProfile?,
    wallet: Wallet?,
    allUsers: List<UserProfile>,
    onNavigate: (Screen) -> Unit,
    onSwitchUser: (String) -> Unit,
    onRegisterUser: (name: String, email: String, refCode: String) -> Unit,
    onShowMessage: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var showRegisterDialog by remember { mutableStateOf(false) }
    var showSwitchDialog by remember { mutableStateOf(false) }
    var showPolicyDialog by remember { mutableStateOf(false) }

    val regDateStr = if (user != null) {
        SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(Date(user.createdAt))
    } else "Recent"

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBackground)
    ) {
        // Title
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "My Profile",
                    color = TextHigh,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold
                )
                CoinBadge(coins = wallet?.availableCoins ?: 0L)
            }
        }

        // Profile Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
                    .testTag("user_profile_card"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = DarkSurfaceCard),
                border = androidx.compose.foundation.BorderStroke(1.dp, CardBorderStroke)
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .background(PurpleGradient),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = (user?.displayName?.take(1) ?: "U").uppercase(Locale.ROOT),
                            color = Color.White,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 30.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = user?.displayName ?: "Guest User",
                            color = TextHigh,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(
                            imageVector = Icons.Default.Verified,
                            contentDescription = "Verified",
                            tint = InfoSky,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Text(
                        text = user?.email ?: "user@rewardearn3.app",
                        color = TextMedium,
                        fontSize = 12.sp
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Surface(
                        color = DarkSurfaceVariant,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("User ID: ${user?.id ?: ""}", color = TextHigh, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                Text("Member since $regDateStr", color = TextMuted, fontSize = 11.sp)
                            }
                            StatusChip(status = user?.status ?: "ACTIVE")
                        }
                    }
                }
            }
        }

        // Action Menu
        item {
            Text(
                text = "Account & Rewards",
                color = TextHigh,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
            )
        }

        item {
            ProfileMenuItem(
                title = "My Rewards & Gift Vouchers",
                subtitle = "View delivered codes for Amazon, Flipkart, PhonePe",
                icon = Icons.Default.CardGiftcard,
                tint = RadiantGold,
                onClick = { onNavigate(Screen.MyRewards) },
                testTag = "menu_my_rewards"
            )
        }

        item {
            ProfileMenuItem(
                title = "Redeem History",
                subtitle = "Track UPI, IMPS, and gift card payout requests",
                icon = Icons.Default.Redeem,
                tint = SuccessEmerald,
                onClick = { onNavigate(Screen.History) },
                testTag = "menu_redeem_history"
            )
        }

        item {
            ProfileMenuItem(
                title = "Help & Support Tickets",
                subtitle = "Submit queries and view admin replies",
                icon = Icons.Default.HelpOutline,
                tint = InfoSky,
                onClick = { onNavigate(Screen.Support) },
                testTag = "menu_support"
            )
        }

        item {
            ProfileMenuItem(
                title = "Admin Panel",
                subtitle = "Manage withdrawals, users, gift cards & audit logs",
                icon = Icons.Default.AdminPanelSettings,
                tint = RadiantGold,
                onClick = { onNavigate(Screen.Admin) },
                testTag = "menu_admin_panel"
            )
        }

        item {
            Text(
                text = "User Sessions & Legal",
                color = TextHigh,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
            )
        }

        item {
            ProfileMenuItem(
                title = "Register New Account",
                subtitle = "Create another account profile to test referral bonuses",
                icon = Icons.Default.PersonAdd,
                tint = RoyalPurple,
                onClick = { showRegisterDialog = true },
                testTag = "menu_register_account"
            )
        }

        item {
            ProfileMenuItem(
                title = "Google Login & Onboarding",
                subtitle = "Sign in with Google Account (+500 Coins bonus)",
                icon = Icons.Default.Lock,
                tint = BrightGold,
                onClick = { onNavigate(Screen.GoogleLogin) },
                testTag = "menu_google_login"
            )
        }

        item {
            ProfileMenuItem(
                title = "Switch Active User",
                subtitle = "Currently active: ${user?.displayName}",
                icon = Icons.Default.SwapHoriz,
                tint = TextMedium,
                onClick = { showSwitchDialog = true },
                testTag = "menu_switch_user"
            )
        }

        item {
            ProfileMenuItem(
                title = "Terms & Privacy Policy",
                subtitle = "Indian RBI & Digital Services Compliance",
                icon = Icons.Default.Policy,
                tint = TextMuted,
                onClick = { showPolicyDialog = true },
                testTag = "menu_terms_policy"
            )
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    // Register User Dialog
    if (showRegisterDialog) {
        var newName by remember { mutableStateOf("") }
        var newEmail by remember { mutableStateOf("") }
        var newRefCode by remember { mutableStateOf(user?.referralCode ?: "") }

        AlertDialog(
            onDismissRequest = { showRegisterDialog = false },
            containerColor = DarkSurfaceCard,
            title = { Text("Register New Account", color = TextHigh, fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Text(
                        "Create a new verified user account with signup bonus (+500 Coins) and optional referral linkage.",
                        color = TextMuted,
                        fontSize = 12.sp
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = newName,
                        onValueChange = { newName = it },
                        label = { Text("Full Name") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = RadiantGold
                        ),
                        modifier = Modifier.fillMaxWidth().testTag("reg_name_input")
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = newEmail,
                        onValueChange = { newEmail = it },
                        label = { Text("Email Address") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = RadiantGold
                        ),
                        modifier = Modifier.fillMaxWidth().testTag("reg_email_input")
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = newRefCode,
                        onValueChange = { newRefCode = it },
                        label = { Text("Referral Code (Optional)") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = RadiantGold
                        ),
                        modifier = Modifier.fillMaxWidth().testTag("reg_refcode_input")
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newName.isNotBlank() && newEmail.isNotBlank()) {
                            onRegisterUser(newName, newEmail, newRefCode)
                            showRegisterDialog = false
                        } else {
                            onShowMessage("Please enter name and email")
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = RadiantGold),
                    modifier = Modifier.testTag("submit_registration_btn")
                ) {
                    Text("Register & Switch", color = Color.Black, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showRegisterDialog = false }) {
                    Text("Cancel", color = TextMuted)
                }
            }
        )
    }

    // Switch User Dialog
    if (showSwitchDialog) {
        AlertDialog(
            onDismissRequest = { showSwitchDialog = false },
            containerColor = DarkSurfaceCard,
            title = { Text("Switch User Account", color = TextHigh, fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    allUsers.forEach { u ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onSwitchUser(u.id)
                                    showSwitchDialog = false
                                }
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(PurpleGradient),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(u.displayName.take(1), color = Color.White, fontWeight = FontWeight.Bold)
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(u.displayName, color = TextHigh, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                Text("${u.id} • ${u.email}", color = TextMuted, fontSize = 11.sp)
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showSwitchDialog = false }) {
                    Text("Close", color = RadiantGold)
                }
            }
        )
    }

    // Terms & Privacy Policy Dialog
    if (showPolicyDialog) {
        AlertDialog(
            onDismissRequest = { showPolicyDialog = false },
            containerColor = DarkSurfaceCard,
            title = { Text("Terms & Privacy Policy", color = TextHigh, fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Text(
                        text = "Reward Earn 3 Compliance & Security Guidelines:\n\n" +
                                "1. Currency & Conversion: All rewards are calculated in Indian Rupees (INR ₹) at the rate of 1,000 Coins = ₹10.\n\n" +
                                "2. Payout Verification: UPI and IMPS withdrawals require genuine KYC-compliant Indian bank accounts. We never ask for UPI PINs, ATM PINs, OTPs, or passwords.\n\n" +
                                "3. Fraud Prevention: Self-referrals, multiple automated accounts, emulator manipulation, and fake offer completion callbacks are strictly prohibited and result in permanent account suspension.",
                        color = TextMedium,
                        fontSize = 12.sp
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = { showPolicyDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = RoyalPurple)
                ) {
                    Text("I Agree", color = Color.White)
                }
            }
        )
    }
}

@Composable
fun ProfileMenuItem(
    title: String,
    subtitle: String,
    icon: ImageVector,
    tint: Color,
    onClick: () -> Unit,
    testTag: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .clickable { onClick() }
            .testTag(testTag),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurfaceCard),
        border = androidx.compose.foundation.BorderStroke(0.5.dp, CardBorderStroke)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(tint.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = tint, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, color = TextHigh, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                Text(subtitle, color = TextMuted, fontSize = 11.sp)
            }
        }
    }
}
