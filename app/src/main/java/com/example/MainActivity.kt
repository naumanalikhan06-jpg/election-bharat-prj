package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Accessible
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.FactCheck
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.HowToVote
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.ReportProblem
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.ui.components.SmsNotificationBanner
import com.example.ui.components.SovereignHeader
import com.example.ui.screens.AccessibilityCenterScreen
import com.example.ui.screens.CandidateTransparencyScreen
import com.example.ui.screens.ElectionMapScreen
import com.example.ui.screens.FactCheckScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.MccReportingScreen
import com.example.ui.screens.NirvachanAiScreen
import com.example.ui.screens.OfficialCommandCenterScreen
import com.example.ui.screens.ResultsAnalyticsScreen
import com.example.ui.screens.SecureVotingBoothScreen
import com.example.ui.screens.VoterDashboardScreen
import com.example.ui.theme.AshokaBlue
import com.example.ui.theme.BharatGreen
import com.example.ui.theme.BharatSaffron
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.SovereignNavy
import com.example.viewmodel.ElectionViewModel

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Home : Screen("home", "Home", Icons.Default.Home)
    object VoterDashboard : Screen("voter_dashboard", "Smart Voter", Icons.Default.HowToVote)
    object VotingBooth : Screen("voting_booth", "3D EVM", Icons.Default.VpnKey)
    object ElectionMap : Screen("election_map", "GIS Map", Icons.Default.Map)
    object Candidates : Screen("candidates", "Candidates", Icons.Default.People)
    object NirvachanAi : Screen("nirvachan_ai", "Nirvachan AI", Icons.Default.Psychology)
    object MccReporting : Screen("mcc_reporting", "MCC Report", Icons.Default.ReportProblem)
    object CommandCenter : Screen("command_center", "Command", Icons.Default.Security)
    object Results : Screen("results", "Results", Icons.Default.Analytics)
    object FactCheck : Screen("fact_check", "Fact-Check", Icons.Default.FactCheck)
    object Accessibility : Screen("accessibility", "Accessible", Icons.Default.Accessible)
}

class MainActivity : ComponentActivity() {
    private val viewModel: ElectionViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val uiState by viewModel.uiState.collectAsState()

            MyApplicationTheme(
                isHighContrast = uiState.isHighContrast
            ) {
                val navController = rememberNavController()

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        Column {
                            SovereignHeader(
                                currentLanguage = uiState.currentLanguage,
                                onLanguageSelected = { viewModel.setLanguage(it) },
                                currentRole = uiState.currentRole,
                                onRoleSelected = { viewModel.setRole(it) },
                                isHighContrast = uiState.isHighContrast,
                                onToggleHighContrast = { viewModel.toggleHighContrast() }
                            )

                            // Priority SMS Push Notification Banner
                            SmsNotificationBanner(
                                smsAlert = uiState.activeSmsAlert,
                                onDismiss = { viewModel.dismissSmsBanner() },
                                onViewReceipt = {
                                    viewModel.dismissSmsBanner()
                                    navController.navigate(Screen.VoterDashboard.route)
                                }
                            )
                        }
                    },
                    bottomBar = {
                        BottomNavigationBar(navController = navController)
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = Screen.Home.route,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        composable(Screen.Home.route) {
                            HomeScreen(
                                uiState = uiState,
                                onNavigateTo = { route -> navController.navigate(route) },
                                onSearchChange = { viewModel.setSearchQuery(it) }
                            )
                        }
                        composable(Screen.VoterDashboard.route) {
                            VoterDashboardScreen(
                                uiState = uiState,
                                onNavigateToVoting = { navController.navigate(Screen.VotingBooth.route) }
                            )
                        }
                        composable(Screen.VotingBooth.route) {
                            SecureVotingBoothScreen(
                                uiState = uiState,
                                viewModel = viewModel
                            )
                        }
                        composable(Screen.ElectionMap.route) {
                            ElectionMapScreen(
                                uiState = uiState,
                                viewModel = viewModel
                            )
                        }
                        composable(Screen.Candidates.route) {
                            CandidateTransparencyScreen(
                                uiState = uiState,
                                viewModel = viewModel
                            )
                        }
                        composable(Screen.NirvachanAi.route) {
                            NirvachanAiScreen(
                                uiState = uiState,
                                viewModel = viewModel
                            )
                        }
                        composable(Screen.MccReporting.route) {
                            MccReportingScreen(
                                uiState = uiState,
                                viewModel = viewModel
                            )
                        }
                        composable(Screen.CommandCenter.route) {
                            OfficialCommandCenterScreen(
                                uiState = uiState,
                                viewModel = viewModel
                            )
                        }
                        composable(Screen.Results.route) {
                            ResultsAnalyticsScreen(
                                uiState = uiState
                            )
                        }
                        composable(Screen.FactCheck.route) {
                            FactCheckScreen(
                                uiState = uiState
                            )
                        }
                        composable(Screen.Accessibility.route) {
                            AccessibilityCenterScreen(
                                uiState = uiState,
                                viewModel = viewModel
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val primaryTabs = listOf(
        Screen.Home,
        Screen.VoterDashboard,
        Screen.VotingBooth,
        Screen.ElectionMap,
        Screen.Candidates,
        Screen.NirvachanAi
    )

    NavigationBar(
        containerColor = SovereignNavy,
        tonalElevation = 8.dp
    ) {
        primaryTabs.forEach { screen ->
            val isSelected = currentRoute == screen.route
            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    if (currentRoute != screen.route) {
                        navController.navigate(screen.route) {
                            popUpTo(Screen.Home.route) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                icon = {
                    Icon(
                        imageVector = screen.icon,
                        contentDescription = screen.title,
                        modifier = Modifier.size(20.dp)
                    )
                },
                label = {
                    Text(
                        text = screen.title,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 9.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = BharatSaffron,
                    selectedTextColor = BharatSaffron,
                    unselectedIconColor = Color.White.copy(alpha = 0.6f),
                    unselectedTextColor = Color.White.copy(alpha = 0.6f),
                    indicatorColor = SovereignNavy
                )
            )
        }
    }
}
