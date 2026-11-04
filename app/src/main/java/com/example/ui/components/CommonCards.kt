package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
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
import com.example.data.model.OfferEvent
import com.example.ui.theme.BrightGold
import com.example.ui.theme.CardBorderStroke
import com.example.ui.theme.CardGradient
import com.example.ui.theme.DangerCoral
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceCard
import com.example.ui.theme.DarkSurfaceVariant
import com.example.ui.theme.GoldGradient
import com.example.ui.theme.InfoSky
import com.example.ui.theme.PurpleGradient
import com.example.ui.theme.RadiantGold
import com.example.ui.theme.RoyalPurple
import com.example.ui.theme.SuccessEmerald
import com.example.ui.theme.TextHigh
import com.example.ui.theme.TextMedium
import com.example.ui.theme.TextMuted
import com.example.ui.theme.WarningAmber
import java.text.NumberFormat
import java.util.Locale

fun formatCoins(coins: Long): String {
    return NumberFormat.getNumberInstance(Locale("en", "IN")).format(coins)
}

fun coinsToInr(coins: Long): String {
    val inr = coins / 100.0
    return "₹" + String.format(Locale("en", "IN"), "%.2f", inr)
}

@Composable
fun CoinBadge(
    coins: Long,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null
) {
    Surface(
        color = DarkSurfaceCard,
        shape = RoundedCornerShape(20.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, RadiantGold.copy(alpha = 0.5f)),
        modifier = modifier
            .testTag("coin_badge")
            .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
        ) {
            Icon(
                imageVector = Icons.Default.MonetizationOn,
                contentDescription = "Coins",
                tint = BrightGold,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = formatCoins(coins),
                color = BrightGold,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp
            )
        }
    }
}

@Composable
fun AppHeader(
    userName: String,
    coins: Long,
    unreadCount: Int,
    onBalanceClick: () -> Unit,
    onNotificationsClick: () -> Unit,
    onAdminClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(PurpleGradient),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = userName.take(1).uppercase(Locale.ROOT),
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = "Welcome back,",
                    color = TextMuted,
                    fontSize = 11.sp
                )
                Text(
                    text = userName,
                    color = TextHigh,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            }
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            CoinBadge(coins = coins, onClick = onBalanceClick)
            Spacer(modifier = Modifier.width(8.dp))

            IconButton(
                onClick = onNotificationsClick,
                modifier = Modifier
                    .size(40.dp)
                    .testTag("notifications_button")
            ) {
                BadgedBox(
                    badge = {
                        if (unreadCount > 0) {
                            Badge(
                                containerColor = DangerCoral,
                                contentColor = Color.White
                            ) {
                                Text(unreadCount.toString(), fontSize = 10.sp)
                            }
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "Notifications",
                        tint = TextMedium
                    )
                }
            }

            IconButton(
                onClick = onAdminClick,
                modifier = Modifier
                    .size(40.dp)
                    .testTag("admin_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Shield,
                    contentDescription = "Admin Panel",
                    tint = RadiantGold
                )
            }
        }
    }
}

@Composable
fun StatusChip(status: String, modifier: Modifier = Modifier) {
    val (bgColor, textColor, label) = when (status.uppercase(Locale.ROOT)) {
        "COMPLETED", "ACTIVE", "VERIFIED_CREDITED" -> Triple(SuccessEmerald.copy(alpha = 0.15f), SuccessEmerald, "Completed")
        "PENDING", "PENDING_CALLBACK", "UNDER_REVIEW" -> Triple(WarningAmber.copy(alpha = 0.15f), WarningAmber, "Pending")
        "PAYMENT_PROCESSING" -> Triple(InfoSky.copy(alpha = 0.15f), InfoSky, "Processing")
        "REJECTED", "SUSPENDED" -> Triple(DangerCoral.copy(alpha = 0.15f), DangerCoral, "Rejected")
        "AVAILABLE" -> Triple(RoyalPurple.copy(alpha = 0.15f), RoyalPurple, "Available")
        else -> Triple(DarkSurfaceVariant, TextMedium, status)
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(bgColor)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = label,
            color = textColor,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun SectionHeader(
    title: String,
    subtitle: String? = null,
    actionLabel: String? = null,
    onActionClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = title,
                color = TextHigh,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold
            )
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    color = TextMuted,
                    fontSize = 12.sp
                )
            }
        }
        if (actionLabel != null && onActionClick != null) {
            TextButton(
                onClick = onActionClick,
                modifier = Modifier.testTag("section_action_${title.lowercase(Locale.ROOT)}")
            ) {
                Text(
                    text = actionLabel,
                    color = RadiantGold,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
fun OfferTrackingDialog(
    event: OfferEvent,
    onClose: () -> Unit,
    onSimulatePostback: (verified: Boolean) -> Unit
) {
    AlertDialog(
        onDismissRequest = onClose,
        containerColor = DarkSurface,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.MonetizationOn,
                    contentDescription = null,
                    tint = BrightGold,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Offer Tracking Active", color = TextHigh, fontWeight = FontWeight.Bold)
            }
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "A live tracking session has been registered with the offer provider.",
                    color = TextMedium,
                    fontSize = 13.sp
                )
                Spacer(modifier = Modifier.height(12.dp))

                Card(
                    colors = CardDefaults.cardColors(containerColor = DarkSurfaceCard),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CardBorderStroke)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Tracking ID:", color = TextMuted, fontSize = 12.sp)
                            Text(event.id, color = TextHigh, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Provider:", color = TextMuted, fontSize = 12.sp)
                            Text(event.providerId.uppercase(Locale.ROOT), color = RoyalPurple, fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Reward Coins:", color = TextMuted, fontSize = 12.sp)
                            Text("+${formatCoins(event.payoutCoins)} (${coinsToInr(event.payoutCoins)})", color = BrightGold, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))
                Text(
                    text = "Server Postback Verification:",
                    color = TextHigh,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp
                )
                Text(
                    text = "Test the genuine server-side callback to credit coins atomically into your wallet or simulate rejection.",
                    color = TextMuted,
                    fontSize = 12.sp
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onSimulatePostback(true) },
                colors = ButtonDefaults.buttonColors(containerColor = SuccessEmerald),
                modifier = Modifier.testTag("verify_postback_button")
            ) {
                Text("Simulate Verified Callback", color = Color.Black, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = { onSimulatePostback(false) },
                colors = ButtonDefaults.outlinedButtonColors(contentColor = DangerCoral),
                modifier = Modifier.testTag("reject_postback_button")
            ) {
                Text("Simulate Rejection")
            }
        }
    )
}
