package com.example.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.SlowMotionVideo
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Wallet
import com.example.ui.components.CoinBadge
import com.example.ui.components.formatCoins
import com.example.ui.theme.BrightGold
import com.example.ui.theme.CardBorderStroke
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

@Composable
fun VideoRewardScreen(
    wallet: Wallet?,
    isVideoPlaying: Boolean,
    countdown: Int,
    onBack: () -> Unit,
    onWatchVideo: () -> Unit,
    modifier: Modifier = Modifier
) {
    val availableCoins = wallet?.availableCoins ?: 0L
    val todayWatched = wallet?.todayVideosWatched ?: 0
    val maxLimit = 10
    val limitReached = todayWatched >= maxLimit

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBackground)
    ) {
        // Header
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("video_back_btn")) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Rewarded Videos",
                        color = TextHigh,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                CoinBadge(coins = availableCoins)
            }
        }

        // Active Video Watching Card
        if (isVideoPlaying) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp)
                        .testTag("video_player_card"),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkSurfaceCard),
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, RadiantGold)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(100.dp)
                                .clip(CircleShape)
                                .background(DarkSurfaceVariant),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                progress = { (15 - countdown) / 15f },
                                modifier = Modifier.size(90.dp),
                                color = RadiantGold,
                                strokeWidth = 6.dp
                            )
                            Text(
                                text = "${countdown}s",
                                color = BrightGold,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 24.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Watching Sponsored Partner Ad...",
                            color = TextHigh,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Reward will be verified by the backend upon timer completion.",
                            color = TextMuted,
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        } else {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp)
                        .testTag("watch_video_promo_card"),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkSurfaceCard),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CardBorderStroke)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(RoyalPurple.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.SlowMotionVideo,
                                contentDescription = null,
                                tint = RadiantGold,
                                modifier = Modifier.size(36.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Watch & Earn +50 Coins",
                            color = TextHigh,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 18.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "User-initiated rewarded videos only. No forced ads or interruptions.",
                            color = TextMuted,
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(16.dp))
                        // Daily limit tracker
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Daily Limit Progress", color = TextMuted, fontSize = 12.sp)
                                Text("$todayWatched of $maxLimit watched", color = BrightGold, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            LinearProgressIndicator(
                                progress = { todayWatched / maxLimit.toFloat() },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(8.dp)
                                    .clip(RoundedCornerShape(4.dp)),
                                color = RadiantGold,
                                trackColor = DarkSurfaceVariant
                            )
                        }

                        Spacer(modifier = Modifier.height(20.dp))
                        Button(
                            onClick = onWatchVideo,
                            enabled = !limitReached,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = RadiantGold,
                                disabledContainerColor = DarkSurfaceVariant
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .testTag("watch_video_action_btn")
                        ) {
                            Icon(imageVector = Icons.Default.PlayCircle, contentDescription = null, tint = if (limitReached) TextMuted else Color.Black)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (limitReached) "Daily Limit Reached (10/10)" else "Watch Video (+50 Coins)",
                                color = if (limitReached) TextMuted else Color.Black,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }
        }

        // Rules & Policy
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = DarkSurfaceCard),
                border = androidx.compose.foundation.BorderStroke(1.dp, CardBorderStroke)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Rewarded Video Guidelines", color = TextHigh, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("• Watching is 100% voluntary. You choose when to watch.", color = TextMedium, fontSize = 12.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("• Ads run for 15 seconds. You must watch till the end to credit coins.", color = TextMedium, fontSize = 12.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("• Daily limit of 10 ads resets at 12:00 AM IST every day.", color = TextMedium, fontSize = 12.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("• Backend verifies genuine ad completions to prevent fraud.", color = TextMedium, fontSize = 12.sp)
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
