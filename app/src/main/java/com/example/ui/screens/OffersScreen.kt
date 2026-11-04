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
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.Games
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.example.data.model.Offer
import com.example.data.model.OfferProvider
import com.example.ui.components.CoinBadge
import com.example.ui.components.formatCoins
import com.example.ui.theme.BrightGold
import com.example.ui.theme.CardBorderStroke
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DarkSurfaceCard
import com.example.ui.theme.DarkSurfaceVariant
import com.example.ui.theme.RadiantGold
import com.example.ui.theme.TextHigh
import com.example.ui.theme.TextMedium
import com.example.ui.theme.TextMuted

@Composable
fun OffersScreen(
    offers: List<Offer>,
    providers: List<OfferProvider>,
    availableCoins: Long,
    onBack: (() -> Unit)? = null,
    onStartOffer: (Offer) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedCategory by remember { mutableStateOf("All") }
    val categories = listOf("All", "Apps", "Games", "Surveys", "Register")

    val filteredOffers = offers.filter { offer ->
        when (selectedCategory) {
            "All" -> true
            "Apps" -> offer.category.equals("APPS", ignoreCase = true) || offer.category.equals("APP", ignoreCase = true)
            "Games" -> offer.category.equals("GAMES", ignoreCase = true) || offer.category.equals("GAME", ignoreCase = true)
            "Surveys" -> offer.category.equals("SURVEYS", ignoreCase = true) || offer.category.equals("SURVEY", ignoreCase = true)
            "Register" -> offer.category.equals("REGISTER", ignoreCase = true) || offer.category.equals("REGISTRATION", ignoreCase = true)
            else -> true
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBackground)
    ) {
        // Header matching Screenshot 2
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (onBack != null) {
                        IconButton(onClick = onBack, modifier = Modifier.testTag("offers_back_btn")) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.White
                            )
                        }
                    } else {
                        Spacer(modifier = Modifier.width(6.dp))
                    }
                    Text(
                        text = "Super Offers",
                        color = TextHigh,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                CoinBadge(coins = availableCoins)
            }
        }

        // Category Filter Pills matching Screenshot 2
        item {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(categories) { cat ->
                    val isSelected = selectedCategory == cat
                    Surface(
                        modifier = Modifier
                            .clickable { selectedCategory = cat }
                            .testTag("filter_pill_$cat"),
                        shape = RoundedCornerShape(20.dp),
                        color = if (isSelected) BrightGold else DarkSurfaceVariant,
                        border = androidx.compose.foundation.BorderStroke(0.5.dp, if (isSelected) BrightGold else CardBorderStroke)
                    ) {
                        Text(
                            text = cat,
                            color = if (isSelected) Color.Black else TextMedium,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(6.dp))
        }

        // Offer items matching Screenshot 2
        items(filteredOffers) { offer ->
            ScreenshotOfferCard(
                offer = offer,
                onStart = { onStartOffer(offer) },
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
            )
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun ScreenshotOfferCard(
    offer: Offer,
    onStart: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("super_offer_card_${offer.id}"),
        shape = RoundedCornerShape(16.dp),
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
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // App Icon Box
                val (iconColor, icon) = when (offer.iconType) {
                    "CANDY" -> Pair(Color(0xFFFF4757), Icons.Default.Casino)
                    "SHAREIT" -> Pair(Color(0xFF2E86DE), Icons.Default.Share)
                    "RUMMY" -> Pair(Color(0xFFFF9F43), Icons.Default.Casino)
                    "SURVEY" -> Pair(Color(0xFF54A0FF), Icons.Default.Assignment)
                    "CRICKET" -> Pair(Color(0xFF10AC84), Icons.Default.SportsEsports)
                    else -> Pair(Color(0xFF6C5CE7), Icons.Default.PhoneAndroid)
                }

                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(iconColor.copy(alpha = 0.25f))
                        .border(1.dp, iconColor.copy(alpha = 0.5f), RoundedCornerShape(14.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = offer.appName.ifBlank { offer.title },
                        tint = iconColor,
                        modifier = Modifier.size(28.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = offer.appName.ifBlank { offer.title },
                        color = TextHigh,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = offer.description,
                        color = TextMedium,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(4.dp))

                    // Pill: ⏱ 5-10 min | Easy Task
                    Surface(
                        color = DarkSurfaceVariant,
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Schedule,
                                contentDescription = null,
                                tint = TextMuted,
                                modifier = Modifier.size(11.dp)
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = "${offer.estimatedMinutes} min | ${offer.difficulty}",
                                color = TextMuted,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }

            // Reward and Yellow Button matching screenshot
            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = "Earn up to",
                    color = TextMuted,
                    fontSize = 10.sp
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.MonetizationOn,
                        contentDescription = null,
                        tint = BrightGold,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = formatCoins(offer.rewardCoins),
                        color = BrightGold,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 15.sp
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = onStart,
                    colors = ButtonDefaults.buttonColors(containerColor = BrightGold),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .height(34.dp)
                        .testTag("start_offer_btn_${offer.id}")
                ) {
                    Text(
                        text = "Start Offer →",
                        color = Color.Black,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 11.sp
                    )
                }
            }
        }
    }
}
