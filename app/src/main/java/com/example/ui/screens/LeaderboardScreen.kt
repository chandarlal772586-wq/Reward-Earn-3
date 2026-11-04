package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.UserProfile
import com.example.data.model.Wallet
import com.example.ui.components.coinsToInr
import com.example.ui.components.formatCoins
import com.example.ui.theme.BrightGold
import com.example.ui.theme.CardBorderStroke
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceCard
import com.example.ui.theme.DarkSurfaceVariant
import com.example.ui.theme.PurpleGradient
import com.example.ui.theme.RadiantGold
import com.example.ui.theme.RoyalPurple
import com.example.ui.theme.SuccessEmerald
import com.example.ui.theme.TextHigh
import com.example.ui.theme.TextMedium
import com.example.ui.theme.TextMuted

data class LeaderboardEntry(
    val rank: Int,
    val name: String,
    val state: String,
    val coins: Long
)

@Composable
fun LeaderboardScreen(
    currentUser: UserProfile?,
    currentWallet: Wallet?,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTimeframe by remember { mutableIntStateOf(0) }
    val timeframes = listOf("Today", "This Week", "This Month")

    val entries = listOf(
        LeaderboardEntry(1, "Vikram Malhotra", "Mumbai, MH", 84500),
        LeaderboardEntry(2, "Pooja Hegde", "Bengaluru, KA", 72100),
        LeaderboardEntry(3, "Arun Kumar", "Chennai, TN", 65400),
        LeaderboardEntry(4, "Deepak Joshi", "Delhi, DL", 51200),
        LeaderboardEntry(5, "Ananya Roy", "Kolkata, WB", 43800),
        LeaderboardEntry(6, currentUser?.displayName ?: "Rahul Sharma", "Pune, MH", currentWallet?.totalCoins ?: 18500),
        LeaderboardEntry(7, "Suresh Patel", "Ahmedabad, GJ", 16200),
        LeaderboardEntry(8, "Karan Singh", "Jaipur, RJ", 14800)
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBackground)
    ) {
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack, modifier = Modifier.testTag("leaderboard_back_btn")) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Top Earners Leaderboard",
                    color = TextHigh,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        item {
            TabRow(
                selectedTabIndex = selectedTimeframe,
                containerColor = DarkSurface,
                contentColor = RadiantGold,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTimeframe]),
                        color = RadiantGold
                    )
                }
            ) {
                timeframes.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTimeframe == index,
                        onClick = { selectedTimeframe = index },
                        text = {
                            Text(
                                text = title,
                                fontWeight = if (selectedTimeframe == index) FontWeight.Bold else FontWeight.Normal,
                                color = if (selectedTimeframe == index) RadiantGold else TextMuted
                            )
                        },
                        modifier = Modifier.testTag("lb_tab_$index")
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
        }

        // Current User Rank Banner
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
                    .testTag("current_user_rank_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = DarkSurfaceCard),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, RadiantGold)
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
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(RadiantGold),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("#6", color = Color.Black, fontWeight = FontWeight.ExtraBold, fontSize = 14.sp)
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text("Your Current Rank", color = TextMuted, fontSize = 11.sp)
                            Text(currentUser?.displayName ?: "You", color = TextHigh, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                        }
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = formatCoins(currentWallet?.totalCoins ?: 0) + " Coins",
                            color = BrightGold,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 14.sp
                        )
                        Text(
                            text = coinsToInr(currentWallet?.totalCoins ?: 0),
                            color = SuccessEmerald,
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }

        // Leaderboard List
        itemsIndexed(entries) { index, entry ->
            val isTop3 = entry.rank <= 3
            val medalColor = when (entry.rank) {
                1 -> BrightGold
                2 -> Color(0xFFC0C0C0)
                3 -> Color(0xFFCD7F32)
                else -> DarkSurfaceVariant
            }

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
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(if (isTop3) medalColor else DarkSurfaceVariant),
                            contentAlignment = Alignment.Center
                        ) {
                            if (isTop3) {
                                Icon(
                                    imageVector = Icons.Default.EmojiEvents,
                                    contentDescription = null,
                                    tint = if (entry.rank == 1) Color.Black else Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                            } else {
                                Text("#${entry.rank}", color = TextMedium, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            }
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(entry.name, color = TextHigh, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            Text(entry.state, color = TextMuted, fontSize = 10.sp)
                        }
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = formatCoins(entry.coins) + " Coins",
                            color = if (isTop3) BrightGold else TextHigh,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                        Text(
                            text = coinsToInr(entry.coins),
                            color = SuccessEmerald,
                            fontSize = 11.sp
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
