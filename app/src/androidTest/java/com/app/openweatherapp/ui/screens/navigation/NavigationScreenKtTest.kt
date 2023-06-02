package com.app.openweatherapp.ui.screens.navigation

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.navigation.testing.TestNavHostController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.app.openweatherapp.ui.screens.home.HomeViewModel
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

// TODO - this class requires more mock dependency for HomeViewModel,
//  due to time constraints I haven't finished this

@RunWith(AndroidJUnit4::class)
class NavigationScreenKtTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setUpNavHost() {

        // Mock dependencies
        val homeViewModel = mockk<HomeViewModel>()

        composeTestRule.setContent {
            NavigationView(homeViewModel)

        }
    }

    @Test
    fun test_StartDestinationIsHomeScreen() {
        composeTestRule.onNodeWithTag("Search cities here").assertIsDisplayed()
    }
}