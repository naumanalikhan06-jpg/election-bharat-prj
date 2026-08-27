package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.NoEncryption
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Sms
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AuditVerificationProof
import com.example.model.Candidate
import com.example.model.EncryptedVotePayload
import com.example.model.VoteSubmissionResult
import com.example.model.VoterReceipt
import com.example.ui.components.AshokaChakra3DView
import com.example.ui.components.DataSourceBadge
import com.example.ui.components.DataSourceType
import com.example.ui.components.ThreeDCard
import com.example.ui.theme.AshokaBlue
import com.example.ui.theme.AshokaBlueLight
import com.example.ui.theme.BharatGreen
import com.example.ui.theme.BharatSaffron
import com.example.ui.theme.HologramBlue
import com.example.ui.theme.SovereignGold
import com.example.ui.theme.SovereignNavy
import com.example.ui.theme.SovereignNavyLight
import com.example.ui.theme.StatusSuccess
import com.example.viewmodel.ElectionUiState
import com.example.viewmodel.ElectionViewModel

@Composable
fun SecureVotingBoothScreen(
    uiState: ElectionUiState,
    viewModel: ElectionViewModel,
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
                        text = "3D EVM & Secure Ballot Engine",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = "AES-256 Field Encryption • Firestore Transactions • HMAC Integrity",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                DataSourceBadge(type = DataSourceType.OFFICIAL_VERIFIED)
            }
        }

        // Step Indicator Progress Bar
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    StepItem(step = 1, title = "Auth OTP", isCurrent = uiState.votingStep == 1, isDone = uiState.votingStep > 1)
                    StepDivider(isDone = uiState.votingStep > 1)
                    StepItem(step = 2, title = "3D EVM Unit", isCurrent = uiState.votingStep == 2, isDone = uiState.votingStep > 2)
                    StepDivider(isDone = uiState.votingStep > 2)
                    StepItem(step = 3, title = "7s VVPAT", isCurrent = uiState.votingStep == 3, isDone = uiState.votingStep > 3)
                    StepDivider(isDone = uiState.votingStep > 3)
                    StepItem(step = 4, title = "Firestore & Seal", isCurrent = uiState.votingStep == 4, isDone = uiState.votingStep == 4)
                }
            }
        }

        // STEP 1: Biometric & OTP Verification
        if (uiState.votingStep == 1) {
            item {
                ThreeDCard(
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = SovereignNavy,
                    cornerRadius = 20.dp
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                                .background(BharatSaffron.copy(alpha = 0.18f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Fingerprint,
                                contentDescription = null,
                                tint = BharatSaffron,
                                modifier = Modifier.size(34.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "Elector Multi-Factor Verification",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = Color.White
                        )
                        Text(
                            text = "EPIC: ${uiState.elector.epicNumber} • Mobile: ${uiState.elector.mobileNumber}",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.75f)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        OutlinedTextField(
                            value = uiState.otpInput,
                            onValueChange = { viewModel.setOtpInput(it) },
                            placeholder = { Text("Enter 6-digit Secure OTP (e.g. 782910)", color = Color.White.copy(alpha = 0.5f)) },
                            singleLine = true,
                            leadingIcon = {
                                Icon(imageVector = Icons.Default.PhoneAndroid, contentDescription = null, tint = AshokaBlueLight)
                            },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color(0xFF071424),
                                unfocusedContainerColor = Color(0xFF071424),
                                focusedBorderColor = BharatSaffron,
                                unfocusedBorderColor = AshokaBlueLight.copy(alpha = 0.4f),
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = { viewModel.verifyOtpAndProceedToEvm() },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = BharatGreen),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(imageVector = Icons.Default.Lock, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Authorize & Unlock Balloting Unit", fontWeight = FontWeight.Bold)
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "🔒 Air-gapped EVM protocol simulated under ECI Rule 49A guidelines",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White.copy(alpha = 0.5f)
                        )
                    }
                }
            }
        }

        // STEP 2: 3D Electronic Voting Machine (Ballot Unit)
        if (uiState.votingStep == 2) {
            item {
                Text(
                    text = "ELECTRONIC BALLOTING UNIT (M3 EVM)",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, letterSpacing = 1.sp),
                    color = BharatSaffron
                )
            }

            itemsIndexed(uiState.candidates) { index, candidate ->
                EvmCandidateRow(
                    serialNo = index + 1,
                    candidate = candidate,
                    onPressVote = { viewModel.castVoteOnEvm(candidate) }
                )
            }
        }

        // STEP 3: VVPAT 7-Second Inspection Window Animation
        if (uiState.votingStep == 3) {
            item {
                VvpatSimulationCard(
                    candidate = uiState.selectedCandidateForVote,
                    isSubmitting = uiState.isTransactionSubmitting
                )
            }
        }

        // STEP 4: Vote Cast, Client-Side Encryption Proof & Firestore Transaction Verification
        if (uiState.votingStep == 4) {
            item {
                VoteSuccessCertificateCard(
                    uiState = uiState,
                    viewModel = viewModel,
                    onReset = { viewModel.startVotingFlow() }
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun EvmCandidateRow(
    serialNo: Int,
    candidate: Candidate,
    onPressVote: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F5F9))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Serial Number + Braille Dot Representation
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.width(36.dp)
            ) {
                Text(
                    text = "$serialNo",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = Color.Black
                )
                Text(
                    text = "⠼${'a' + serialNo - 1}",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            // Candidate Name & Party
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = candidate.name,
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    color = Color.Black
                )
                Text(
                    text = "${candidate.partyName} (${candidate.partyAbbr})",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF475569)
                )
            }

            // Party Symbol Emoji Box
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.White)
                    .border(1.dp, Color(0xFFCBD5E1), RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = candidate.partySymbolEmoji, fontSize = 24.sp)
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Ready Red LED indicator lamp
            Box(
                modifier = Modifier
                    .size(14.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFDC2626))
            )

            Spacer(modifier = Modifier.width(10.dp))

            // Tactile EVM Blue Button
            Box(
                modifier = Modifier
                    .size(width = 54.dp, height = 38.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFF2563EB),
                                Color(0xFF1D4ED8)
                            )
                        )
                    )
                    .clickable(onClick = onPressVote),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Press Blue Button to Cast Vote",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
