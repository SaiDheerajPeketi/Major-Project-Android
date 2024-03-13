package com.invincible.miniproject

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.invincible.miniproject.ui.theme.MiniProjectTheme
import kotlinx.coroutines.delay
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue


class LiveDataActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MiniProjectTheme {
                var listState by remember { mutableStateOf(listOf(60.0f, 70.0f, 60.0f, 55.0f, 90.0f, 50.0f, 60.0f, 70.0f, 75.0f, 70.0f, 60.0f, 65.0f, 60.0f)) }

                BPMDisplay(currValue = listState.get(listState.lastIndex))
                
                PerformanceChart(list = listState)

                LaunchedEffect(key1 = true) {
                    while(true){
                        getLatestData()
                        delay(500)
                        val nextList: MutableList<Float> = mutableListOf()
                        val first = listState.first()
                        nextList.addAll(listState.drop(1))
                        nextList.add(first)
                        listState = nextList
                    }
                }
            }
        }
    }

    private fun getLatestData() {
        val retrofitBuilder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("http://20.205.136.25/")
            .build()
            .create(ApiInterface::class.java)

        val retrofitData = retrofitBuilder.getLatestEntry()

        retrofitData.enqueue(object : Callback<ResponseDataClass?> {
            override fun onResponse(
                call: Call<ResponseDataClass?>,
                response: Response<ResponseDataClass?>
            ) {
                val responseValue = response.body()!!

                val responseFloatValue = responseValue.response.toFloat()

                Log.e("myDebugTag", "onResponse: " + responseFloatValue, )
            }

            override fun onFailure(call: Call<ResponseDataClass?>, t: Throwable) {
                Log.e("myDebugTag", "onFailure: " + t.message, )
            }
        })
    }
}

@Composable
fun PerformanceChart(modifier: Modifier = Modifier, list: List<Float>) {
    Row(modifier = modifier.fillMaxSize()) {
        val max = list.maxOrNull()
        val min = list.minOrNull()

        val lineColor = if (list.last() > list.first()) Color.Green else Color.Red

        for (i in 0 until list.size - 1) {
            val fromValuePercentage = getValuePercentageForRange(list[i], max!!, min!!)
            val toValuePercentage = getValuePercentageForRange(list[i + 1], max, min)

            Canvas(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f),
                onDraw = {
                    val fromPoint = Offset(x = 0f, y = size.height * (1 - fromValuePercentage))
                    val toPoint = Offset(x = size.width, y = size.height * (1 - toValuePercentage))

                    drawLine(
                        color = lineColor,
                        start = fromPoint,
                        end = toPoint,
                        strokeWidth = 3f
                    )
                }
            )
        }
    }
}

private fun getValuePercentageForRange(value: Float, max: Float, min: Float): Float {
    return (value - min) / (max - min)
}

@Composable
fun BPMDisplay(currValue: Float){
    Box (modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight(0.3f)
    ){
        
    }
}
