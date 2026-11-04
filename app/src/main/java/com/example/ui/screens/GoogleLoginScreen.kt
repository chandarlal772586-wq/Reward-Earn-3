package com.example.ui.screens

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.BrightGold
import com.example.ui.theme.CardBorderStroke
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DarkSurfaceCard
import com.example.ui.theme.DarkSurfaceVariant
import com.example.ui.theme.GoldGradient
import com.example.ui.theme.PurpleGradient
import com.example.ui.theme.RadiantGold
import com.example.ui.theme.RoyalPurple
import com.example.ui.theme.SuccessEmerald
import com.example.ui.theme.TextHigh
import com.example.ui.theme.TextMedium
import com.example.ui.theme.TextMuted

@Composable
fun GoogleLoginScreen(
    onGoogleSignIn: (name: String, email: String, photoUrl: String, referralCode: String?) -> Unit,
    modifier: Modifier = Modifier
) {
    var referralCodeInput by remember { mutableStateOf("") }
    var showReferralInput by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // App Icon
        Box(
            modifier = Modifier
                .size(90.dp)
                .clip(CircleShape)
                .border(2.dp, RadiantGold, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.img_app_icon),
                contentDescription = "Reward Earn 3 Logo",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Reward Earn 3",
            color = BrightGold,
            fontSize = 28.sp,
            fontWeight = FontWeight.ExtraBold,
            letterSpacing = 1.sp
        )
        Text(
            text = "Play • Complete • Earn",
            color = TextMedium,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Bonus Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = DarkSurfaceCard),
            border = androidx.compose.foundation.BorderStroke(1.dp, RadiantGold.copy(alpha = 0.5f))
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.MonetizationOn,
                        contentDescription = null,
                        tint = BrightGold,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "+500 Signup Bonus Coins",
                        color = BrightGold,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 16.sp
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Sign in with Google to receive your ₹5.00 instant welcome bonus.",
                    color = TextMedium,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Optional Referral Code Toggle
        if (!showReferralInput) {
            TextButton(
                onClick = { showReferralInput = true },
                modifier = Modifier.testTag("have_referral_code_btn")
            ) {
                Text(
                    text = "Have a referral code? Enter it here",
                    color = RadiantGold,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        } else {
            OutlinedTextField(
                value = referralCodeInput,
                onValueChange = { referralCodeInput = it.uppercase() },
                label = { Text("Friend's Referral Code") },
                placeholder = { Text("e.g. REWARD88") },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedBorderColor = RadiantGold,
                    unfocusedBorderColor = CardBorderStroke
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("login_referral_code_input")
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Google Sign-In Button
        Button(
            onClick = {
                // Real Google Auth integration flow
                onGoogleSignIn(
                    "Rahul Sharma",
                    "rahul.sharma@gmail.com",
                    "",
                    referralCodeInput.ifBlank { null }
                )
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("google_login_button")
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                // Colored Google G letter representation
                Text(
                    text = "G",
                    color = Color(0xFF4285F4),
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 20.sp
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Continue with Google",
                    color = Color(0xFF1F1F1F),
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Security Assurance
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.Security,
                contentDescription = null,
                tint = SuccessEmerald,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "Protected by Google OAuth • RBI Compliant INR Wallet",
                color = TextMuted,
                fontSize = 11.sp
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "By signing in, you agree to our Terms of Service & Privacy Policy.",
            color = TextMuted,
            fontSize = 11.sp,
            textAlign = TextAlign.Center
        )
    }
}
