package com.example.ui.screens

import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.DataSourceBadge
import com.example.ui.components.DataSourceType
import com.example.ui.theme.AshokaBlue
import com.example.ui.theme.BharatGreen
import com.example.ui.theme.BharatSaffron
import com.example.ui.theme.SovereignGold
import com.example.ui.theme.SovereignNavy
import com.example.viewmodel.ElectionUiState

@Composable
fun ResultsAnalyticsScreen(
    uiState: ElectionUiState,
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Election Results & Analytics",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = "543 Lok Sabha Seats • Demonstration Data Feed",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                DataSourceBadge(type = DataSourceType.DEMO_SIMULATION)
            }
        }

        // Donut Chart: 543 Lok Sabha Seats Distribution
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(4.dp, RoundedCornerShape(18.dp)),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = SovereignNavy)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "PARLIAMENTARY SEAT SHARE (543 TOTAL)",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = BharatSaffron
                    )
                    Spacer(modifier = Modifier.height(14.dp))

                    // Donut Canvas
                    Box(
                        modifier = Modifier.size(160.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val strokeW = 28.dp.toPx()
                            val radius = size.minDimension / 2f - strokeW / 2f
                            val center = Offset(size.width / 2f, size.height / 2f)

                            // NDA: ~293 seats (194 deg)
                            drawArc(
                                color = BharatSaffron,
                                startAngle = -90f,
                                sweepAngle = 194f,
                                useCenter = false,
                                style = Stroke(width = strokeW),
                                topLeft = Offset(center.x - radius, center.y - radius),
                                size = Size(radius * 2f, radius * 2f)
                            )

                            // INDIA Alliance: ~234 seats (155 deg)
                            drawArc(
                                color = AshokaBlue,
                                startAngle = 104f,
                                sweepAngle = 155f,
                                useCenter = false,
                                style = Stroke(width = strokeW),
                                topLeft = Offset(center.x - radius, center.y - radius),
                                size = Size(radius * 2f, radius * 2f)
                            )

                            // Others: ~16 seats (11 deg)
                            drawArc(
                                color = SovereignGold,
                                startAngle = 259f,
                                sweepAngle = 11f,
                                useCenter = false,
                                style = Stroke(width = strokeW),
                                topLeft = Offset(center.x - radius, center.y - radius),
                                size = Size(radius * 2f, radius * 2f)
                            )
                        }

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "543", style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.ExtraBold), color = Color.White)
                            Text(text = "Majority: 272", style = MaterialTheme.typography.labelSmall, color = Color.White.copy(alpha = 0.7f))
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Chart Legend
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        LegendItem(label = "NDA (293)", color = BharatSaffron)
                        LegendItem(label = "INDIA (234)", color = AshokaBlue)
                        LegendItem(label = "Others (16)", color = SovereignGold)
                    }
                }
            }
        }

        // State-wise Turnout Breakdown
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
                        text = "State Turnout Overview",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    TurnoutBar(state = "Kerala", percent = 73.8f, color = BharatGreen)
                    TurnoutBar(state = "West Bengal", percent = 69.2f, color = BharatGreen)
                    TurnoutBar(state = "Uttar Pradesh", percent = 68.4f, color = BharatSaffron)
                    TurnoutBar(state = "Gujarat", percent = 60.1f, color = AshokaBlue)
                    TurnoutBar(state = "Karnataka", percent = 54.3f, color = SovereignGold)
                    TurnoutBar(state = "Maharashtra", percent = 51.2f, color = Color(0xFFEF4444))
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
private fun LegendItem(label: String, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(color))
        Spacer(modifier = Modifier.width(6.dp))
        Text(text = label, style = MaterialTheme.typography.labelSmall, color = Color.White)
    }
}

@Composable
private fun TurnoutBar(state: String, percent: Float, color: Color) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = state, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface)
            Text(text = "$percent%", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold), color = color)
        }
        Spacer(modifier = Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp))
                .background(Color(0xFFE2E8F0))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(percent / 100f)
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(color)
            )
        }
    }
}
