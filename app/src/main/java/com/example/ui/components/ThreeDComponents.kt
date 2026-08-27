package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ui.theme.AshokaBlue
import com.example.ui.theme.AshokaBlueLight
import com.example.ui.theme.BharatGreen
import com.example.ui.theme.BharatSaffron
import com.example.ui.theme.HologramBlue
import com.example.ui.theme.HologramCyan
import com.example.ui.theme.SovereignGold
import com.example.ui.theme.SovereignNavy
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/**
 * 3D Interactive Tilt Modifier: Gives cards realistic 3D perspective, depth shadows,
 * and holographic specular highlight reflecting finger position.
 */
fun Modifier.threeDTilt(
    maxTiltDegrees: Float = 14f,
    enableAutoFloat: Boolean = false
): Modifier = composed {
    var dragX by remember { mutableFloatStateOf(0f) }
    var dragY by remember { mutableFloatStateOf(0f) }

    val infiniteTransition = rememberInfiniteTransition(label = "3d_float")
    val autoTiltX by infiniteTransition.animateFloat(
        initialValue = -3f,
        targetValue = 3f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "auto_x"
    )
    val autoTiltY by infiniteTransition.animateFloat(
        initialValue = 4f,
        targetValue = -4f,
        animationSpec = infiniteRepeatable(
            animation = tween(3800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "auto_y"
    )

    val targetRotX = if (dragY != 0f) -dragY.coerceIn(-maxTiltDegrees, maxTiltDegrees) else (if (enableAutoFloat) autoTiltX else 0f)
    val targetRotY = if (dragX != 0f) dragX.coerceIn(-maxTiltDegrees, maxTiltDegrees) else (if (enableAutoFloat) autoTiltY else 0f)

    val animRotX by animateFloatAsState(targetValue = targetRotX, animationSpec = spring(stiffness = 300f), label = "rotX")
    val animRotY by animateFloatAsState(targetValue = targetRotY, animationSpec = spring(stiffness = 300f), label = "rotY")

    this
        .pointerInput(Unit) {
            detectDragGestures(
                onDragStart = { },
                onDragEnd = {
                    dragX = 0f
                    dragY = 0f
                },
                onDragCancel = {
                    dragX = 0f
                    dragY = 0f
                },
                onDrag = { change, dragAmount ->
                    change.consume()
                    dragX += dragAmount.x * 0.08f
                    dragY += dragAmount.y * 0.08f
                }
            )
        }
        .graphicsLayer {
            rotationX = animRotX
            rotationY = animRotY
            cameraDistance = 18f * density
            shadowElevation = (16f + (animRotX.coerceAtLeast(0f) + animRotY.coerceAtLeast(0f)) * 0.5f)
        }
}

/**
 * 3D Holographic Ashoka Chakra with 24 Spokes, Radiant Energy Glow, and Depth Layers
 */
@Composable
fun AshokaChakra3DView(
    modifier: Modifier = Modifier,
    size: Dp = 120.dp,
    showOuterRays: Boolean = true
) {
    val infiniteTransition = rememberInfiniteTransition(label = "chakra_rotation")
    val rotationAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(24000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "chakra_angle"
    )

    val pulseGlow by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    Box(
        modifier = modifier
            .size(size)
            .graphicsLayer {
                rotationZ = 0f
                cameraDistance = 14f * density
            },
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val center = Offset(this.size.width / 2f, this.size.height / 2f)
            val outerRadius = this.size.minDimension / 2f * 0.88f
            val hubRadius = outerRadius * 0.22f

            // Outer Radiant Hologram Aura
            if (showOuterRays) {
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            HologramCyan.copy(alpha = 0.25f * pulseGlow),
                            AshokaBlue.copy(alpha = 0.15f * pulseGlow),
                            Color.Transparent
                        ),
                        center = center,
                        radius = outerRadius * 1.3f
                    ),
                    radius = outerRadius * 1.3f,
                    center = center
                )
            }

            // Outer Saffron & Green Decorative Glow Rings
            drawCircle(
                brush = Brush.sweepGradient(
                    colors = listOf(
                        BharatSaffron,
                        Color.White,
                        BharatGreen,
                        AshokaBlue,
                        BharatSaffron
                    ),
                    center = center
                ),
                radius = outerRadius,
                center = center,
                style = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round)
            )

            // Inner Ashoka Navy Ring
            drawCircle(
                color = AshokaBlueLight,
                radius = outerRadius * 0.92f,
                center = center,
                style = Stroke(width = 2.dp.toPx())
            )

            // Center Hub
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(SovereignGold, AshokaBlue),
                    center = center,
                    radius = hubRadius
                ),
                radius = hubRadius,
                center = center
            )

            // 24 Ashoka Spokes
            rotate(degrees = rotationAngle, pivot = center) {
                for (i in 0 until 24) {
                    val angleRad = (i * 15) * (PI / 180f).toFloat()
                    val spokeStartX = center.x + hubRadius * cos(angleRad)
                    val spokeStartY = center.y + hubRadius * sin(angleRad)
                    val spokeEndX = center.x + (outerRadius * 0.92f) * cos(angleRad)
                    val spokeEndY = center.y + (outerRadius * 0.92f) * sin(angleRad)

                    drawLine(
                        color = AshokaBlueLight,
                        start = Offset(spokeStartX, spokeStartY),
                        end = Offset(spokeEndX, spokeEndY),
                        strokeWidth = 2.dp.toPx(),
                        cap = StrokeCap.Round
                    )

                    // Small spoke node dots on the rim
                    drawCircle(
                        color = SovereignGold,
                        radius = 2.dp.toPx(),
                        center = Offset(spokeEndX, spokeEndY)
                    )
                }
            }
        }
    }
}

/**
 * 3D Holographic Sovereign Card Container with layered sheen
 */
@Composable
fun ThreeDCard(
    modifier: Modifier = Modifier,
    backgroundColor: Color = SovereignNavy,
    borderColor: Color = AshokaBlueLight.copy(alpha = 0.4f),
    cornerRadius: Dp = 20.dp,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .threeDTilt(maxTiltDegrees = 12f, enableAutoFloat = true)
            .shadow(16.dp, RoundedCornerShape(cornerRadius), ambientColor = BharatSaffron.copy(alpha = 0.3f), spotColor = AshokaBlue.copy(alpha = 0.5f))
            .clip(RoundedCornerShape(cornerRadius))
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        backgroundColor,
                        backgroundColor.copy(alpha = 0.92f),
                        Color(0xFF061121)
                    )
                )
            )
            .drawBehind {
                // Top border accent (Tricolor)
                val strokeWidth = 3.dp.toPx()
                drawLine(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            BharatSaffron,
                            Color.White,
                            BharatGreen
                        )
                    ),
                    start = Offset(0f, 0f),
                    end = Offset(size.width, 0f),
                    strokeWidth = strokeWidth
                )

                // Holographic subtle diagonal specular sheen
                drawPath(
                    path = Path().apply {
                        moveTo(0f, 0f)
                        lineTo(size.width * 0.35f, 0f)
                        lineTo(0f, size.height * 0.5f)
                        close()
                    },
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.08f),
                            Color.Transparent
                        )
                    )
                )
            }
    ) {
        content()
    }
}
