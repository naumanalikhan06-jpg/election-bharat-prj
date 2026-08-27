package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Science
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.ui.theme.BharatGreen
import com.example.ui.theme.BharatSaffron
import com.example.ui.theme.SovereignGold

enum class DataSourceType {
    OFFICIAL_VERIFIED,
    INFORMATIONAL_GUIDE,
    DEMO_SIMULATION
}

@Composable
fun DataSourceBadge(
    type: DataSourceType,
    modifier: Modifier = Modifier
) {
    val (bg, fg, icon, label) = when (type) {
        DataSourceType.OFFICIAL_VERIFIED -> Quadruple(
            BharatGreen.copy(alpha = 0.15f),
            BharatGreen,
            Icons.Default.CheckCircle,
            "Official ECI Verified Data"
        )
        DataSourceType.INFORMATIONAL_GUIDE -> Quadruple(
            SovereignGold.copy(alpha = 0.15f),
            SovereignGold,
            Icons.Default.Info,
            "Official Guidance Source"
        )
        DataSourceType.DEMO_SIMULATION -> Quadruple(
            BharatSaffron.copy(alpha = 0.15f),
            BharatSaffron,
            Icons.Default.Science,
            "Demonstration / Sandbox Simulation"
        )
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(bg)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = fg,
                modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = fg
            )
        }
    }
}

private data class Quadruple<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)
