@file:OptIn(ExperimentalTextApi::class)

package dev.piotrprus.composeparticleemitter

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.drawText
import androidx.compose.ui.unit.toSize

fun DrawScope.draw(
    canvasParticle: CanvasParticle
) {
    when (canvasParticle.shape) {
        ParticleShape.Circle -> {
            val centerX = canvasParticle.currentPosition.x.toPx()
            val centerY = canvasParticle.currentPosition.y.toPx()
            withTransform({
                scale(
                    scaleX = canvasParticle.scale,
                    scaleY = canvasParticle.scale,
                    pivot = Offset(centerX, centerY)
                )
            }) {
                drawCircle(
                    color = canvasParticle.color,
                    radius = canvasParticle.size.toSize().width / 2,
                    center = Offset(
                        x = centerX,
                        y = centerY
                    ), blendMode = canvasParticle.blendMode,
                    alpha = canvasParticle.alpha
                )
            }
        }

        is ParticleShape.Image -> {
            val centerX = canvasParticle.currentPosition.x.toPx()
            val centerY = canvasParticle.currentPosition.y.toPx()
            val colorFilter = ColorFilter.tint(color = canvasParticle.color)
            val imageScale =
                (canvasParticle.size.width.toPx() / canvasParticle.shape.imageBitmap.width)
                    .coerceAtMost(1f)

            withTransform({
                scale(
                    scaleX = canvasParticle.scale * imageScale,
                    scaleY = canvasParticle.scale * imageScale,
                    pivot = Offset(centerX, centerY)
                )
                rotate(
                    degrees = canvasParticle.rotation * canvasParticle.scale,
                    Offset(centerX, centerY)
                )
                translate(
                    left = centerX - canvasParticle.shape.imageBitmap.width / 2,
                    top = centerY - canvasParticle.shape.imageBitmap.height / 2
                )
            }) {
                drawImage(
                    image = canvasParticle.shape.imageBitmap,
                    blendMode = canvasParticle.blendMode,
                    alpha = canvasParticle.alpha,
                    colorFilter = colorFilter,
                    filterQuality = FilterQuality.Medium,
                )
            }
        }

        is ParticleShape.PathShape -> {
            val centerX = canvasParticle.currentPosition.x.toPx()
            val centerY = canvasParticle.currentPosition.y.toPx()
            val width = canvasParticle.size.toSize().width
            val height = canvasParticle.size.toSize().height

            withTransform({
                scale(
                    scaleX = canvasParticle.scale,
                    scaleY = canvasParticle.scale,
                    pivot = Offset(centerX, centerY)
                )
                rotate(
                    degrees = canvasParticle.rotation * canvasParticle.scale,
                    Offset(centerX, centerY)
                )
                translate(left = centerX - width / 6, top = centerY - height / 6)
            }) {
                drawPath(
                    color = canvasParticle.color,
                    path = canvasParticle.shape.shapePath,
                    alpha = canvasParticle.alpha,
                    blendMode = canvasParticle.blendMode
                )
            }
        }

        is ParticleShape.Text -> {
            val centerX = canvasParticle.currentPosition.x.toPx()
            val centerY = canvasParticle.currentPosition.y.toPx()
            withTransform({
                scale(
                    scaleX = canvasParticle.scale,
                    scaleY = canvasParticle.scale,
                    pivot = Offset(centerX, centerY)
                )
                rotate(
                    degrees = canvasParticle.rotation * canvasParticle.scale,
                    Offset(centerX, centerY)
                )
                translate(
                    left = centerX,
                    top = centerY
                )
            }) {
                drawText(
                    textMeasurer = canvasParticle.shape.textMeasurer,
                    text = canvasParticle.shape.text,
                    blendMode = canvasParticle.blendMode,
                    style = canvasParticle.shape.textStyle.copy(color = canvasParticle.color),
                    topLeft = size.center - canvasParticle.shape.textMeasurer.measure(
                        text = canvasParticle.shape.text,
                        style = canvasParticle.shape.textStyle
                    ).size.toSize().center
                )
            }
        }
    }
}