package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
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
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Referral
import com.example.data.model.UserProfile
import com.example.ui.components.CoinBadge
import com.example.ui.components.StatusChip
import com.example.ui.components.coinsToInr
import com.example.ui.components.formatCoins
import com.example.ui.theme.BrightGold
import com.example.ui.theme.CardBorderStroke
import com.example.ui.theme.DarkBackground
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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ReferScreen(
    user: UserProfile?,
    referrals: List<Referral>,
    availableCoins: Long,
    onShowMessage: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val referralCode = user?.referralCode ?: "REWARD88"
    val referralLink = "https://rewardearn3.app/ref/$referralCode"

    val totalInvited = referrals.size
    val successfulCount = referrals.count { it.status == "COMPLETED" }
    val pendingCount = referrals.count { it.status == "PENDING" }
    val totalReferralCoinsEarned = successfulCount * 2500L

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBackground)
    ) {
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Refer & Earn",
                        color = TextHigh,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(
                        text = "Invite friends & earn ₹25 (2,500 Coins) per referral",
                        color = TextMuted,
                        fontSize = 12.sp
                    )
                }
                CoinBadge(coins = availableCoins)
            }
        }

        // Referral Code Hero Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
                    .testTag("refer_card_hero"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = DarkSurfaceCard),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, RadiantGold.copy(alpha = 0.5f))
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(CircleShape)
                            .background(RadiantGold.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CardGiftcard,
                            contentDescription = null,
                            tint = RadiantGold,
                            modifier = Modifier.size(30.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Your Exclusive Referral Code",
                        color = TextMuted,
                        fontSize = 12.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Surface(
                        color = DarkSurfaceVariant,
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, CardBorderStroke)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = referralCode,
                                color = BrightGold,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.ExtraBold,
                                letterSpacing = 2.sp
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            IconButton(
                                onClick = {
                                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                    val clip = ClipData.newPlainText("Referral Code", referralCode)
                                    clipboard.setPrimaryClip(clip)
                                    onShowMessage("Referral Code $referralCode copied to clipboard!")
                                },
                                modifier = Modifier
                                    .size(32.dp)
                                    .testTag("copy_referral_code_btn")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ContentCopy,
                                    contentDescription = "Copy Code",
                                    tint = RadiantGold,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    Button(
                        onClick = {
                            val sendIntent: Intent = Intent().apply {
                                action = Intent.ACTION_SEND
                                putExtra(Intent.EXTRA_TEXT, "Join Reward Earn 3 to earn real cash! Use my referral code $referralCode to get ₹10 welcome bonus: $referralLink")
                                type = "text/plain"
                            }
                            val shareIntent = Intent.createChooser(sendIntent, "Share Referral Link")
                            context.startActivity(shareIntent)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = RadiantGold),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("share_referral_link_btn")
                    ) {
                        Icon(imageVector = Icons.Default.Share, contentDescription = null, tint = Color.Black)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Share Referral Link & Earn",
                            color = Color.Black,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }

        // Referral Stats Grid
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                ReferralStatCard(
                    title = "Total Invited",
                    value = totalInvited.toString(),
                    caption = "Friends joined",
                    icon = Icons.Default.People,
                    color = InfoSky,
                    modifier = Modifier.weight(1f)
                )
                ReferralStatCard(
                    title = "Completed",
                    value = successfulCount.toString(),
                    caption = "Earned ₹25 bonus",
                    icon = Icons.Default.CheckCircle,
                    color = SuccessEmerald,
                    modifier = Modifier.weight(1f)
                )
                ReferralStatCard(
                    title = "Earnings",
                    value = coinsToInr(totalReferralCoinsEarned),
                    caption = "${formatCoins(totalReferralCoinsEarned)} Coins",
                    icon = Icons.Default.MonetizationOn,
                    color = BrightGold,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // How Refer & Earn Works
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = DarkSurfaceCard),
                border = androidx.compose.foundation.BorderStroke(1.dp, CardBorderStroke)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "How Refer & Earn Works",
                        color = TextHigh,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    RuleStepItem(step = "1", title = "Share your Link", desc = "Send your link or referral code to your friends on WhatsApp or Telegram.")
                    RuleStepItem(step = "2", title = "Friend Registers", desc = "Your friend enters your code and gets ₹10 (1,000 Coins) instant welcome bonus.")
                    RuleStepItem(step = "3", title = "Qualifying Offer", desc = "When your friend finishes 1 Super Offer, you receive ₹25 (2,500 Coins) directly!")
                }
            }
        }

        // Referral History
        item {
            Text(
                text = "Referral History (${referrals.size})",
                color = TextHigh,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }

        if (referrals.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No friends invited yet. Start sharing to earn!", color = TextMuted, fontSize = 13.sp)
                }
            }
        } else {
            items(referrals) { ref ->
                val dateStr = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date(ref.timestamp))
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkSurfaceCard),
                    border = androidx.compose.foundation.BorderStroke(0.5.dp, CardBorderStroke)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = ref.referredUserName,
                                color = TextHigh,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                            Text(
                                text = "Invited: $dateStr • ${if (ref.qualificationOfferCompleted) "Offer Completed" else "Awaiting 1st Offer"}",
                                color = TextMuted,
                                fontSize = 11.sp
                            )
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "+${formatCoins(ref.rewardCoins)} Coins",
                                color = if (ref.status == "COMPLETED") SuccessEmerald else TextMuted,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                            StatusChip(status = ref.status)
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun ReferralStatCard(
    title: String,
    value: String,
    caption: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurfaceCard),
        border = androidx.compose.foundation.BorderStroke(1.dp, CardBorderStroke)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.height(6.dp))
            Text(value, color = TextHigh, fontWeight = FontWeight.ExtraBold, fontSize = 15.sp)
            Text(title, color = TextMuted, fontSize = 10.sp, fontWeight = FontWeight.SemiBold)
            Text(caption, color = color, fontSize = 9.sp)
        }
    }
}

@Composable
fun RuleStepItem(step: String, title: String, desc: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .background(RoyalPurple),
            contentAlignment = Alignment.Center
        ) {
            Text(step, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.width(10.dp))
        Column {
            Text(title, color = TextHigh, fontWeight = FontWeight.Bold, fontSize = 13.sp)
            Text(desc, color = TextMedium, fontSize = 11.sp)
        }
    }
}
