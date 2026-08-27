package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Accessible
import androidx.compose.material.icons.filled.Blind
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Contrast
import androidx.compose.material.icons.filled.FormatSize
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.PanTool
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import com.example.ui.components.DataSourceBadge
import com.example.ui.components.DataSourceType
import com.example.ui.theme.AshokaBlue
import com.example.ui.theme.BharatGreen
import com.example.ui.theme.BharatSaffron
import com.example.ui.theme.SovereignGold
import com.example.ui.theme.SovereignNavy
import com.example.viewmodel.ElectionUiState
import com.example.viewmodel.ElectionViewModel

@Composable
fun AccessibilityCenterScreen(
    uiState: ElectionUiState,
    viewModel: ElectionViewModel,
    modifier: Modifier = Modifier
) {
    var homeVotingRequested by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 14.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Inclusive & Accessible Elections",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = "Assistive Technologies for PwD & Senior Citizens",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                DataSourceBadge(type = DataSourceType.OFFICIAL_VERIFIED)
            }
        }

        // Accessibility Display Settings
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(3.dp, RoundedCornerShape(16.dp)),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Visual & Display Comfort",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    // High Contrast Switch
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Contrast, contentDescription = null, tint = AshokaBlue)
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text("High-Contrast Mode", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold))
                                Text("Enhanced legibility & crisp monochrome contrast", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                            }
                        }
                        Switch(
                            checked = uiState.isHighContrast,
                            onCheckedChange = { viewModel.toggleHighContrast() },
                            colors = SwitchDefaults.colors(checkedThumbColor = BharatGreen)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Large Text Switch
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.FormatSize, contentDescription = null, tint = BharatSaffron)
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text("Enlarged Typography", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold))
                                Text("Scale text up for easier readability", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                            }
                        }
                        Switch(
                            checked = uiState.isLargeText,
                            onCheckedChange = { viewModel.toggleLargeText() },
                            colors = SwitchDefaults.colors(checkedThumbColor = BharatSaffron)
                        )
                    }
                }
            }
        }

        // PwD & Senior Citizen Doorstep Home Voting Facility Registration (Form 12D)
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(3.dp, RoundedCornerShape(16.dp)),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Home, contentDescription = null, tint = BharatGreen)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Doorstep Home Voting (Form 12D)",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Under ECI Guidelines, senior citizens aged 85+ and voters with benchmark physical disability (40%+) are eligible for postal ballot voting at home under full videography and security escort.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    if (!homeVotingRequested) {
                        Button(
                            onClick = { homeVotingRequested = true },
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = BharatGreen),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Default.Send, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Request BLO Home Visit Registration", fontWeight = FontWeight.Bold)
                        }
                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(BharatGreen.copy(alpha = 0.15f))
                                .padding(10.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = BharatGreen)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Form 12D Request Logged! BLO Smt. Sunita Verma will contact you within 48 hours for verification.",
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = BharatGreen
                                )
                            }
                        }
                    }
                }
            }
        }

        // Assured Minimum Facilities (AMF) at every Booth
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(3.dp, RoundedCornerShape(16.dp)),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SovereignNavy)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "100% Assured Minimum Facilities (AMF)",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    AmfItem("♿ 1:12 Gradient Wheelchair Ramps & Handrails")
                    AmfItem("⠃⠗ Embossed Braille Numerals on all EVM Balloting Units")
                    AmfItem("🦻 Sign-Language Volunteers & Video Instruction Guides")
                    AmfItem("🪑 Priority Seating & Ground Floor Rooms for Senior Electors")
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
private fun AmfItem(text: String) {
    Row(
        modifier = Modifier.padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.size(6.dp).clip(RoundedCornerShape(3.dp)).background(BharatSaffron))
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = text, style = MaterialTheme.typography.bodySmall, color = Color.White.copy(alpha = 0.9f))
    }
}
