package com.louisfn.somovie.ui.common.modifier

import androidx.compose.animation.core.DurationBasedAnimationSpec
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer

private const val DefaultInitialScale = 1f
private const val DefaultTargetScale = .9f
private const val DefaultAnimationDuration = 50

@Composable
fun Modifier.shake(
    enabled: Boolean,
    initialScale: Float = DefaultInitialScale,
    targetScale: Float = DefaultTargetScale,
    animation: DurationBasedAnimationSpec<Float> = tween(
        durationMillis = DefaultAnimationDuration,
        easing = LinearEasing,
    ),
) = if (enabled) {
    val infiniteTransition = rememberInfiniteTransition(label = "ShakeInfiniteTransition")
    val scale by infiniteTransition.animateFloat(
        initialValue = initialScale,
        targetValue = targetScale,
        animationSpec = infiniteRepeatable(
            animation = animation,
            repeatMode = RepeatMode.Reverse,
        ),
        label = "Shake",
    )
    graphicsLayer {
        scaleX = scale
        scaleY = scale
    }
} else {
    this
}
