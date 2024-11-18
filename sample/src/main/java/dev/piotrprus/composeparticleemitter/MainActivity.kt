package dev.piotrprus.composeparticleemitter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseInCirc
import androidx.compose.animation.core.EaseInCubic
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.center
import androidx.compose.ui.unit.dp
import dev.piotrprus.composeparticleemitter.ui.theme.ComposeParticleEmitterTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposeParticleEmitterTheme {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
//                        .background(Color.DarkGray)
                ) {
                    val animX = remember { Animatable(0f) }
                    LaunchedEffect(Unit) {
                        repeat(50) {
                            animX.animateTo(1f, tween())
                            animX.animateTo(0f)
                        }
                    }
                    val screenWidth = LocalConfiguration.current.screenWidthDp
                    val density = LocalDensity.current
                    val widthPx = with(density) { screenWidth.dp.roundToPx() }
                    var progress by remember { mutableFloatStateOf(0.3f) }
                    Box(modifier = Modifier.align(Alignment.Center)) {
                        Text(
                            text = "Droidcon Lisbon is the best Android conference on the planet. \nI am learning so much stuff here :)",
                            style = MaterialTheme.typography.bodyLarge
                        )
                        BreakParticles(
                            size = DpSize(
                                boostRotationContainerSize,
                                boostRotationContainerSize
                            ), progress = progress,
                            modifier = Modifier.offset { IntOffset(x = (100 * animX.value).toInt(), y = 0) }
                        )
                    }
//                    Button(
//                        modifier = Modifier
//                            .align(Alignment.BottomCenter)
//                            .padding(40.dp),
//                        onClick = {
//                            if (progress < 1.5f) progress += 0.1f else progress = 0f
//                        }) {
//                        Text(text = "Click me")
//                    }
                }
            }
        }
    }
}

@Composable
fun TapParticles(modifier: Modifier = Modifier) {
    val tapEffects = remember { mutableStateListOf<CanvasEmitterConfig>() }
}

val boostBubbleSize = 50.dp
val boostRotationContainerSize = 70.dp

@Composable
private fun BreakParticles(size: DpSize, progress: Float, modifier: Modifier) {
    val context = LocalContext.current
    val ovalImage = remember { ImageBitmap.imageResource(context.resources, R.drawable.oval_low_q) }
    CanvasParticleEmitter(
        modifier = modifier
            .size(size)
            .graphicsLayer {
                scaleX = progress
                scaleY = progress
            }, CanvasEmitterConfig(
            particlePerSecond = 50,
            emitterCenter = size.center,
            startRegionShape = CanvasEmitterConfig.Shape.OVAL,
            startRegionSize = size,
            particleShapes = listOf(ParticleShape.Image(ovalImage)),
            lifespanRange = IntRange(2000, 2000),
            colors = listOf(
                Color.Blue,
                Color.Magenta,
                Color.Cyan
            ),
            blendMode = BlendMode.Screen,
            translateEasing = FastOutLinearInEasing,
            alphaEasing = EaseInCirc,
            scaleEasing = EaseInCubic,
            particleSizes = listOf(DpSize(40.dp, 40.dp)),
            flyDistancesDp = IntRange(3, 5),
            spread = IntRange(-180, 180),
            fadeOutTime = IntRange(2000, 2000),
            rotationRange = IntRange(0, 0),
            scaleTime = IntRange(2000, 2000),
            targetScaleRange = IntRange(2, 2),
            startScaleRange = IntRange(1, 2),
        )
    )
}