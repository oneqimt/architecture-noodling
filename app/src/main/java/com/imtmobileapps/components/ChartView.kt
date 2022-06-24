package com.imtmobileapps.components

import android.graphics.PointF
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.imtmobileapps.model.GeckoCoin
import com.imtmobileapps.util.Constants.CHART_VIEW_TAG
import com.imtmobileapps.util.getDummyGeckoCoin
import logcat.logcat

@Composable
fun ChartView(
    geckoCoin: GeckoCoin
) {

    val arr = geckoCoin.sparklineIn7D.price

    val minY = arr.minOrNull()


    val priceChange = arr.last() - arr.first()
    val lineColor:Color = if (priceChange > 0){
        Color.Green
    }else{
        Color.Red
    }

    val density = LocalDensity.current
    val anim = remember {
        Animatable(0f)
    }

    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = anim, block ={
        anim.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 1000, easing = LinearEasing)
        )
    } )

    val points = mutableListOf<PointF>()
    Column(modifier = Modifier
        .padding(16.dp)
        .wrapContentSize(align = Alignment.Center),
        verticalArrangement = Arrangement.Center
    )

    {
        Canvas(modifier =
        Modifier
            .fillMaxWidth()
            .height(100.dp)
        )
        {
            //let xPosition = geo.size.width / CGFloat(data.count)
            //                        * CGFloat(index + 1)

            //val distance = size.width / (arr.size + 1)
            val maxY = arr.maxOrNull()?.toFloat() ?: 0f

            var currentX = 0f
            arr.forEachIndexed { index, data ->

                logcat(CHART_VIEW_TAG){
                    "INDEX is $index DATA is $data maxY is $maxY"
                }

                val distance = size.width / (arr.size + 1)
                if (arr.size >= index + 2) {
                    val y0 = ((maxY - data) * (size.height / maxY)).toFloat()
                    val x0 = currentX + distance
                    points.add(PointF(x0, y0))
                    currentX += distance
                }
            }

            for (i in 0 until points.size -1) {
                drawLine(
                    start = Offset(points[i].x, points[i].y),
                    end = Offset(points[i + 1].x, points[i + 1].y),
                    color = lineColor,
                    strokeWidth = 8f
                )

               /* drawLine(
                    start = Offset(points[i].x, points[i].y),
                    end = Offset(points[i + 1].x, points[i + 1].y),
                    color = lineColor,
                    strokeWidth = 8f
                )*/

                /* drawLine(
                     color = Color.Black,
                     start = Offset(0f, 0f),
                     end = Offset(animVal.value * size.width, animVal.value * size.height),
                     strokeWidth = 2f
                 )*/
            }
        }
    }

}
@Composable
@Preview
private fun ChartViewPreview(

){
    ChartView(
        geckoCoin = getDummyGeckoCoin())
}