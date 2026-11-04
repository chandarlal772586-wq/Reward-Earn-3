package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Redeem
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
import com.example.data.model.UserReward
import com.example.ui.components.StatusChip
import com.example.ui.theme.BrightGold
import com.example.ui.theme.CardBorderStroke
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DarkSurfaceCard
import com.example.ui.theme.DarkSurfaceVariant
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
fun MyRewardsScreen(
    rewards: List<UserReward>,
    onBack: () -> Unit,
    onNavigateRedeem: () -> Unit,
    onShowMessage: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

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
                IconButton(onClick = onBack, modifier = Modifier.testTag("rewards_back_btn")) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "My Rewards & Vouchers",
                    color = TextHigh,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        if (rewards.isEmpty()) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkSurfaceCard),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CardBorderStroke)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(RadiantGold.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.CardGiftcard, contentDescription = null, tint = RadiantGold, modifier = Modifier.size(36.dp))
                        }
                        Spacer(modifier = Modifier.height(14.dp))
                        Text("No Gift Vouchers Yet", color = TextHigh, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            "Redeem your coins for Amazon, Flipkart, or PhonePe gift cards to receive instant digital codes here.",
                            color = TextMuted,
                            fontSize = 12.sp,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = onNavigateRedeem,
                            colors = ButtonDefaults.buttonColors(containerColor = RadiantGold),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.testTag("redeem_first_gc_btn")
                        ) {
                            Text("Redeem a Gift Card", color = Color.Black, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        } else {
            items(rewards) { reward ->
                val dateStr = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date(reward.deliveredTimestamp))
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                        .testTag("reward_item_${reward.id}"),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkSurfaceCard),
                    border = androidx.compose.foundation.BorderStroke(1.dp, RadiantGold.copy(alpha = 0.4f))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(RoyalPurple.copy(alpha = 0.25f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.CardGiftcard, contentDescription = null, tint = RadiantGold, modifier = Modifier.size(20.dp))
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(reward.title, color = TextHigh, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                                    Text("Delivered on $dateStr", color = TextMuted, fontSize = 11.sp)
                                }
                            }

                            StatusChip(status = reward.status)
                        }

                        Spacer(modifier = Modifier.height(14.dp))
                        // Voucher Code Box
                        Surface(
                            color = DarkSurfaceVariant,
                            shape = RoundedCornerShape(12.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, CardBorderStroke),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text("VOUCHER CODE", color = TextMuted, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                    Text(
                                        text = reward.voucherCode,
                                        color = BrightGold,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        letterSpacing = 1.sp
                                    )
                                    if (!reward.pin.isNullOrBlank()) {
                                        Text("PIN: ${reward.pin}", color = TextMedium, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                                    }
                                }

                                IconButton(
                                    onClick = {
                                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                        val clip = ClipData.newPlainText("Voucher Code", reward.voucherCode)
                                        clipboard.setPrimaryClip(clip)
                                        onShowMessage("Voucher Code copied to clipboard!")
                                    },
                                    modifier = Modifier.testTag("copy_voucher_btn_${reward.id}")
                                ) {
                                    Icon(imageVector = Icons.Default.ContentCopy, contentDescription = "Copy Code", tint = RadiantGold)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Valid Thru: ${reward.expiryDate}", color = TextMuted, fontSize = 11.sp)
                            Text("Req ID: ${reward.withdrawalId}", color = TextMuted, fontSize = 11.sp)
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
