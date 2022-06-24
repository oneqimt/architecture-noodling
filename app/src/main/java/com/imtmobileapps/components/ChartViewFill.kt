package com.imtmobileapps.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.imtmobileapps.util.getDataPoints

fun List<DataPoint>.xMax(): Float = maxByOrNull { it.x }?.x ?: 0f
fun List<DataPoint>.yMax(): Float = maxByOrNull { it.y }?.y ?: 0f

fun Float.toRealX(xMax: Float, width: Float) = (this / xMax) * width
fun Float.toRealY(yMax: Float, height: Float) = (this / yMax) * height
data class DataPoint(
    var x: Float,
    var y: Float,
)

@Composable
fun ChartViewFill(
    dataPoint: List<DataPoint>,

    ) {
    Canvas(modifier = Modifier
        .fillMaxWidth(1f)
        .fillMaxHeight(0.7f)
    ) {

        val width = size.width
        val height = size.height
        val spacingOf16DpInPixels = 16.dp.toPx()
        val verticalAxisLineStartOffset = Offset(spacingOf16DpInPixels, spacingOf16DpInPixels)
        val verticalAxisLineEndOffset = Offset(spacingOf16DpInPixels, height)

        drawLine(
            Color.Gray,
            verticalAxisLineStartOffset,
            verticalAxisLineEndOffset,
            strokeWidth = Stroke.DefaultMiter
        )

        val horizontalAxisLineStartOffset = Offset(spacingOf16DpInPixels, height)
        val horizontalAxisLineEndOffset = Offset(width - spacingOf16DpInPixels, height)

        drawLine(
            Color.Gray,
            horizontalAxisLineStartOffset,
            horizontalAxisLineEndOffset,
            strokeWidth = Stroke.DefaultMiter
        )
        val xMax = dataPoint.xMax()
        val yMax = dataPoint.yMax()
        val gradientPath = Path()

        gradientPath.moveTo(spacingOf16DpInPixels, height)

        dataPoint.forEachIndexed { index, curDataPoint ->
            var normX = curDataPoint.x.toRealX(xMax, width)
            val normY = curDataPoint.y.toRealY(yMax, height)

            if (index == 0) normX += spacingOf16DpInPixels
            if (index == dataPoint.size - 1) normX -= spacingOf16DpInPixels

            if (index < dataPoint.size - 1) {
                val offsetStart = Offset(normX, normY)
                var nextNormXPoint = dataPoint[index + 1].x.toRealX(xMax, width)

                if (index == dataPoint.size - 2)
                    nextNormXPoint =
                        dataPoint[index + 1].x.toRealX(xMax, width = width) - spacingOf16DpInPixels

                val nextNormYPoint = dataPoint[index + 1].y.toRealY(yMax, height)
                val offsetEnd = Offset(nextNormXPoint, nextNormYPoint)

                drawLine(
                    Color(0xFFFF0000).copy(alpha = 0.5f),
                    offsetStart,
                    offsetEnd,
                    strokeWidth = Stroke.DefaultMiter
                )
            }

            drawCircle(
                Color(0xFFFF0000).copy(alpha = 0.5f),
                radius = 6.dp.toPx(),
                Offset(normX, normY)
            )
            with(
                gradientPath
            ) {
                lineTo(normX, normY)
            }
        }

        with(
            gradientPath
        ) {
            lineTo(width - spacingOf16DpInPixels, height)
            lineTo(0f, height)
            close()
            drawPath(
                this,
                brush = Brush.verticalGradient(colors = listOf(
                    Color(0xFFFF0000).copy(alpha = 0.5f),
                    Color.LightGray
                ))
            )
        }
    }

}

@Composable
@Preview
fun ChartViewFillPreview(

) {
    ChartViewFill(dataPoint = getDataPoints())
}