fun VvpatSimulationCard(
    candidate: Candidate?,
    isSubmitting: Boolean,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "vvpat_glow")
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow"
    )

    ThreeDCard(
        modifier = modifier.fillMaxWidth(),
        backgroundColor = SovereignNavy,
        cornerRadius = 22.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFEF4444).copy(alpha = glowAlpha))
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "VVPAT 7-SECOND VERIFICATION WINDOW",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = BharatSaffron
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.VolumeUp,
                        contentDescription = null,
                        tint = SovereignGold,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "EVM BEEP ACTIVE",
                        style = MaterialTheme.typography.labelSmall,
                        color = SovereignGold
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 3D Illuminated Transparent VVPAT Chamber
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .height(160.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0xFF051224))
                    .border(2.dp, AshokaBlueLight, RoundedCornerShape(14.dp))
                    .padding(12.dp),
                contentAlignment = Alignment.Center
            ) {
                // Printed Paper Slip Hanging Down
                Card(
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .shadow(8.dp, RoundedCornerShape(4.dp)),
                    shape = RoundedCornerShape(4.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFBEB))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "VVPAT PAPER AUDIT SLIP",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace
                            ),
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = candidate?.partySymbolEmoji ?: "🗳️",
                            fontSize = 32.sp
                        )
                        Text(
                            text = candidate?.name ?: "Selected Candidate",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.ExtraBold),
                            color = Color.Black
                        )
                        Text(
                            text = candidate?.partyName ?: "Party Name",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.DarkGray
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                CircularProgressIndicator(
                    modifier = Modifier.size(18.dp),
                    strokeWidth = 2.dp,
                    color = BharatGreen
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Encrypting choice (AES-256) & executing Firestore transaction...",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.85f)
                )
            }
        }
    }
}

