package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.CompareArrows
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Candidate
import com.example.ui.components.DataSourceBadge
import com.example.ui.components.DataSourceType
import com.example.ui.components.ThreeDCard
import com.example.ui.theme.AshokaBlue
import com.example.ui.theme.AshokaBlueLight
import com.example.ui.theme.BharatGreen
import com.example.ui.theme.BharatSaffron
import com.example.ui.theme.SovereignGold
import com.example.ui.theme.SovereignNavy
import com.example.viewmodel.ElectionUiState
import com.example.viewmodel.ElectionViewModel

@Composable
fun CandidateTransparencyScreen(
    uiState: ElectionUiState,
    viewModel: ElectionViewModel,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableIntStateOf(0) } // 0: Candidate Profiles, 1: Side-by-Side Comparison

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 14.dp)
    ) {
        Spacer(modifier = Modifier.height(6.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Candidate Transparency Center",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "Verified Form 26 Affidavits & Neutral Comparison",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            DataSourceBadge(type = DataSourceType.OFFICIAL_VERIFIED)
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Tab Selector: Directory vs Comparison Matrix
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = AshokaBlue
        ) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = { Text("Candidate Profiles (${uiState.candidates.size})", fontWeight = FontWeight.Bold) }
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = { Text("Neutral Comparison Matrix", fontWeight = FontWeight.Bold) }
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        if (selectedTab == 0) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(uiState.candidates) { candidate ->
                    CandidateCard(
                        candidate = candidate,
                        onCompare = {
                            viewModel.setComparisonCandidates(candidate, uiState.candidates.find { it.id != candidate.id })
                            selectedTab = 1
                        }
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        } else {
            // SIDE-BY-SIDE COMPARISON MATRIX
            SideBySideComparisonView(
                candidates = uiState.candidates,
                candidate1 = uiState.comparisonCandidate1 ?: uiState.candidates.firstOrNull(),
                candidate2 = uiState.comparisonCandidate2 ?: uiState.candidates.getOrNull(1),
                onSelectCandidate1 = { c -> viewModel.setComparisonCandidates(c, uiState.comparisonCandidate2) },
                onSelectCandidate2 = { c -> viewModel.setComparisonCandidates(uiState.comparisonCandidate1, c) }
            )
        }
    }
}

@Composable
fun CandidateCard(
    candidate: Candidate,
    onCompare: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header Row: Symbol + Name + Party + Verified Tag
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFFF1F5F9)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = candidate.partySymbolEmoji, fontSize = 26.sp)
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = candidate.name,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Default.Verified,
                            contentDescription = "Verified Affidavit",
                            tint = BharatGreen,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    Text(
                        text = "${candidate.partyName} (${candidate.partyAbbr}) • ${candidate.constituencyName}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Candidate Specs Grid: Assets, Education, Cases
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                MetricBox(
                    label = "Total Assets",
                    value = "₹${candidate.totalAssetsInCrores} Cr",
                    icon = Icons.Default.AccountBalance,
                    color = AshokaBlue,
                    modifier = Modifier.weight(1f)
                )
                MetricBox(
                    label = "Criminal Cases",
                    value = if (candidate.criminalCasesCount == 0) "0 (Clean)" else "${candidate.criminalCasesCount} Pending",
                    icon = Icons.Default.Gavel,
                    color = if (candidate.criminalCasesCount == 0) BharatGreen else Color(0xFFEF4444),
                    modifier = Modifier.weight(1f)
                )
                MetricBox(
                    label = "Education",
                    value = candidate.education.split(" ").firstOrNull() ?: "Graduate",
                    icon = Icons.Default.School,
                    color = SovereignGold,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Criminal details disclaimer (strict objectivity)
            Text(
                text = "Legal Record: ${candidate.criminalCaseDetails}",
                style = MaterialTheme.typography.labelSmall,
                color = if (candidate.criminalCasesCount == 0) BharatGreen else Color(0xFFDC2626)
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Key Manifesto Highlights
            Text(
                text = "Key Manifesto Highlights:",
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )
            candidate.keyManifestoPledges.take(2).forEach { pledge ->
                Row(
                    modifier = Modifier.padding(vertical = 2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(modifier = Modifier.size(4.dp).clip(CircleShape).background(BharatSaffron))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = pledge, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                FilledTonalButton(
                    onClick = onCompare,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(imageVector = Icons.Default.CompareArrows, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Compare Candidate", style = MaterialTheme.typography.labelSmall)
                }
            }
        }
    }
}

@Composable
fun SideBySideComparisonView(
    candidates: List<Candidate>,
    candidate1: Candidate?,
    candidate2: Candidate?,
    onSelectCandidate1: (Candidate) -> Unit,
    onSelectCandidate2: (Candidate) -> Unit
) {
    if (candidate1 == null || candidate2 == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Select candidates to compare")
        }
        return
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            // Objective Neutrality Note
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(SovereignGold.copy(alpha = 0.12f))
                    .padding(10.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Shield, contentDescription = null, tint = SovereignGold, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Neutrality Notice: Bharat Election Nexus presents factual affidavit disclosures without subjective ranking or endorsement.",
                        style = MaterialTheme.typography.labelSmall,
                        color = SovereignGold
                    )
                }
            }
        }

        // Side-by-Side Candidates Header Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp)
                ) {
                    // Candidate 1 Column
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = candidate1.partySymbolEmoji, fontSize = 28.sp)
                        Text(
                            text = candidate1.name,
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = candidate1.partyAbbr,
                            style = MaterialTheme.typography.labelSmall,
                            color = AshokaBlue
                        )
                    }

                    Box(
                        modifier = Modifier
                            .width(1.dp)
                            .height(60.dp)
                            .background(Color.LightGray)
                    )

                    // Candidate 2 Column
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = candidate2.partySymbolEmoji, fontSize = 28.sp)
                        Text(
                            text = candidate2.name,
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = candidate2.partyAbbr,
                            style = MaterialTheme.typography.labelSmall,
                            color = BharatSaffron
                        )
                    }
                }
            }
        }

        // Comparison Rows
        item { ComparisonParamRow(label = "Declared Total Assets", val1 = "₹${candidate1.totalAssetsInCrores} Cr", val2 = "₹${candidate2.totalAssetsInCrores} Cr") }
        item { ComparisonParamRow(label = "Declared Liabilities", val1 = "₹${candidate1.totalLiabilitiesInCrores} Cr", val2 = "₹${candidate2.totalLiabilitiesInCrores} Cr") }
        item { ComparisonParamRow(label = "Criminal Cases Pending", val1 = "${candidate1.criminalCasesCount}", val2 = "${candidate2.criminalCasesCount}") }
        item { ComparisonParamRow(label = "Educational Qualification", val1 = candidate1.education, val2 = candidate2.education) }
        item { ComparisonParamRow(label = "Age & Profession", val1 = "${candidate1.age} yrs • ${candidate1.profession}", val2 = "${candidate2.age} yrs • ${candidate2.profession}") }
        item { ComparisonParamRow(label = "Previous Election Margin", val1 = candidate1.previousElectionPerformance, val2 = candidate2.previousElectionPerformance) }

        item {
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
private fun MetricBox(
    label: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(color.copy(alpha = 0.1f))
            .padding(8.dp)
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = label, style = MaterialTheme.typography.labelSmall, color = color)
            }
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = value, style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold), color = MaterialTheme.colorScheme.onSurface)
        }
    }
}

@Composable
private fun ComparisonParamRow(label: String, val1: String, val2: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = label.uppercase(),
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                color = AshokaBlue
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = val1,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.weight(1f),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Box(modifier = Modifier.width(1.dp).height(20.dp).background(Color(0xFFE2E8F0)))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = val2,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.weight(1f),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}
