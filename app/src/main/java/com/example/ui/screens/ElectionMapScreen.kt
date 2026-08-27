package com.example.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.ZoomIn
import androidx.compose.material.icons.filled.ZoomOut
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ParliamentaryConstituency
import com.example.ui.components.DataSourceBadge
import com.example.ui.components.DataSourceType
import com.example.ui.theme.AshokaBlue
import com.example.ui.theme.AshokaBlueLight
import com.example.ui.theme.BharatGreen
import com.example.ui.theme.BharatSaffron
import com.example.ui.theme.SovereignGold
import com.example.ui.theme.SovereignNavy
import com.example.viewmodel.ElectionUiState
import com.example.viewmodel.ElectionViewModel

@Composable
fun ElectionMapScreen(
    uiState: ElectionUiState,
    viewModel: ElectionViewModel,
    modifier: Modifier = Modifier
) {
    var selectedFilter by remember { mutableStateOf("All Phases") }
    var selectedConstituency by remember { mutableStateOf<ParliamentaryConstituency?>(null) }
    var zoomScale by remember { mutableFloatStateOf(1.0f) }

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
                    text = "GIS Election Discovery Map",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "Constituency Polling Phases & Turnout Geotagging",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            DataSourceBadge(type = DataSourceType.OFFICIAL_VERIFIED)
        }

        Spacer(modifier = Modifier.height(10.dp))

        // GIS Layer Filter Chips
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf("All Phases", "Turnout Heatmap", "Wheelchair AMF", "Phase 1 & 2", "Phase 7").forEach { chip ->
                FilterChip(
                    selected = selectedFilter == chip,
                    onClick = { selectedFilter = chip },
                    label = { Text(chip, style = MaterialTheme.typography.labelSmall) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = AshokaBlue,
                        selectedLabelColor = Color.White
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Interactive GIS Vector Map Canvas
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp)
                .shadow(6.dp, RoundedCornerShape(18.dp)),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = SovereignNavy)
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Canvas(
                    modifier = Modifier
                        .fillMaxSize()
                        .pointerInput(Unit) {
                            detectTapGestures { offset ->
                                // Hit detection against known constituency points
                                val width = size.width
                                val height = size.height
                                
                                val hit = uiState.constituencies.firstOrNull { pc ->
                                    val mapX = ((pc.longitude - 68.0) / (98.0 - 68.0) * width * 0.8f + width * 0.1f).toFloat()
                                    val mapY = ((38.0 - pc.latitude) / (38.0 - 8.0) * height * 0.8f + height * 0.1f).toFloat()
                                    val dist = (offset.x - mapX) * (offset.x - mapX) + (offset.y - mapY) * (offset.y - mapY)
                                    dist < 1200f
                                }
                                if (hit != null) {
                                    selectedConstituency = hit
                                }
                            }
                        }
                ) {
                    val w = size.width
                    val h = size.height

                    // Grid lines (Latitude / Longitude)
                    for (i in 1..5) {
                        drawLine(
                            color = Color.White.copy(alpha = 0.05f),
                            start = Offset(0f, h * i / 6f),
                            end = Offset(w, h * i / 6f),
                            strokeWidth = 1f
                        )
                        drawLine(
                            color = Color.White.copy(alpha = 0.05f),
                            start = Offset(w * i / 6f, 0f),
                            end = Offset(w * i / 6f, h),
                            strokeWidth = 1f
                        )
                    }

                    // Stylized Outline of India
                    val indiaOutline = Path().apply {
                        val startX = w * 0.35f
                        val startY = h * 0.12f
                        moveTo(startX, startY) // Kashmir
                        lineTo(w * 0.42f, h * 0.10f)
                        lineTo(w * 0.52f, h * 0.22f) // Himalayas
                        lineTo(w * 0.78f, h * 0.28f) // North-East
                        lineTo(w * 0.88f, h * 0.38f) // Assam / Arunachal
                        lineTo(w * 0.74f, h * 0.44f) // Bengal
                        lineTo(w * 0.68f, h * 0.58f) // Odisha coast
                        lineTo(w * 0.58f, h * 0.78f) // Andhra coast
                        lineTo(w * 0.48f, h * 0.92f) // Kanyakumari
                        lineTo(w * 0.38f, h * 0.78f) // Kerala coast
                        lineTo(w * 0.30f, h * 0.60f) // Maharashtra coast
                        lineTo(w * 0.22f, h * 0.44f) // Gujarat Rann
                        lineTo(w * 0.28f, h * 0.30f) // Rajasthan
                        lineTo(w * 0.32f, h * 0.18f) // Punjab
                        close()
                    }

                    // Draw Map Landmass
                    drawPath(
                        path = indiaOutline,
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFF102A43),
                                Color(0xFF0F172A)
                            )
                        )
                    )
                    drawPath(
                        path = indiaOutline,
                        color = AshokaBlueLight.copy(alpha = 0.6f),
                        style = Stroke(width = 2.dp.toPx())
                    )

                    // Draw Constituency Interactive Radar Pins
                    uiState.constituencies.forEach { pc ->
                        val mapX = ((pc.longitude - 68.0) / (98.0 - 68.0) * w * 0.8f + w * 0.1f).toFloat()
                        val mapY = ((38.0 - pc.latitude) / (38.0 - 8.0) * h * 0.8f + h * 0.1f).toFloat()
                        val isSelected = selectedConstituency?.id == pc.id

                        val pinColor = when {
                            selectedFilter == "Turnout Heatmap" -> if (pc.turnoutPercentage > 65.0) BharatGreen else BharatSaffron
                            else -> if (pc.phase % 2 == 0) AshokaBlueLight else BharatSaffron
                        }

                        // Outer Pulse Glow
                        drawCircle(
                            color = pinColor.copy(alpha = if (isSelected) 0.6f else 0.25f),
                            radius = if (isSelected) 14.dp.toPx() else 9.dp.toPx(),
                            center = Offset(mapX, mapY)
                        )

                        // Inner Pin Center
                        drawCircle(
                            color = if (isSelected) Color.White else pinColor,
                            radius = if (isSelected) 6.dp.toPx() else 4.dp.toPx(),
                            center = Offset(mapX, mapY)
                        )
                    }
                }

                // Map Legend & Controls Overlay
                Column(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF071424).copy(alpha = 0.85f))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "🗺️ 543 Parliamentary Bounds",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Selected Constituency Inspection Card
        if (selectedConstituency != null) {
            val pc = selectedConstituency!!
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(4.dp, RoundedCornerShape(16.dp)),
                shape = RoundedCornerShape(16.dp),
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
                        Column {
                            Text(
                                text = "${pc.name} (${pc.id})",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "State: ${pc.state} • Phase: ${pc.phase}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        IconButton(onClick = { selectedConstituency = null }) {
                            Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        InfoStat(label = "Total Electors", value = "${pc.totalElectorsInLakhs} Lakh", color = AshokaBlue)
                        InfoStat(label = "Polling Stations", value = "${pc.totalPollingStations}", color = BharatGreen)
                        InfoStat(label = "Turnout Rate", value = "${pc.turnoutPercentage}%", color = BharatSaffron)
                    }
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
        }

        // Constituency List View
        Text(
            text = "Constituencies Overview (${uiState.constituencies.size})",
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(6.dp))

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(uiState.constituencies) { pc ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { selectedConstituency = pc },
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "${pc.name} (${pc.id})",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "${pc.state} • Polling: ${pc.pollingDate}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Text(
                            text = "${pc.turnoutPercentage}% Turnout",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = BharatGreen
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun InfoStat(label: String, value: String, color: Color) {
    Column {
        Text(label, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
        Text(value, style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold), color = color)
    }
}
