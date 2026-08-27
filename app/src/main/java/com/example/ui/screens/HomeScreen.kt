package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.FactCheck
import androidx.compose.material.icons.filled.HowToVote
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.ReportProblem
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.material.icons.filled.WheelchairPickup
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.OfficialAnnouncement
import com.example.ui.components.AshokaChakra3DView
import com.example.ui.components.DataSourceBadge
import com.example.ui.components.DataSourceType
import com.example.ui.components.ThreeDCard
import com.example.ui.theme.AshokaBlue
import com.example.ui.theme.AshokaBlueLight
import com.example.ui.theme.BharatGreen
import com.example.ui.theme.BharatSaffron
import com.example.ui.theme.BharatSaffronLight
import com.example.ui.theme.HologramBlue
import com.example.ui.theme.SovereignGold
import com.example.ui.theme.SovereignNavy
import com.example.ui.theme.SovereignNavyLight
import com.example.viewmodel.ElectionUiState

@Composable
fun HomeScreen(
    uiState: ElectionUiState,
    onNavigateTo: (String) -> Unit,
    onSearchChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 14.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(6.dp))
            // 3D Hero Banner: Democracy Connected
            ThreeDCard(
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = SovereignNavy,
                cornerRadius = 24.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(BharatSaffron)
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = "INDIA • 2026",
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                        color = Color.White
                                    )
                                }
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "ELECTION OPERATIONS",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color.White.copy(alpha = 0.7f)
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Democracy,\nConnected.",
                                style = MaterialTheme.typography.headlineLarge.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    lineHeight = 32.sp
                                ),
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "One Intelligent Platform for Transparent, Accessible and Connected Elections.",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White.copy(alpha = 0.85f)
                            )
                        }

                        // Rotating 3D Ashoka Hologram
                        AshokaChakra3DView(
                            size = 90.dp,
                            showOuterRays = true
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Quick Search Bar
                    OutlinedTextField(
                        value = uiState.searchQuery,
                        onValueChange = onSearchChange,
                        placeholder = {
                            Text(
                                "Search Constituency, Candidate, EPIC or Booth...",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White.copy(alpha = 0.6f)
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search",
                                tint = AshokaBlueLight
                            )
                        },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color(0xFF061527),
                            unfocusedContainerColor = Color(0xFF061527),
                            focusedBorderColor = BharatSaffron,
                            unfocusedBorderColor = AshokaBlueLight.copy(alpha = 0.4f),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

        // Live Sovereign Metric Counters
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                MetricPill(title = "Total Electors", value = "96.8 Cr", sub = "Verified in Roll", color = AshokaBlue)
                MetricPill(title = "Lok Sabha Seats", value = "543", sub = "Across 28 States & 8 UTs", color = BharatSaffron)
                MetricPill(title = "Polling Stations", value = "10.48 Lakh", sub = "100% AMF Standard", color = BharatGreen)
                MetricPill(title = "Average Turnout", value = "68.4%", sub = "Real-Time Aggregate", color = SovereignGold)
            }
        }

        // Feature Service Portals Grid
        item {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Citizen & Operations Portals",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    DataSourceBadge(type = DataSourceType.OFFICIAL_VERIFIED)
                }
                Spacer(modifier = Modifier.height(10.dp))

                // Grid of Main Actions
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    PortalCard(
                        title = "Smart Voter Card",
                        subtitle = "Digital EPIC & Polling Booth",
                        icon = Icons.Default.HowToVote,
                        accentColor = AshokaBlue,
                        modifier = Modifier.weight(1f),
                        onClick = { onNavigateTo("voter_dashboard") }
                    )
                    PortalCard(
                        title = "3D Secure Voting",
                        subtitle = "EVM & VVPAT + SMS Alert",
                        icon = Icons.Default.VpnKey,
                        accentColor = BharatSaffron,
                        modifier = Modifier.weight(1f),
                        onClick = { onNavigateTo("voting_booth") }
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    PortalCard(
                        title = "GIS Election Map",
                        subtitle = "Constituencies & Turnout",
                        icon = Icons.Default.Map,
                        accentColor = BharatGreen,
                        modifier = Modifier.weight(1f),
                        onClick = { onNavigateTo("election_map") }
                    )
                    PortalCard(
                        title = "Candidates & Affidavits",
                        subtitle = "Side-by-Side Comparison",
                        icon = Icons.Default.People,
                        accentColor = SovereignGold,
                        modifier = Modifier.weight(1f),
                        onClick = { onNavigateTo("candidates") }
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    PortalCard(
                        title = "Nirvachan AI",
                        subtitle = "Multilingual Assistant",
                        icon = Icons.Default.Psychology,
                        accentColor = HologramBlue,
                        modifier = Modifier.weight(1f),
                        onClick = { onNavigateTo("nirvachan_ai") }
                    )
                    PortalCard(
                        title = "Report MCC Issue",
                        subtitle = "Geotagged Violation Log",
                        icon = Icons.Default.ReportProblem,
                        accentColor = Color(0xFFEF4444),
                        modifier = Modifier.weight(1f),
                        onClick = { onNavigateTo("mcc_reporting") }
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    PortalCard(
                        title = "Command Center",
                        subtitle = "DEO / BLO / Observer Hub",
                        icon = Icons.Default.Security,
                        accentColor = SovereignNavy,
                        modifier = Modifier.weight(1f),
                        onClick = { onNavigateTo("command_center") }
                    )
                    PortalCard(
                        title = "Results & Analytics",
                        subtitle = "Seat Tallies & Turnout",
                        icon = Icons.Default.Analytics,
                        accentColor = Color(0xFF8B5CF6),
                        modifier = Modifier.weight(1f),
                        onClick = { onNavigateTo("results") }
                    )
                }
            }
        }

        // Official Announcements Bulletin
        item {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Campaign,
                            contentDescription = null,
                            tint = BharatSaffron,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Official Announcements",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                    Text(
                        text = "LIVE ECI FEED",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = BharatGreen
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        items(uiState.announcements) { announcement ->
            AnnouncementCard(announcement = announcement)
        }

        item {
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun MetricPill(
    title: String,
    value: String,
    sub: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .width(160.dp)
            .shadow(4.dp, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(color)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold),
                color = color
            )
            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = sub,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun PortalCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    accentColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .shadow(6.dp, RoundedCornerShape(18.dp))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(accentColor.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = accentColor,
                    modifier = Modifier.size(22.dp)
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1
            )
        }
    }
}

@Composable
fun AnnouncementCard(
    announcement: OfficialAnnouncement,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .shadow(2.dp, RoundedCornerShape(14.dp)),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(if (announcement.isHighPriority) BharatSaffron.copy(alpha = 0.2f) else AshokaBlue.copy(alpha = 0.15f))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = announcement.category.uppercase(),
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = if (announcement.isHighPriority) BharatSaffron else AshokaBlue
                    )
                }
                Text(
                    text = announcement.date,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = announcement.title,
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = announcement.summary,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
