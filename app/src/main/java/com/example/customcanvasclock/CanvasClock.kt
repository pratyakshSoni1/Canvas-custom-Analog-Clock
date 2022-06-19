package com.example.customcanvasclock

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.*


private val clockStyle = Paint().apply {
    style = PaintingStyle.Stroke
    color = Color.Green
    strokeWidth = 12f
    pathEffect = PathEffect.dashPathEffect(floatArrayOf(2f,2f))

}

private val hourHandStyle = Paint().apply {
    strokeCap = StrokeCap.Round
    color = Color.Blue
    strokeWidth = 20f

}
private val minuteHandStyle = Paint().apply {
    strokeCap = StrokeCap.Round
    color = Color.Green
    strokeWidth = 20f

}
private val secondHandStyle = Paint().apply {
    strokeCap = StrokeCap.Round
    color = Color.Black
    strokeWidth = 20f

}

private val clockRadius:Float = 300f
private val hourHandRadius:Float = (clockRadius * 0.5).toFloat()
private val minuteHandRadius:Float = (clockRadius * 0.8 ).toFloat()
private val secondHandRadius:Float = (clockRadius * 0.8 ).toFloat()



@Composable
fun MainScreenClockTut() {

    val hour = remember { mutableStateOf(10) }
    val minute = remember { mutableStateOf(10) }
    val second = remember { mutableStateOf(30) }

    Box(modifier=Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
        androidx.compose.foundation.Canvas(
            modifier = Modifier
                .fillMaxSize(0.65f)
        ) {

            drawIntoCanvas {

                DrawFrame(this, it)
                DrawHourHand(this, it, hour, hourHandRadius)
                DrawMinuteHand(this, it, minute, minuteHandRadius)
                DrawSecond(this, it, second, secondHandRadius)

            }
        }

        DrawDigits(Color.Blue, hour,true) //hour
        DrawDigits(Color.Green, minute, false) //minute
        DrawDigits(Color.Black, second, false) //seconds

    }


    LaunchedEffect(key1 = true ){

        val dateFormatter = SimpleDateFormat("hh:mm:ss")

        while(true){

            val calendar = Calendar.getInstance()
            val cureentTime = dateFormatter.format(calendar.time).split(":")

            val tempHour = (cureentTime[0]).toInt()
            val tempMinute = (cureentTime[1]).toInt()
            val tempSecond = (cureentTime[2]).toInt()

            hour.value = tempHour
            minute.value = tempMinute
            second.value = tempSecond

            Log.d("CUSTOM_CLOCK","TimeGiven : ${hour.value}:${minute.value}:${second.value}")
            delay(500)

        }

    }

}

private fun DrawFrame(drawScope: DrawScope, it: Canvas) {

    it.drawCircle(drawScope.center, radius = clockRadius, clockStyle)

}

private fun DrawMinuteHand(drawScope: DrawScope, it: Canvas, minute: MutableState<Int>, minuteHandRadius:Float) {

    it.save()
    it.rotate(minute.value * 6f,
        pivotX = drawScope.center.x,
        pivotY = drawScope.center.y
    )
    it.drawLine(
        Offset(drawScope.center.x, drawScope.center.y),
        Offset(drawScope.center.x, drawScope.center.y - minuteHandRadius),
        paint = minuteHandStyle
    )
    it.restore()
}

private fun DrawSecond(drawScope: DrawScope, it: Canvas, second: MutableState<Int>, secondHandRadius:Float) {

    it.save()
    it.rotate(second.value * 6f,
        pivotX = drawScope.center.x,
        pivotY = drawScope.center.y
    )
    it.drawLine(
        Offset(drawScope.center.x, drawScope.center.y),
        Offset(drawScope.center.x, drawScope.center.y - secondHandRadius),
        paint = secondHandStyle
    )
    it.restore()
}

private fun DrawHourHand(drawScope: DrawScope, it: Canvas, hour: MutableState<Int>, hourHandRadius:Float) {

    it.save()
    it.rotate(hour.value * 30f,
        pivotX = drawScope.center.x,
        pivotY = drawScope.center.y
    )
    it.drawLine(
        Offset(drawScope.center.x, drawScope.center.y),
        Offset(drawScope.center.x, drawScope.center.y - hourHandRadius),
        paint = hourHandStyle
    )
    it.restore()
}

@Composable
private fun BoxScope.DrawDigits(color: Color, digit: MutableState<Int>, isHourHand:Boolean) {

    val rotationAngle = if(isHourHand){digit.value * 30f}else{ digit.value * 6f }

    Box(modifier= Modifier
        .size((clockRadius + 60f).dp)
        .rotate(rotationAngle)
        .aspectRatio(1f / 1f), contentAlignment = Alignment.TopCenter){

        Text(text = digit.value.toString(), modifier = Modifier.rotate(rotationAngle - (rotationAngle*2)), fontSize = 18.sp, color = color)

    }

}
