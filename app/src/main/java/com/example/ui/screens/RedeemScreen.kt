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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import com.example.ui.theme.InfoSky
import com.example.ui.theme.RadiantGold
import com.example.ui.theme.RoyalPurple
import com.example.ui.theme.SuccessEmerald
import com.example.ui.theme.TextHigh
import com.example.ui.theme.TextMedium
import com.example.ui.theme.TextMuted

@Composable
fun RedeemScreen(
    wallet: Wallet?,
    onBack: () -> Unit,
    onSubmitRedemption: (method: String, coins: Long, inr: Double, details: String) -> Unit,
    onShowMessage: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val availableCoins = wallet?.availableCoins ?: 1250L

    var selectedTab by remember { mutableStateOf("UPI Cash") }
    val tabs = listOf("UPI Cash", "Bank Transfer", "Gift Cards")

    // UPI States
    var upiId by remember { mutableStateOf("user@okaxis") }
    var selectedUpiAmount by remember { mutableIntStateOf(50) } // ₹50, ₹100, ₹200, ₹500

    // Bank Transfer States
    var accHolder by remember { mutableStateOf("Rahul Sharma") }
    var bankName by remember { mutableStateOf("HDFC Bank") }
    var accNumber by remember { mutableStateOf("50100492819201") }
    var ifscCode by remember { mutableStateOf("HDFC0001024") }
    var selectedBankAmount by remember { mutableIntStateOf(200) }

    // Gift Cards States
    var selectedGiftBrand by remember { mutableStateOf("GOOGLE_PLAY") }
    var selectedGiftAmount by remember { mutableIntStateOf(50) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBackground)
    ) {
        // Header matching Screenshot 4
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("redeem_back_btn")) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Redeem",
                        color = TextHigh,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                CoinBadge(coins = availableCoins)
            }
        }

        // 3 Tabs matching Screenshot 4: UPI Cash (active yellow), Bank Transfer, Gift Cards
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                tabs.forEach { tab ->
                    val isSelected = selectedTab == tab
                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { selectedTab = tab }
                            .testTag("redeem_tab_$tab"),
                        shape = RoundedCornerShape(12.dp),
                        color = if (isSelected) BrightGold else Color(0xFF1B234F),
                        border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) BrightGold else CardBorderStroke)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = tab,
                                color = if (isSelected) Color.Black else TextMedium,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
        }

        when (selectedTab) {
            "UPI Cash" -> {
                // Main UPI Section matching Screenshot 4
                item {
                    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            // Left Card: UPI form
                            Card(
                                modifier = Modifier
                                    .weight(1.8f)
                                    .testTag("upi_withdrawal_card"),
                                shape = RoundedCornerShape(18.dp),
                                colors = CardDefaults.cardColors(containerColor = DarkSurfaceCard),
                                border = androidx.compose.foundation.BorderStroke(1.dp, CardBorderStroke)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(Color.White)
                                                .padding(horizontal = 8.dp, vertical = 4.dp)
                                        ) {
                                            Text(
                                                text = "UPI ▸",
                                                color = Color(0xFF00796B),
                                                fontWeight = FontWeight.ExtraBold,
                                                fontSize = 14.sp
                                            )
                                        }
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Column {
                                            Text("UPI Cash Withdrawal", color = TextHigh, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                            Text("Get instant money in your UPI account", color = TextMuted, fontSize = 10.sp)
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(12.dp))
                                    OutlinedTextField(
                                        value = upiId,
                                        onValueChange = { upiId = it.trim() },
                                        label = { Text("Enter UPI ID (e.g. user@okaxis)") },
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedTextColor = Color.White,
                                            unfocusedTextColor = Color.White,
                                            focusedBorderColor = RadiantGold
                                        ),
                                        shape = RoundedCornerShape(12.dp),
                                        modifier = Modifier.fillMaxWidth().testTag("upi_input_field")
                                    )

                                    Spacer(modifier = Modifier.height(12.dp))
                                    // Row of Amount Pills matching Screenshot 4: ₹50, ₹100, ₹200, ₹500
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        listOf(50, 100, 200, 500).forEach { amt ->
                                            val isSelected = selectedUpiAmount == amt
                                            Surface(
                                                modifier = Modifier
                                                    .weight(1f)
                                                    .clickable { selectedUpiAmount = amt }
                                                    .testTag("upi_amt_$amt"),
                                                shape = RoundedCornerShape(10.dp),
                                                color = if (isSelected) BrightGold else Color(0xFF1B234F),
                                                border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) BrightGold else CardBorderStroke)
                                            ) {
                                                Box(
                                                    modifier = Modifier.padding(vertical = 10.dp),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    Text(
                                                        text = "₹$amt",
                                                        color = if (isSelected) Color.Black else Color.White,
                                                        fontWeight = FontWeight.ExtraBold,
                                                        fontSize = 13.sp
                                                    )
                                                }
                                            }
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(10.dp))
                                    val requiredCoins = selectedUpiAmount * 100L
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text("Required Coins: ", color = TextMuted, fontSize = 12.sp)
                                        Icon(Icons.Default.MonetizationOn, contentDescription = null, tint = BrightGold, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(3.dp))
                                        Text(
                                            text = formatCoins(requiredCoins),
                                            color = BrightGold,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 13.sp
                                        )
                                    }
                                }
                            }

                            // Right Card: Withdrawal Limit matching Screenshot 4
                            Card(
                                modifier = Modifier
                                    .weight(1.2f)
                                    .testTag("limit_badge_card"),
                                shape = RoundedCornerShape(18.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.Transparent)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(
                                            Brush.verticalGradient(
                                                listOf(Color(0xFF6C5CE7), Color(0xFF3F2B96), Color(0xFF120B44))
                                            )
                                        )
                                        .padding(14.dp)
                                ) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text("Your Withdrawal Limit", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.SemiBold)
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text("₹5,000", color = BrightGold, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold)
                                        Text("Per Request", color = Color.White.copy(alpha = 0.7f), fontSize = 10.sp)

                                        Spacer(modifier = Modifier.height(14.dp))
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text("✔", color = SuccessEmerald, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text("Secure & Fast", color = Color.White, fontSize = 10.sp)
                                        }
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text("✔", color = SuccessEmerald, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text("Direct to UPI", color = Color.White, fontSize = 10.sp)
                                        }
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Full width yellow button matching Screenshot 4
                        val upiCoinsNeeded = selectedUpiAmount * 100L
                        Button(
                            onClick = {
                                if (upiId.isBlank() || !upiId.contains("@")) {
                                    onShowMessage("Please enter a valid UPI ID (e.g. mobile@upi)")
                                    return@Button
                                }
                                if (availableCoins < upiCoinsNeeded) {
                                    onShowMessage("Insufficient coin balance ($upiCoinsNeeded coins needed)")
                                    return@Button
                                }
                                onSubmitRedemption("UPI", upiCoinsNeeded, selectedUpiAmount.toDouble(), upiId)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = BrightGold),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .testTag("request_withdrawal_btn")
                        ) {
                            Text(
                                text = "Request Withdrawal",
                                color = Color.Black,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 15.sp
                            )
                        }
                    }
                }
            }

            "Bank Transfer" -> {
                item {
                    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.cardColors(containerColor = DarkSurfaceCard),
                            border = androidx.compose.foundation.BorderStroke(1.dp, CardBorderStroke)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text("Direct Bank Transfer (IMPS)", color = TextHigh, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                                Text("Payout processed directly to any verified Indian bank account.", color = TextMuted, fontSize = 11.sp)

                                Spacer(modifier = Modifier.height(14.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    listOf(200, 500, 1000, 2000).forEach { amt ->
                                        val isSelected = selectedBankAmount == amt
                                        Surface(
                                            modifier = Modifier
                                                .weight(1f)
                                                .clickable { selectedBankAmount = amt },
                                            shape = RoundedCornerShape(10.dp),
                                            color = if (isSelected) BrightGold else Color(0xFF1B234F),
                                            border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) BrightGold else CardBorderStroke)
                                        ) {
                                            Box(modifier = Modifier.padding(vertical = 10.dp), contentAlignment = Alignment.Center) {
                                                Text("₹$amt", color = if (isSelected) Color.Black else Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                            }
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(14.dp))
                                OutlinedTextField(
                                    value = accHolder,
                                    onValueChange = { accHolder = it },
                                    label = { Text("Account Holder Name") },
                                    colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White),
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                OutlinedTextField(
                                    value = bankName,
                                    onValueChange = { bankName = it },
                                    label = { Text("Bank Name") },
                                    colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White),
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                OutlinedTextField(
                                    value = accNumber,
                                    onValueChange = { accNumber = it },
                                    label = { Text("Bank Account Number") },
                                    colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White),
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                OutlinedTextField(
                                    value = ifscCode,
                                    onValueChange = { ifscCode = it.uppercase() },
                                    label = { Text("IFSC Code (e.g. HDFC0001024)") },
                                    colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White),
                                    modifier = Modifier.fillMaxWidth()
                                )

                                Spacer(modifier = Modifier.height(16.dp))
                                val bankCoinsNeeded = selectedBankAmount * 100L
                                Button(
                                    onClick = {
                                        if (accHolder.isBlank() || accNumber.isBlank() || ifscCode.isBlank()) {
                                            onShowMessage("Please enter complete bank details")
                                            return@Button
                                        }
                                        if (availableCoins < bankCoinsNeeded) {
                                            onShowMessage("Insufficient coins ($bankCoinsNeeded coins needed)")
                                            return@Button
                                        }
                                        val details = "$accHolder | $bankName | A/C: $accNumber | IFSC: $ifscCode"
                                        onSubmitRedemption("IMPS", bankCoinsNeeded, selectedBankAmount.toDouble(), details)
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = BrightGold),
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier.fillMaxWidth().height(48.dp)
                                ) {
                                    Text("Request Bank Transfer ₹$selectedBankAmount", color = Color.Black, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }

            "Gift Cards" -> {
                item {
                    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                        // Brand Selector (Google Play, Amazon, Flipkart, PhonePe)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            listOf(
                                Pair("GOOGLE_PLAY", "Google Play"),
                                Pair("AMAZON", "Amazon Pay"),
                                Pair("FLIPKART", "Flipkart"),
                                Pair("PHONEPE", "PhonePe")
                            ).forEach { (b, label) ->
                                val isSelected = selectedGiftBrand == b
                                Surface(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clickable { selectedGiftBrand = b }
                                        .testTag("gift_brand_$b"),
                                    shape = RoundedCornerShape(10.dp),
                                    color = if (isSelected) BrightGold else Color(0xFF1B234F),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) BrightGold else CardBorderStroke)
                                ) {
                                    Box(
                                        modifier = Modifier.padding(vertical = 10.dp, horizontal = 4.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = label,
                                            color = if (isSelected) Color.Black else TextMedium,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 11.sp
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.cardColors(containerColor = DarkSurfaceCard),
                            border = androidx.compose.foundation.BorderStroke(1.dp, CardBorderStroke)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                val brandTitle = when (selectedGiftBrand) {
                                    "GOOGLE_PLAY" -> "Google Play Redeem Code"
                                    "AMAZON" -> "Amazon Pay Gift Card"
                                    "FLIPKART" -> "Flipkart Gift Voucher"
                                    else -> "PhonePe Gift Card"
                                }

                                Text(brandTitle, color = BrightGold, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                Text("Official digital code delivered securely to your 'My Rewards' tab.", color = TextMuted, fontSize = 11.sp)

                                Spacer(modifier = Modifier.height(14.dp))
                                Text("Select Voucher Denomination:", color = TextHigh, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                Spacer(modifier = Modifier.height(8.dp))

                                val denominations = if (selectedGiftBrand == "GOOGLE_PLAY") {
                                    listOf(10, 50, 100, 250, 500, 1000)
                                } else {
                                    listOf(50, 100, 250, 500)
                                }

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    denominations.take(4).forEach { amt ->
                                        val isSelected = selectedGiftAmount == amt
                                        Surface(
                                            modifier = Modifier
                                                .weight(1f)
                                                .clickable { selectedGiftAmount = amt },
                                            shape = RoundedCornerShape(10.dp),
                                            color = if (isSelected) BrightGold else Color(0xFF1B234F),
                                            border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) BrightGold else CardBorderStroke)
                                        ) {
                                            Box(modifier = Modifier.padding(vertical = 10.dp), contentAlignment = Alignment.Center) {
                                                Text("₹$amt", color = if (isSelected) Color.Black else Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                            }
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(16.dp))
                                val gcCoinsNeeded = selectedGiftAmount * 100L
                                Button(
                                    onClick = {
                                        if (availableCoins < gcCoinsNeeded) {
                                            onShowMessage("Insufficient coins ($gcCoinsNeeded coins needed)")
                                            return@Button
                                        }
                                        onSubmitRedemption(selectedGiftBrand, gcCoinsNeeded, selectedGiftAmount.toDouble(), "Deliver to My Rewards")
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = BrightGold),
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier.fillMaxWidth().height(48.dp).testTag("submit_gc_redemption_btn")
                                ) {
                                    Text("Redeem ₹$selectedGiftAmount Code (${formatCoins(gcCoinsNeeded)} Coins)", color = Color.Black, fontWeight = FontWeight.Bold)
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
