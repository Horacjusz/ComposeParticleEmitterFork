package dev.piotrprus.composeparticleemitter

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle

sealed interface ParticleShape {
    data object Circle: ParticleShape
    data class PathShape(val shapePath: Path): ParticleShape
    data class Image(val imageBitmap: ImageBitmap): ParticleShape
    data class Text(val text: String, val textStyle: TextStyle, val textMeasurer: TextMeasurer):
        ParticleShape
}