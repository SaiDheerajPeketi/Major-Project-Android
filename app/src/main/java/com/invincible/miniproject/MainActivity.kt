package com.invincible.miniproject

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.invincible.miniproject.ui.theme.MiniProjectTheme
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieConstants

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MiniProjectTheme {
                var userNameInput by remember {
                    mutableStateOf("")
                }
                var password by remember {
                    mutableStateOf("")
                }
                var passwordVisibility by remember { 
                    mutableStateOf(false) 
                }
                val icon = if (passwordVisibility)
                    painterResource(id = R.drawable.show_star)
                else
                    painterResource(id = R.drawable.show_eye)

//                LottieAnimation()
                


                Column(
                    modifier = Modifier.fillMaxSize().background(Color.White),
                    verticalArrangement = Arrangement.SpaceEvenly,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "Mini Project Login", fontSize = 40.sp)
                    LottieAnimation()
//

                    TextField(
                        value = userNameInput,
                        onValueChange = {
                            userNameInput = it
                        },
                        placeholder = { Text(text = "Username") }
                    )

                    TextField(
                        value = password,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        onValueChange = {
                            password = it
                        },
                        placeholder = { Text(text = "Password") },
                        trailingIcon = {
                            IconButton(onClick = {
                                passwordVisibility = !passwordVisibility
                            }) {
                                Icon(
                                    painter = icon,
                                    contentDescription = "Visibility Icon"
                                )
                            }
                        },
                        visualTransformation = if (passwordVisibility) VisualTransformation.None
                        else PasswordVisualTransformation()
                    )

                    Button(
                        onClick = {
                            val intent: Intent = Intent(this@MainActivity, LiveDataActivity::class.java)
                            startActivity(intent)
                        }
                    ) {
                        Text(text = "Login")
                    }

                    OutlinedButton(
                            onClick = {
                                val intent: Intent = Intent(this@MainActivity, DoctorActivity::class.java)
                                startActivity(intent)
                            }
                        ) {
                            Text(text = "Doctor")
                        }
                }
            }
        }
    }
}

@Composable
fun LottieAnimation() {
    // Way 1: Using a Raw Animation File
    val rawComposition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.moving_blocks))

    // Way 2: Using an Animation URL
//    val urlComposition by rememberLottieComposition(spec = LottieCompositionSpec.Url("https://lottie.host/0696621c-a35b-48df-adb9-631e81d6ca59/5nlsTfOUgN.lottie"))

    val progress by animateLottieCompositionAsState(composition = rawComposition) // Use 'urlComposition' for Way 2

//    LottieAnimation(
//        composition = rawComposition, // Use 'urlComposition' for Way 2
//        progress = progress,
//        modifier = Modifier.fillMaxSize(),
//        contentScale = ContentScale.FillBounds,
//        colorFilter = ColorFilter.tint(Color.Black)
//    )

    LottieAnimation(
        composition = rawComposition,
        iterations = LottieConstants.IterateForever,
//        progress = progress,
        modifier = Modifier.size(200.dp),
        contentScale = ContentScale.FillBounds,

    )
}