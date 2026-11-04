package com.example

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.data.local.AppDatabase
import com.example.data.repository.RewardRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

    private lateinit var db: AppDatabase
    private lateinit var repository: RewardRepository

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        repository = RewardRepository(db)
    }

    @After
    fun teardown() {
        db.close()
    }

    @Test
    fun testAppName() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("Reward Earn 3", appName)
    }

    @Test
    fun testInitializationAndOfferCreditCUJ() = runBlocking {
        repository.initializeDefaultData()

        val user = repository.currentUserFlow.firstOrNull()
        assertNotNull(user)
        assertEquals("RE3-782910", user?.id)

        val initialWallet = repository.currentWalletFlow.firstOrNull()
        assertNotNull(initialWallet)
        val initialAvailable = initialWallet!!.availableCoins

        // Start seeded offer
        val startResult = repository.startOffer("OFF-CANDY", user!!.id)
        assertTrue(startResult.isSuccess)
        val event = startResult.getOrThrow()

        // Verify callback
        val callbackResult = repository.processOfferCallback(event.id, verified = true)
        assertTrue(callbackResult.isSuccess)

        val updatedWallet = repository.currentWalletFlow.firstOrNull()
        assertEquals(initialAvailable + event.payoutCoins, updatedWallet?.availableCoins)
    }

    @Test
    fun testWithdrawalReservationAndRefundCUJ() = runBlocking {
        repository.initializeDefaultData()

        val user = repository.currentUserFlow.first()!!

        // Fund wallet with enough coins for withdrawal test
        repository.adminAdjustCoins(user.id, 10000L, isCredit = true, reason = "Test balance funding")

        val wallet = repository.currentWalletFlow.first()!!
        val beforeAvailable = wallet.availableCoins

        // Request ₹50 (5000 coins)
        val reqResult = repository.submitWithdrawalRequest(
            userId = user.id,
            paymentMethod = "UPI",
            coinAmount = 5000L,
            inrAmount = 50.0,
            detailsJson = "rahul@okhdfcbank"
        )
        assertTrue(reqResult.isSuccess)
        val request = reqResult.getOrThrow()

        val reservedWallet = repository.currentWalletFlow.first()!!
        assertEquals(beforeAvailable - 5000L, reservedWallet.availableCoins)
        assertEquals(wallet.pendingCoins + 5000L, reservedWallet.pendingCoins)

        // Admin rejects -> coins should be refunded
        val rejectResult = repository.adminRejectWithdrawal(request.id, "Test rejection refund")
        assertTrue(rejectResult.isSuccess)

        val refundedWallet = repository.currentWalletFlow.first()!!
        assertEquals(beforeAvailable, refundedWallet.availableCoins)
    }
}
