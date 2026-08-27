package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Accessibility
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Brightness4
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Language
import com.example.model.UserRole
import com.example.ui.theme.AshokaBlue
import com.example.ui.theme.AshokaBlueLight
import com.example.ui.theme.BharatGreen
import com.example.ui.theme.BharatSaffron
import com.example.ui.theme.SovereignNavy
import com.example.ui.theme.SovereignNavyLight

@Composable
fun SovereignHeader(
    currentLanguage: Language,
    onLanguageSelected: (Language) -> Unit,
    currentRole: UserRole,
    onRoleSelected: (UserRole) -> Unit,
    isHighContrast: Boolean,
    onToggleHighContrast: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showLangMenu by remember { mutableStateOf(false) }
    var showRoleMenu by remember { mutableStateOf(false) }

    Column(modifier = modifier.fillMaxWidth()) {
        // Official Sovereign Tricolor Top Stripe
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(4.dp)
                    .background(BharatSaffron)
            )
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(4.dp)
                    .background(Color.White)
            )
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(4.dp)
                    .background(BharatGreen)
            )
        }

        // Main Sovereign Navigation Bar
        Surface(
            color = SovereignNavy,
            shadowElevation = 8.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Left: Emblem + Brand Title
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    AshokaChakra3DView(
                        size = 36.dp,
                        showOuterRays = false
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "BHARAT ELECTION NEXUS",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 0.5.sp
                                ),
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = Icons.Default.VerifiedUser,
                                contentDescription = "Verified Sovereign Platform",
                                tint = AshokaBlueLight,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        Text(
                            text = "भारत निर्वाचन आयोग • Operations & Citizen Portal",
                            style = MaterialTheme.typography.labelSmall,
                            color = BharatSaffron.copy(alpha = 0.9f)
                        )
                    }
                }

                // Right: Language Dropdown, Role Switcher & High Contrast Quick Toggle
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    // Language Selector Pill
                    Box {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(SovereignNavyLight)
                                .clickable { showLangMenu = true }
                                .padding(horizontal = 8.dp, vertical = 5.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Language,
                                contentDescription = "Select Language",
                                tint = AshokaBlueLight,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = currentLanguage.code.uppercase(),
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = Color.White
                            )
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = null,
                                tint = Color.White.copy(alpha = 0.7f),
                                modifier = Modifier.size(16.dp)
                            )
                        }

                        DropdownMenu(
                            expanded = showLangMenu,
                            onDismissRequest = { showLangMenu = false }
                        ) {
                            Language.entries.forEach { lang ->
                                DropdownMenuItem(
                                    text = {
                                        Text("${lang.nativeName} (${lang.displayName})")
                                    },
                                    onClick = {
                                        onLanguageSelected(lang)
                                        showLangMenu = false
                                    }
                                )
                            }
                        }
                    }

                    // Role Switcher Pill
                    Box {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(BharatSaffron.copy(alpha = 0.15f))
                                .clickable { showRoleMenu = true }
                                .padding(horizontal = 8.dp, vertical = 5.dp)
                        ) {
                            Text(
                                text = currentRole.badge,
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                                color = BharatSaffron
                            )
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = null,
                                tint = BharatSaffron,
                                modifier = Modifier.size(16.dp)
                            )
                        }

                        DropdownMenu(
                            expanded = showRoleMenu,
                            onDismissRequest = { showRoleMenu = false }
                        ) {
                            UserRole.entries.forEach { role ->
                                DropdownMenuItem(
                                    text = {
                                        Column {
                                            Text(role.title, fontWeight = FontWeight.Bold)
                                            Text(role.level, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                                        }
                                    },
                                    onClick = {
                                        onRoleSelected(role)
                                        showRoleMenu = false
                                    }
                                )
                            }
                        }
                    }

                    // High Contrast Toggle
                    IconButton(
                        onClick = onToggleHighContrast,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Accessibility,
                            contentDescription = "Toggle Accessibility High Contrast",
                            tint = if (isHighContrast) BharatGreen else Color.White.copy(alpha = 0.8f),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}
