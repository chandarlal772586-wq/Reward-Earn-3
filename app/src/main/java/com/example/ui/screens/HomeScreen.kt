package com.example.ui.screens

import android.content.Context
import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.CoinTransaction
import com.example.data.model.Offer
import com.example.data.model.UserProfile
import com.example.data.model.Wallet
import com.example.ui.components.CoinBadge
import com.example.ui.components.coinsToInr
import com.example.ui.components.formatCoins
import com.example.ui.theme.BrightGold
import com.example.ui.theme.CardBorderStroke
import com.example.ui.theme.DangerCoral
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DarkSurfaceCard
import com.example.ui.theme.DarkSurfaceVariant
import com.example.ui.theme.GoldGradient
import com.example.ui.theme.NeonPurple
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
fun HomeScreen(
    user: UserProfile?,
    wallet: Wallet?,
    offers: List<Offer>,
    transactions: List<CoinTransaction>,
    unreadCount: Int,
    onNavigate: (Screen) -> Unit,
    onStartOffer: (Offer) -> Unit,
    onShowMessage: (String) -> Unit
) {
    val totalCoins = wallet?.totalCoins ?: 1750L
    val availableCoins = wallet?.availableCoins ?: 1250L
    val pendingCoins = wallet?.pendingCoins ?: 250L
    val todayEarnings = wallet?.todayEarnings ?: 320L
    val displayName = user?.displayName ?: "Rahul Sharma"

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
    ) {
        // 1. Top Bar matching Screenshot 1
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .border(2.dp, RadiantGold, CircleShape)
                            .background(PurpleGradient),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = displayName.take(1).uppercase(Locale.ROOT),
                            color = Color.White,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 20.sp
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Welcome back",
                            color = TextMuted,
                            fontSize = 11.sp
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = displayName,
                                color = TextHigh,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "👑", fontSize = 14.sp)
                        }
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    CoinBadge(
                        coins = availableCoins,
                        onClick = { onNavigate(Screen.WalletTab) }
                    )
                    Spacer(modifier = Modifier.width(8.dp))

                    IconButton(
                        onClick = { onNavigate(Screen.Notifications) },
                        modifier = Modifier
                            .size(40.dp)
                            .testTag("home_notifications_btn")
                    ) {
                        BadgedBox(
                            badge = {
                                Badge(
                                    containerColor = DangerCoral,
                                    contentColor = Color.White
                                ) {
                                    Text("3", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = "Notifications",
                                tint = Color.White
                            )
                        }
                    }

                    IconButton(
                        onClick = { onNavigate(Screen.Admin) },
                        modifier = Modifier
                            .size(36.dp)
                            .testTag("home_admin_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Shield,
                            contentDescription = "Admin",
                            tint = RadiantGold
                        )
                    }
                }
            }
        }

        // 2. Featured Reward Earn 3 Banner matching Screenshot 1
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
                    .testTag("featured_banner_card"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(175.dp)
                        .background(
                            Brush.linearGradient(
                                colors = listOf(Color(0xFF2A085C), Color(0xFF13093B), Color(0xFF0D1B48))
                            )
                        )
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_reward_hero),
                        contentDescription = "Reward Earn 3 Banner",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(20.dp)),
                        contentScale = ContentScale.Crop
                    )
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(Color.Transparent, Color(0xFF090D1E).copy(alpha = 0.85f))
                                )
                            )
                    )
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(16.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "👑 Reward Earn 3",
                                color = BrightGold,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                        Text(
                            text = "Play • Complete • Earn",
                            color = Color.White,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Complete Tasks & Get Amazing Rewards",
                            color = TextMedium,
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }

        // 3. Available Balance Card matching Screenshot 1
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .testTag("available_balance_card"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = DarkSurfaceCard),
                border = androidx.compose.foundation.BorderStroke(1.dp, RadiantGold.copy(alpha = 0.5f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Available Balance",
                        color = TextMuted,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(28.dp)
                                    .clip(CircleShape)
                                    .background(RadiantGold),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("₹", color = Color.Black, fontWeight = FontWeight.ExtraBold, fontSize = 16.sp)
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = formatCoins(availableCoins),
                                color = Color.White,
                                fontSize = 28.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "(${coinsToInr(availableCoins)})",
                                color = RadiantGold,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Button(
                            onClick = { onNavigate(Screen.Redeem) },
                            colors = ButtonDefaults.buttonColors(containerColor = BrightGold),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .height(40.dp)
                                .testTag("home_redeem_arrow_btn")
                        ) {
                            Text(
                                text = "Redeem →",
                                color = Color.Black,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 13.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Pending Coins", color = TextMuted, fontSize = 11.sp)
                            Text(formatCoins(pendingCoins), color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                        }
                        Column {
                            Text("Total Earned", color = TextMuted, fontSize = 11.sp)
                            Text(formatCoins(totalCoins), color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                        }
                        Column {
                            Text("Today's Earnings", color = TextMuted, fontSize = 11.sp)
                            Text(formatCoins(todayEarnings), color = SuccessEmerald, fontWeight = FontWeight.ExtraBold, fontSize = 15.sp)
                        }
                    }
                }
            }
        }

        // 4. Six Feature Cards in 3x2 Grid matching Screenshot 1
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Row 1: Super Offers, Play Games, Watch & Earn
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    FeatureGridCard(
                        title = "Super Offers",
                        subtitle = "Apps & Tasks",
                        icon = Icons.Default.Star,
                        gradient = Brush.linearGradient(listOf(Color(0xFF6C5CE7), Color(0xFF3F2B96))),
                        modifier = Modifier.weight(1f),
                        onClick = { onNavigate(Screen.Offers) },
                        testTag = "feature_super_offers"
                    )
                    FeatureGridCard(
                        title = "Play Games",
                        subtitle = "Earn Coins",
                        icon = Icons.Default.SportsEsports,
                        gradient = Brush.linearGradient(listOf(Color(0xFF0984E3), Color(0xFF0652DD))),
                        modifier = Modifier.weight(1f),
                        onClick = { onNavigate(Screen.PlayGames) },
                        testTag = "feature_play_games"
                    )
                    FeatureGridCard(
                        title = "Watch & Earn",
                        subtitle = "Get Reward",
                        icon = Icons.Default.PlayCircle,
                        gradient = Brush.linearGradient(listOf(Color(0xFF00CEC9), Color(0xFF009688))),
                        modifier = Modifier.weight(1f),
                        onClick = { onNavigate(Screen.VideoReward) },
                        testTag = "feature_watch_earn"
                    )
                }

                // Row 2: Refer & Earn, My Wallet, My Rewards
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    FeatureGridCard(
                        title = "Refer & Earn",
                        subtitle = "Get 350 Coins",
                        icon = Icons.Default.People,
                        gradient = Brush.linearGradient(listOf(Color(0xFFE17055), Color(0xFFD63031))),
                        modifier = Modifier.weight(1f),
                        onClick = { onNavigate(Screen.Refer) },
                        testTag = "feature_refer_earn"
                    )
                    FeatureGridCard(
                        title = "My Wallet",
                        subtitle = "Check Balance",
                        icon = Icons.Default.AccountBalanceWallet,
                        gradient = Brush.linearGradient(listOf(Color(0xFFE84393), Color(0xFFB83280))),
                        modifier = Modifier.weight(1f),
                        onClick = { onNavigate(Screen.WalletTab) },
                        testTag = "feature_my_wallet"
                    )
                    FeatureGridCard(
                        title = "My Rewards",
                        subtitle = "View Gift Cards",
                        icon = Icons.Default.CardGiftcard,
                        gradient = Brush.linearGradient(listOf(Color(0xFF00B894), Color(0xFF00897B))),
                        modifier = Modifier.weight(1f),
                        onClick = { onNavigate(Screen.MyRewards) },
                        testTag = "feature_my_rewards"
                    )
                }
            }
        }

        // 5. Recent Activity matching Screenshot 1
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Recent Activity",
                    color = TextHigh,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )
                TextButton(
                    onClick = { onNavigate(Screen.History) },
                    modifier = Modifier.testTag("view_all_activity_btn")
                ) {
                    Text(
                        text = "View All →",
                        color = RadiantGold,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Activity item matching Screenshot 1: "App Install Offer Completed", "Today, 09:15 AM", "+200 Coins"
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = DarkSurfaceCard),
                border = androidx.compose.foundation.BorderStroke(1.dp, CardBorderStroke)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(RoyalPurple.copy(alpha = 0.25f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = RadiantGold,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "App Install Offer Completed",
                                color = TextHigh,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                            Text(
                                text = "Today, 09:15 AM",
                                color = TextMuted,
                                fontSize = 11.sp
                            )
                        }
                    }

                    Text(
                        text = "+200 Coins",
                        color = SuccessEmerald,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 15.sp
                    )
                }
            }
        }

        // Additional transactions from ledger
        items(transactions.take(2)) { txn ->
            if (txn.description != "App Install Offer Completed") {
                val isCredit = txn.type == "CREDIT" || txn.type == "REFUND"
                val dateStr = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault()).format(Date(txn.timestamp))
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    shape = RoundedCornerShape(14.dp),
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
                            Text(txn.description, color = TextHigh, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            Text(dateStr, color = TextMuted, fontSize = 11.sp)
                        }
                        Text(
                            text = (if (isCredit) "+" else "-") + "${txn.coinAmount} Coins",
                            color = if (isCredit) SuccessEmerald else RadiantGold,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
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
fun FeatureGridCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    gradient: Brush,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    testTag: String
) {
    Card(
        modifier = modifier
            .height(115.dp)
            .clickable { onClick() }
            .testTag(testTag),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(gradient)
                .padding(10.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = title,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp,
                    maxLines = 1
                )
                Text(
                    text = subtitle,
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 9.sp,
                    maxLines = 1
                )
            }
        }
    }
}
