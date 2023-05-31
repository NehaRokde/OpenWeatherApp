import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.app.openweatherapp.ui.screens.home.HomeScreen
import com.app.openweatherapp.ui.screens.home.HomeViewModel
import com.app.openweatherapp.ui.screens.navigation.NavigationView
import com.app.openweatherapp.ui.screens.search.SearchScreen
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

@RunWith(AndroidJUnit4::class)
class NavigationViewTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Mock
    lateinit var navController: NavHostController

    @Mock
    lateinit var homeViewModel: HomeViewModel

    // Define the custom composition local key for NavHostController
    private val LocalNavController = compositionLocalOf<NavHostController> { error("No NavHostController provided") }

    @Test
    fun testNavigationView() {
        MockitoAnnotations.initMocks(this)

        // Mock the behavior of navController
        `when`(navController.currentBackStackEntry).thenReturn(null)

        // Launch the NavigationView composable with the mocked HomeViewModel
        composeTestRule.setContent {
            CompositionLocalProvider(LocalNavController provides navController) {
                val viewModel: HomeViewModel = viewModel()
                NavigationView(viewModel)
            }
        }

        composeTestRule.onNodeWithTag("home").assertExists()

    }
}
