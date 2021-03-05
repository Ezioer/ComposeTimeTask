/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.androiddevchallenge

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.androiddevchallenge.ui.theme.MyTheme
import kotlin.math.min

var progress by mutableStateOf(0L)
var inputTime by mutableStateOf("")
var totalTime by mutableStateOf(0L)

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyTheme {
                MyApp()
            }
        }
    }
}

// Start building your app here!
@Composable
fun MyApp() {
    val textStyle = MaterialTheme.typography
    Column(modifier = Modifier.padding(20.dp)) {

        TimerCircle(elapsedTime = progress, totalTime = totalTime)
        TextField(
            value = inputTime,
            onValueChange = {
                if (it.isNullOrEmpty()) {
                    inputTime = ""
                    totalTime = 0
                    progress = 0
                } else {
                    inputTime = it
                    totalTime = it.toLong()
                    progress = it.toLong()
                }
            },
            modifier = Modifier
                .padding(top = 20.dp)
                .align(Alignment.CenterHorizontally)
        )
        Text(
            "count down--${progress}s", style = textStyle.h4,
            modifier = Modifier
                .padding(top = 20.dp)
                .align(Alignment.CenterHorizontally)
        )
        Button(
            onClick = {
                if (totalTime == 0L) {
                    return@Button
                }
                val timer = object : CountDownTimer(totalTime * 1000, 1000) {
                    override fun onTick(millisUntilFinished: Long) {
                        progress--
                        Log.i("time", "progress=$progress")
                    }

                    override fun onFinish() {
                    }
                }
                timer.start()
            },
            modifier = Modifier
                .padding(top = 20.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            Text(text = "start")
        }
    }
}

@Preview("Light Theme", widthDp = 360, heightDp = 640)
@Composable
fun LightPreview() {
    MyTheme {
        MyApp()
    }
}

@Composable
fun TimerCircle(
    elapsedTime: Long,
    totalTime: Long
) {
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp),
        onDraw = {
            val strokeSize = 20.dp
            val radiusOffset = 6

            val xCenter = size.width / 2f
            val yCenter = size.height / 2f
            val radius = min(xCenter, yCenter)
            val arcWidthHeight = ((radius - radiusOffset) * 2f)
            val arcSize = Size(arcWidthHeight, arcWidthHeight)

            val remainderColor = Color.Gray
            val completedColor = Color.Red

            val grayPercent =
                min(1f, elapsedTime.toFloat() / totalTime.toFloat())
            val redPercent = 1 - grayPercent

            drawArc(
                completedColor,
                270f,
                -redPercent * 360f,
                false,
                topLeft = Offset(radiusOffset.toFloat(), radiusOffset.toFloat()),
                size = arcSize,
                style = Stroke(width = strokeSize.value)
            )

            drawArc(
                remainderColor,
                270f,
                grayPercent * 360,
                false,
                topLeft = Offset(radiusOffset.toFloat(), radiusOffset.toFloat()),
                size = arcSize,
                style = Stroke(width = strokeSize.value)
            )
        }
    )
}