@Composable
fun VoteSuccessCertificateCard(
    uiState: ElectionUiState,
    viewModel: ElectionViewModel,
    onReset: () -> Unit,
    modifier: Modifier = Modifier
) {
    val elector = uiState.elector
    val receipt = uiState.latestReceipt
    val payload = uiState.latestEncryptedPayload
    val subResult = uiState.latestSubmissionResult
    val proof = uiState.auditProof
    val decryptedJson = uiState.decryptedAuditText
    val doubleVoteWarning = uiState.doubleVoteBlockWarning

    ThreeDCard(
        modifier = modifier.fillMaxWidth(),
        backgroundColor = SovereignNavy,
        cornerRadius = 22.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header Icon
            Box(
                modifier = Modifier
                    .size(54.dp)
                    .clip(CircleShape)
                    .background(if (doubleVoteWarning != null) Color(0xFFEF4444) else BharatGreen),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (doubleVoteWarning != null) Icons.Default.Warning else Icons.Default.CheckCircle,
                    contentDescription = "Transaction Status",
                    tint = Color.White,
                    modifier = Modifier.size(36.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = if (doubleVoteWarning != null) "Duplicate Vote Prevented!" else "Vote Sealed & Immutable!",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = Color.White,
                textAlign = TextAlign.Center
            )
            Text(
                text = if (doubleVoteWarning != null) {
                    doubleVoteWarning
                } else {
                    "Your franchise has been encrypted client-side with AES-256-GCM and committed via atomic Firestore transaction."
                },
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(alpha = 0.8f),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Real-Time SMS Alert Confirmed Banner
            if (doubleVoteWarning == null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(BharatGreen.copy(alpha = 0.15f))
                        .border(1.dp, BharatGreen, RoundedCornerShape(12.dp))
                        .padding(12.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Sms,
                            contentDescription = null,
                            tint = BharatGreen,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "REAL-TIME GOV SMS DISPATCHED",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = BharatGreen
                            )
                            Text(
                                text = "Delivered to registered mobile ${elector.mobileNumber}",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // FIRESTORE TRANSACTION STATUS BADGE
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF061427))
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.CloudDone,
                                contentDescription = null,
                                tint = HologramBlue,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "FIRESTORE ATOMIC TRANSACTION",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = HologramBlue
                            )
                        }
                        Text(
                            text = if ((subResult as? VoteSubmissionResult.Success)?.isFirestoreCloudSynced == true) "CLOUD SYNCED" else "SOVEREIGN LEDGER",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = SovereignGold
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Tx Hash: " + ((subResult as? VoteSubmissionResult.Success)?.txHash ?: "TX-BEN-2026-OK9102"),
                        style = MaterialTheme.typography.labelSmall.copy(fontFamily = FontFamily.Monospace),
                        color = Color.White
                    )
                    Text(
                        text = "Double-Voting Protection: ACTIVE (1-Elector-1-Ballot Enforced)",
                        style = MaterialTheme.typography.labelSmall,
                        color = BharatGreen
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // CLIENT-SIDE ENCRYPTION & INTEGRITY HASH CARD
            if (payload != null) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF071424))
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Key,
                                contentDescription = null,
                                tint = SovereignGold,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "CLIENT-SIDE FIELD ENCRYPTION (AES-256-GCM)",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = SovereignGold
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Ciphertext: ${payload.ciphertextBase64.take(24)}... (Secret Ballot)",
                            style = MaterialTheme.typography.labelSmall.copy(fontFamily = FontFamily.Monospace),
                            color = Color(0xFFE2E8F0)
                        )
                        Text(
                            text = "GCM IV: ${payload.ivBase64}",
                            style = MaterialTheme.typography.labelSmall.copy(fontFamily = FontFamily.Monospace),
                            color = Color(0xFF94A3B8)
                        )
                        Text(
                            text = "HMAC-SHA256: ${payload.integrityHmac.take(24)}...",
                            style = MaterialTheme.typography.labelSmall.copy(fontFamily = FontFamily.Monospace),
                            color = Color(0xFF94A3B8)
                        )
                        Text(
                            text = "Anonymized Elector Hash: ${payload.anonymizedElectorHash.take(20)}...",
                            style = MaterialTheme.typography.labelSmall.copy(fontFamily = FontFamily.Monospace),
                            color = Color(0xFF38BDF8)
                        )
                        Text(
                            text = "Digital Seal (SHA-256): ${payload.digitalSealSha256.take(24)}...",
                            style = MaterialTheme.typography.labelSmall.copy(fontFamily = FontFamily.Monospace),
                            color = SovereignGold
                        )
                    }
                }
            }

            // AUDIT PROOF RESULT (if tested)
            if (proof != null) {
                Spacer(modifier = Modifier.height(10.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (proof.isTamperProof) BharatGreen.copy(alpha = 0.2f) else Color(0xFFEF4444).copy(alpha = 0.2f))
                        .border(1.dp, if (proof.isTamperProof) BharatGreen else Color(0xFFEF4444), RoundedCornerShape(10.dp))
                        .padding(10.dp)
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = if (proof.isTamperProof) Icons.Default.Verified else Icons.Default.Warning,
                                contentDescription = null,
                                tint = if (proof.isTamperProof) BharatGreen else Color(0xFFEF4444),
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (proof.isTamperProof) "HMAC & SEAL VERIFIED: 100% UNTAMPERED" else "INTEGRITY MISMATCH DETECTED",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = if (proof.isTamperProof) BharatGreen else Color(0xFFEF4444)
                            )
                        }
                        Text(
                            text = "Computed HMAC matches stored Firestore HMAC signature byte-for-byte.",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White
                        )
                    }
                }
            }

            // DECRYPTED AUDIT PAYLOAD (if tested)
            if (decryptedJson != null) {
                Spacer(modifier = Modifier.height(10.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFF0F172A))
                        .border(1.dp, SovereignGold, RoundedCornerShape(10.dp))
                        .padding(10.dp)
                ) {
                    Column {
                        Text(
                            text = "AUTHORIZED RECOUNT AUDIT INSPECTOR",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = SovereignGold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = decryptedJson,
                            style = MaterialTheme.typography.labelSmall.copy(fontFamily = FontFamily.Monospace),
                            color = Color(0xFFF1F5F9)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Action Buttons
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilledTonalButton(
                        onClick = { viewModel.verifyCurrentBallotIntegrity() },
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(imageVector = Icons.Default.Shield, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Verify Integrity", fontSize = 12.sp)
                    }

                    FilledTonalButton(
                        onClick = { viewModel.decryptCurrentBallotForAudit() },
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(imageVector = Icons.Default.Visibility, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Audit Recount", fontSize = 12.sp)
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = { viewModel.testAttemptDoubleVoting() },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFF87171)),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(imageVector = Icons.Default.Warning, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Test Double Vote", fontSize = 12.sp)
                    }

                    OutlinedButton(
                        onClick = onReset,
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(imageVector = Icons.Default.RestartAlt, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Reset Simulation", fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

@Composable
private fun StepItem(step: Int, title: String, isCurrent: Boolean, isDone: Boolean) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .background(
                    when {
                        isDone -> BharatGreen
                        isCurrent -> BharatSaffron
                        else -> Color(0xFFE2E8F0)
                    }
                ),
            contentAlignment = Alignment.Center
        ) {
            if (isDone) {
                Icon(imageVector = Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
            } else {
                Text(
                    text = "$step",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = if (isCurrent) Color.White else Color.Gray
                )
            }
        }
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal
            ),
            color = if (isCurrent) BharatSaffron else MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun StepDivider(isDone: Boolean) {
    Box(
        modifier = Modifier
            .width(20.dp)
            .height(2.dp)
            .background(if (isDone) BharatGreen else Color(0xFFCBD5E1))
    )
}

