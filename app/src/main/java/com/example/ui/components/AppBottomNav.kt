package com.example.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.RadiantGold
import com.example.ui.theme.RoyalPurple
import com.example.ui.theme.TextMedium
import com.example.ui.theme.TextMuted
import com.example.ui.viewmodel.Screen

data class NavItem(
    val title: String,
    val screen: Screen,
    val icon: ImageVector,
    val tag: String
)

@Composable
fun AppBottomNav(
    currentScreen: Screen,
    onNavigate: (Screen) -> Unit,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        NavItem("Home", Screen.Home, Icons.Default.Home, "nav_home"),
        NavItem("Super Offers", Screen.Offers, Icons.Default.LocalOffer, "nav_offers"),
        NavItem("Wallet", Screen.WalletTab, Icons.Default.AccountBalanceWallet, "nav_wallet"),
        NavItem("Refer", Screen.Refer, Icons.Default.People, "nav_refer"),
        NavItem("Profile", Screen.Profile, Icons.Default.Person, "nav_profile")
    )

    NavigationBar(
        containerColor = DarkSurface,
        modifier = modifier
    ) {
        items.forEach { item ->
            val selected = currentScreen == item.screen
            NavigationBarItem(
                selected = selected,
                onClick = { onNavigate(item.screen) },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.title
                    )
                },
                label = { Text(item.title) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = RadiantGold,
                    selectedTextColor = RadiantGold,
                    indicatorColor = RoyalPurple.copy(alpha = 0.25f),
                    unselectedIconColor = TextMuted,
                    unselectedTextColor = TextMuted
                ),
                modifier = Modifier.testTag(item.tag)
            )
        }
    }
}
