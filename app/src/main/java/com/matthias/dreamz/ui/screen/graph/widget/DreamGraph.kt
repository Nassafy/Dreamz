package com.matthias.dreamz.ui.screen.graph.widget

import android.graphics.Typeface
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.flow.Flow
import java.text.DateFormatSymbols
import java.util.*

@Composable
fun DreamGraph(year: Int, getDream: (Int) -> Flow<HashMap<Int, Int>>) {
    val dreamDayMap =
        getDream(year).collectAsState(initial = hashMapOf()).value
    val max = 31//if (dreamDayMap.isEmpty()) 0 else dreamDayMap.maxOf { it.value }
    val textPaint = Paint().asFrameworkPaint().apply {
        isAntiAlias = true
        textSize = 36.sp.value
        typeface = Typeface.create(Typeface.MONOSPACE, Typeface.BOLD)
        color = MaterialTheme.colors.secondary.toArgb()
    }
    val color = MaterialTheme.colors.primary
    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .padding(all = 10.dp)
    ) {
        val canvasWidth = size.width
        val canvasHeight = size.height
        val ySpacing = 300f
        val xSpacing = 100f
        val yBaseLine = canvasHeight - ySpacing
        val maxHeight = canvasHeight - 2 * ySpacing
        val barWidth = (canvasWidth - xSpacing * 2f) / (12f)
        val textValueOffset = 20f
        drawLine(
            start = Offset(x = xSpacing, y = yBaseLine),
            end = Offset(x = canvasWidth - xSpacing, y = yBaseLine),
            color = color,
            strokeWidth = 10f
        )
        for (i in 0 until dreamDayMap.size) {
            val month = DateFormatSymbols(Locale.getDefault()).months[i]
            val height = ((dreamDayMap[i + 1]?.toFloat() ?: 0f) / max) * maxHeight
            val topLeft = Offset(x = xSpacing + i * barWidth, y = ySpacing + maxHeight - height)
            drawRect(
                color = color,
                size = Size(width = barWidth * 0.8f, height = height),
                topLeft = topLeft
            )
            drawIntoCanvas {
                val monthTextPoint =
                    Offset(x = xSpacing + barWidth * i, y = canvasHeight - ySpacing * 0.8f)
                it.nativeCanvas.save()
                it.nativeCanvas.rotate(50f, monthTextPoint.x, monthTextPoint.y)
                it.nativeCanvas.drawText(month, monthTextPoint.x, monthTextPoint.y, textPaint)
                it.nativeCanvas.restore()

                val valueTextPoint =
                    Offset(
                        x = xSpacing + barWidth * i,
                        y = ySpacing + maxHeight - height - textValueOffset
                    )
                it.nativeCanvas.drawText(
                    dreamDayMap[i + 1].toString(),
                    valueTextPoint.x,
                    valueTextPoint.y,
                    textPaint
                )
            }
        }
    }
}