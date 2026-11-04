package com.example.ui.screens

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CoinTransaction
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
fun HistoryScreen(
    transactions: List<CoinTransaction>,
    withdrawals: List<WithdrawalRequest>,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Earning History", "Withdrawal Requests")

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
                IconButton(onClick = onBack, modifier = Modifier.testTag("history_back_btn")) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "History & Statements",
                    color = TextHigh,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        item {
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = DarkSurface,
                contentColor = RadiantGold,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        color = RadiantGold
                    )
                }
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = {
                            Text(
                                text = title,
                                fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                                color = if (selectedTab == index) RadiantGold else TextMuted
                            )
                        },
                        modifier = Modifier.testTag("history_tab_$index")
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
        }

        if (selectedTab == 0) {
            // Earning History
            if (transactions.isEmpty()) {
                item {
                    Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                        Text("No earning transactions yet", color = TextMuted)
                    }
                }
            } else {
                items(transactions) { txn ->
                    val isCredit = txn.type == "CREDIT" || txn.type == "REFUND"
                    val dateStr = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()).format(Date(txn.timestamp))

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 4.dp),
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
                                Text(
                                    text = txn.description,
                                    color = TextHigh,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                                Text(
                                    text = (if (isCredit) "+" else "-") + formatCoins(txn.coinAmount) + " Coins",
                                    color = if (isCredit) SuccessEmerald else RadiantGold,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                            }

                            Spacer(modifier = Modifier.height(4.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "$dateStr • ID: ${txn.id}",
                                    color = TextMuted,
                                    fontSize = 11.sp
                                )
                                Text(
                                    text = (if (isCredit) "+" else "-") + "₹" + String.format(Locale("en", "IN"), "%.2f", txn.inrAmount),
                                    color = TextMedium,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }

                            Spacer(modifier = Modifier.height(6.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Source: ${txn.source}", color = RoyalPurple, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                                StatusChip(status = txn.status)
                            }
                        }
                    }
                }
            }
        } else {
            // Withdrawal Requests
            if (withdrawals.isEmpty()) {
                item {
                    Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                        Text("No withdrawal requests found", color = TextMuted)
                    }
                }
            } else {
                items(withdrawals) { req ->
                    val dateStr = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()).format(Date(req.requestTimestamp))

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 6.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = DarkSurfaceCard),
                        border = androidx.compose.foundation.BorderStroke(1.dp, CardBorderStroke)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = req.paymentMethod,
                                        color = TextHigh,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "• ₹${req.inrAmount}",
                                        color = BrightGold,
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 15.sp
                                    )
                                }
                                StatusChip(status = req.status)
                            }

                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Requested: $dateStr • ${formatCoins(req.coinAmount)} Coins Reserved",
                                color = TextMuted,
                                fontSize = 11.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Details: ${req.detailsJson}",
                                color = TextMedium,
                                fontSize = 12.sp
                            )

                            if (!req.paymentReference.isNullOrBlank()) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Payment Ref / UTR: ${req.paymentReference}",
                                    color = SuccessEmerald,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            if (!req.rejectionReason.isNullOrBlank()) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Surface(
                                    color = DangerCoral.copy(alpha = 0.15f),
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(
                                        text = "Rejection Reason: ${req.rejectionReason} (Coins Refunded)",
                                        color = DangerCoral,
                                        fontSize = 11.sp,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }
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
