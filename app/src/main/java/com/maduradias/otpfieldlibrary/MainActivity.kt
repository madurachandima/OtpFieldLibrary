package com.maduradias.otpfieldlibrary

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.maduradias.otpfield.OtpFields
import com.maduradias.otpfield.OtpState
import com.maduradias.otpfieldlibrary.ui.theme.OtpFieldLibraryTheme
import kotlinx.coroutines.flow.MutableStateFlow

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            OtpFieldLibraryTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    Column(
                        modifier = Modifier
                            .padding(innerPadding)
                            .consumeWindowInsets(innerPadding)
                    ) {
                        Greeting("Android")

                        OtpFields(
                            onKeyboardValueChange = {
                                Log.d("TAG", "OtpField one : $it")
                            },
                            modifier = Modifier,
                            fieldCount = 6,
                            otpStateFlow = remember { MutableStateFlow(OtpState()) }
                        )

                        OtpFields(
                            onKeyboardValueChange = {
                                Log.d("TAG", "OtpField two : $it")
                            },
                            modifier = Modifier,
                            fieldCount = 5,
                            otpStateFlow = remember { MutableStateFlow(OtpState()) }
                        )
                    }

                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}
