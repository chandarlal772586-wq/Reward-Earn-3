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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CurrencyRupee
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Redeem
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
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
import com.example.data.model.CoinTransaction
import com.example.data.model.Wallet
import com.example.ui.components.coinsToInr
import com.example.ui.components.formatCoins
import com.example.ui.theme.BrightGold
import com.example.ui.theme.CardBorderStroke
import com.example.ui.theme.DarkBackground
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
import com.example.ui.viewmodel.Screen
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun WalletScreen(
    wallet: Wallet?,
    transactions: List<CoinTransaction>,
    onNavigate: (Screen) -> Unit,
    modifier: Modifier = Modifier
) {
    val totalCoins = wallet?.totalCoins ?: 0L
    val availableCoins = wallet?.availableCoins ?: 0L
    val pendingCoins = wallet?.pendingCoins ?: 0L
    val redeemedCoins = wallet?.redeemedCoins ?: 0L

    var selectedFilter by remember { mutableStateOf("ALL") }
    var calcInputCoins by remember { mutableStateOf("5000") }

    val filteredTxns = transactions.filter { txn ->
        when (selectedFilter) {
            "CREDIT" -> txn.type == "CREDIT" || txn.type == "REFUND"
            "DEBIT" -> txn.type == "DEBIT" || txn.type == "RESERVE"
            else -> true
        }
    }

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
                Column {
                    Text(
                        text = "Coin Wallet",
                        color = TextHigh,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(
                        text = "Secure transactional ledger & balance",
                        color = TextMuted,
                        fontSize = 12.sp
                    )
                }

                Button(
                    onClick = { onNavigate(Screen.MyRewards) },
                    colors = ButtonDefaults.buttonColors(containerColor = DarkSurfaceCard),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CardBorderStroke),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.testTag("my_rewards_btn")
                ) {
                    Text("My Rewards 🎁", color = RadiantGold, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        // Primary Wallet Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
                    .testTag("wallet_balance_card"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = DarkSurfaceCard),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, RadiantGold.copy(alpha = 0.5f))
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(text = "Available Coins for Withdrawal", color = TextMuted, fontSize = 12.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.MonetizationOn,
                                contentDescription = null,
                                tint = BrightGold,
                                modifier = Modifier.size(32.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = formatCoins(availableCoins),
                                color = Color.White,
                                fontSize = 32.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }

                        Surface(
                            color = BrightGold.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = coinsToInr(availableCoins),
                                color = BrightGold,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 18.sp,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))
                    Button(
                        onClick = { onNavigate(Screen.Redeem) },
                        colors = ButtonDefaults.buttonColors(containerColor = RadiantGold),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("wallet_withdraw_btn")
                    ) {
                        Icon(imageVector = Icons.Default.Redeem, contentDescription = null, tint = Color.Black)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Withdraw to UPI / Bank / Gift Cards",
                            color = Color.Black,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    // Sub balances 3-column
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        WalletStatBox(
                            title = "Total Earned",
                            coins = totalCoins,
                            icon = Icons.Default.MonetizationOn,
                            tint = RadiantGold
                        )
                        WalletStatBox(
                            title = "Pending Coins",
                            coins = pendingCoins,
                            icon = Icons.Default.HourglassTop,
                            tint = InfoSky
                        )
                        WalletStatBox(
                            title = "Total Redeemed",
                            coins = redeemedCoins,
                            icon = Icons.Default.CheckCircle,
                            tint = SuccessEmerald
                        )
                    }
                }
            }
        }

        // Conversion Rate & Calculator Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .testTag("conversion_calculator_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = DarkSurfaceCard),
                border = androidx.compose.foundation.BorderStroke(1.dp, CardBorderStroke)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Calculate, contentDescription = null, tint = RadiantGold, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Conversion Rate: 1,000 Coins = ₹10",
                            color = TextHigh,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Official rate: 100 Coins = ₹1 INR. Minimum withdrawal is 5,000 Coins (₹50).",
                        color = TextMuted,
                        fontSize = 11.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        OutlinedTextField(
                            value = calcInputCoins,
                            onValueChange = { calcInputCoins = it.filter { char -> char.isDigit() } },
                            label = { Text("Enter Coins") },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = RadiantGold,
                                unfocusedBorderColor = CardBorderStroke,
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            ),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("calc_coins_input")
                        )

                        Spacer(modifier = Modifier.width(14.dp))
                        val inputNum = calcInputCoins.toLongOrNull() ?: 0L
                        Column(horizontalAlignment = Alignment.End) {
                            Text("Equivalent Value", color = TextMuted, fontSize = 11.sp)
                            Text(
                                text = coinsToInr(inputNum),
                                color = SuccessEmerald,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 18.sp
                            )
                        }
                    }
                }
            }
        }

        // Ledger Filter Tabs
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Transaction Ledger",
                    color = TextHigh,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )

                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    listOf("ALL", "CREDIT", "DEBIT").forEach { tab ->
                        val selected = selectedFilter == tab
                        Surface(
                            modifier = Modifier
                                .clickable { selectedFilter = tab }
                                .testTag("ledger_filter_$tab"),
                            shape = RoundedCornerShape(14.dp),
                            color = if (selected) RoyalPurple else DarkSurfaceVariant
                        ) {
                            Text(
                                text = tab,
                                color = if (selected) Color.White else TextMedium,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }
        }

        // Ledger items
        if (filteredTxns.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No transactions found", color = TextMuted, fontSize = 13.sp)
                }
            }
        } else {
            items(filteredTxns) { txn ->
                val isCredit = txn.type == "CREDIT" || txn.type == "REFUND"
                val dateStr = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()).format(Date(txn.timestamp))

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                        .testTag("txn_row_${txn.id}"),
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
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = txn.description,
                                color = TextHigh,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "$dateStr • ID: ${txn.id}",
                                color = TextMuted,
                                fontSize = 11.sp
                            )
                            if (txn.referenceId.isNotBlank()) {
                                Text(
                                    text = "Ref: ${txn.referenceId}",
                                    color = RoyalPurple,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = (if (isCredit) "+" else "-") + formatCoins(txn.coinAmount) + " Coins",
                                color = if (isCredit) SuccessEmerald else RadiantGold,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 13.sp
                            )
                            Text(
                                text = (if (isCredit) "+" else "-") + "₹" + String.format(Locale("en", "IN"), "%.2f", txn.inrAmount),
                                color = TextMuted,
                                fontSize = 11.sp
                            )
                            Surface(
                                color = (if (txn.status == "COMPLETED") SuccessEmerald else RadiantGold).copy(alpha = 0.15f),
                                shape = RoundedCornerShape(4.dp),
                                modifier = Modifier.padding(top = 2.dp)
                            ) {
                                Text(
                                    text = txn.status,
                                    color = if (txn.status == "COMPLETED") SuccessEmerald else RadiantGold,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                )
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

@Composable
fun WalletStatBox(
    title: String,
    coins: Long,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    tint: Color
) {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = icon, contentDescription = null, tint = tint, modifier = Modifier.size(14.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text(title, color = TextMuted, fontSize = 10.sp)
        }
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = formatCoins(coins),
            color = TextHigh,
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp
        )
        Text(
            text = coinsToInr(coins),
            color = tint,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}
