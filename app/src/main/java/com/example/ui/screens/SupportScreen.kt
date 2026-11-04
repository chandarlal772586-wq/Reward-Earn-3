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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.SupportAgent
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.SupportTicket
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
fun SupportScreen(
    tickets: List<SupportTicket>,
    onBack: () -> Unit,
    onSubmitTicket: (category: String, subject: String, message: String) -> Unit,
    modifier: Modifier = Modifier
) {
    var category by remember { mutableStateOf("WITHDRAWAL") }
    var subject by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }

    val faqs = listOf(
        Pair("When will my UPI/Bank withdrawal be credited?", "UPI and IMPS transfers are typically processed by our administrative desk within 1 to 24 hours. Once marked complete, funds appear immediately in your bank."),
        Pair("How do Super Offers track and verify?", "When you click 'Start Offer', our partner networks generate an official tracking ID. Once you fulfill all task conditions, the provider sends a verified postback callback, and your coins are credited instantly."),
        Pair("What is the official coin conversion rate?", "The standard conversion is 1,000 Coins = ₹10 INR (100 coins per ₹1). Minimum withdrawal starts at 5,000 coins (₹50)."),
        Pair("How does the Refer & Earn reward unlock?", "When your invited friend registers with your code and completes their first Super Offer, ₹25 (2,500 Coins) is automatically unlocked for you.")
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
                IconButton(onClick = onBack, modifier = Modifier.testTag("support_back_btn")) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = TextHigh
                    )
                }
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Help & Support Desk",
                    color = TextHigh,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // FAQs Section
        item {
            Text(
                text = "Frequently Asked Questions",
                color = TextHigh,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
            )
        }

        items(faqs) { (q, a) ->
            var expanded by remember { mutableStateOf(false) }
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp)
                    .clickable { expanded = !expanded },
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
                        Text(q, color = TextHigh, fontWeight = FontWeight.Bold, fontSize = 13.sp, modifier = Modifier.weight(1f))
                        Icon(
                            imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                            contentDescription = null,
                            tint = RadiantGold
                        )
                    }
                    if (expanded) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(a, color = TextMedium, fontSize = 12.sp)
                    }
                }
            }
        }

        // Submit Ticket Card
        item {
            Spacer(modifier = Modifier.height(10.dp))
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
                    .testTag("submit_ticket_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = DarkSurfaceCard),
                border = androidx.compose.foundation.BorderStroke(1.dp, CardBorderStroke)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.SupportAgent, contentDescription = null, tint = RadiantGold)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Create Support Ticket", color = TextHigh, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = subject,
                        onValueChange = { subject = it },
                        label = { Text("Issue Subject") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = RadiantGold
                        ),
                        modifier = Modifier.fillMaxWidth().testTag("ticket_subject_input")
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = message,
                        onValueChange = { message = it },
                        label = { Text("Describe the issue in detail") },
                        minLines = 3,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = RadiantGold
                        ),
                        modifier = Modifier.fillMaxWidth().testTag("ticket_message_input")
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = {
                            if (subject.isNotBlank() && message.isNotBlank()) {
                                onSubmitTicket(category, subject, message)
                                subject = ""
                                message = ""
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = RoyalPurple),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth().testTag("submit_ticket_btn")
                    ) {
                        Text("Submit Ticket", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Previous Tickets
        item {
            Text(
                text = "Your Support Tickets (${tickets.size})",
                color = TextHigh,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }

        if (tickets.isEmpty()) {
            item {
                Box(modifier = Modifier.fillMaxWidth().padding(24.dp), contentAlignment = Alignment.Center) {
                    Text("No past support tickets", color = TextMuted)
                }
            }
        } else {
            items(tickets) { ticket ->
                val dateStr = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()).format(Date(ticket.createdTimestamp))
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
                            Text(ticket.subject, color = TextHigh, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            StatusChip(status = ticket.status)
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(dateStr, color = TextMuted, fontSize = 10.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(ticket.message, color = TextMedium, fontSize = 12.sp)

                        if (!ticket.adminReply.isNullOrBlank()) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Surface(
                                color = DarkSurfaceVariant,
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Text("Official Response from Support Team:", color = SuccessEmerald, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(ticket.adminReply, color = TextHigh, fontSize = 12.sp)
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
