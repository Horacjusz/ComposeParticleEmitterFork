package dev.piotrprus.composeparticleemitter

import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.center
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.util.UUID
import kotlin.math.pow

@Composable
fun CanvasParticleEmitter(
    modifier: Modifier,
    config: CanvasEmitterConfig,
    emitDuration: Long = Long.MAX_VALUE,
    onLastParticleEmitted: () -> Unit = {}
) {

    val itemsToAnimate = remember { mutableStateOf<List<CanvasParticle>>(emptyList()) }
    val lastEmitTime = remember { mutableLongStateOf(0L) }
    var emitting by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        delay(emitDuration)
        emitting = false
        delay(config.lifespanRange.last.toLong())
        onLastParticleEmitted()
    }

    LaunchedEffect(config.colors) {
        while (true) {
            withContext(Dispatchers.IO) {
                withFrameNanos { frameNano ->
                    val items = itemsToAnimate.value + if (emitting) {
                        addNewIfTime(
                            config = config,
                            currentTimeNano = frameNano,
                            lastUpdateTime = lastEmitTime.longValue
                        ).also { if (it.isNotEmpty()) lastEmitTime.longValue = frameNano }
                    } else {
                        emptyList()
                    }
                    itemsToAnimate.value = items.mapNotNull { canvasParticle ->
                        val playTime = frameNano - canvasParticle.startTime
                        if (playTime > canvasParticle.lifespan.times(1_000_000L)) {
                            null
                        } else {
                            val newPosition =
                                canvasParticle.animationConfig.getValueFromNanos(playTimeNanos = playTime)
                            val newScale =
                                canvasParticle.scaleAnimConfig.getValueFromNanos(playTimeNanos = playTime)
                            val newAlpha =
                                canvasParticle.alphaAnimConfig.getValueFromNanos(playTimeNanos = playTime)
                            val hide = if (config.hideInStartRegion) {
                                isPointInCircle(
                                    point = canvasParticle.currentPosition,
                                    size = config.startRegionSize
                                )
                            } else {
                                false
                            }
                            canvasParticle.copy(
                                currentPosition = newPosition,
                                scale = newScale,
                                alpha = newAlpha,
                                hide = hide
                            )
                        }
                    }
                }
            }
        }
    }

    Spacer(modifier = modifier
        .fillMaxSize()
        .drawBehind {
            for (canvasParticle in itemsToAnimate.value) {
                if (canvasParticle.hide.not()) {
                    draw(canvasParticle = canvasParticle)
                }
            }
        })
}

private fun isPointInCircle(point: DpOffset, size: DpSize): Boolean {
    val distanceSquared = (point.x.value - size.center.x.value).toDouble()
        .pow(2) + (point.y.value - size.center.y.value).toDouble().pow(2)
    return distanceSquared <= size.width.value.div(2).toDouble().pow(2)
}

private fun addNewIfTime(
    config: CanvasEmitterConfig,
    currentTimeNano: Long,
    lastUpdateTime: Long
): List<CanvasParticle> =
    when {
        lastUpdateTime == 0L -> List(config.particlesIn100ms) {
            CanvasParticle(
                id = UUID.randomUUID().toString(),
                shape = config.particleShapes.random(),
                color = config.colors.random(),
                startPoint = config.startPoint,
                lifespan = config.lifespanRange.random(),
                easing = FastOutLinearInEasing,
                blendMode = config.blendMode,
                size = config.particleSizes.random(),
                angle = config.spread.random(),
                distance = config.flyDistancesDp.random().dp,
                fadeOutDuration = config.fadeOutTime.random(),
                rotation = config.rotationRange.random(),
                alphaEasing = config.alphaEasing,
                scaleDuration = config.scaleTime.random(),
                scaleEasing = config.scaleEasing,
                targetScale = config.targetScaleRange.random().toFloat(),
                startScale = config.startScaleRange.random().toFloat(),
            )
        }

        currentTimeNano - lastUpdateTime < 100_000_000L -> emptyList()
        else -> List(config.particlesIn100ms) {
            CanvasParticle(
                id = UUID.randomUUID().toString(),
                shape = config.particleShapes.random(),
                color = config.colors.random(),
                startPoint = config.startPoint,
                lifespan = config.lifespanRange.random(),
                easing = FastOutLinearInEasing,
                blendMode = config.blendMode,
                size = config.particleSizes.random(),
                angle = config.spread.random(),
                distance = config.flyDistancesDp.random().dp,
                fadeOutDuration = config.fadeOutTime.random(),
                rotation = config.rotationRange.random(),
                alphaEasing = config.alphaEasing,
                scaleDuration = config.scaleTime.random(),
                scaleEasing = config.scaleEasing,
                targetScale = config.targetScaleRange.random().toFloat(),
                startScale = config.startScaleRange.random().toFloat(),
            )
        }
    }