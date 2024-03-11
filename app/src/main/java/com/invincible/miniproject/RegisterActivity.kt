package com.invincible.miniproject

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import com.invincible.miniproject.ui.theme.MiniProjectTheme

class RegisterActivity : ComponentActivity() {
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
                var confirm_password by remember {
                    mutableStateOf("")
                }
                var confirm_passwordVisibility by remember {
                    mutableStateOf(false)
                }
                val icon = if (passwordVisibility)
                    painterResource(id = R.drawable.show_star)
                else
                    painterResource(id = R.drawable.show_eye)
                val confirm_icon = if (confirm_passwordVisibility)
                    painterResource(id = R.drawable.show_star)
                else
                    painterResource(id = R.drawable.show_eye)

                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceEvenly,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
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

                    TextField(
                        value = confirm_password,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        onValueChange = {
                            confirm_password = it
                        },
                        placeholder = { Text(text = "Confirm Password") },
                        trailingIcon = {
                            IconButton(onClick = {
                                confirm_passwordVisibility = !confirm_passwordVisibility
                            }) {
                                Icon(
                                    painter = confirm_icon,
                                    contentDescription = "Visibility Icon"
                                )
                            }
                        },
                        visualTransformation = if (confirm_passwordVisibility) VisualTransformation.None
                        else PasswordVisualTransformation()
                    )

                    Button(
                        onClick = {
                           val intent: Intent = Intent(this@RegisterActivity, LiveDataActivity::class.java)
                           startActivity(intent)
                        }
                    ) {
                        Text(text = "Register")
                    }
                }
            }
        }
    }
}