package com.abrahamputra0058.asesment1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.abrahamputra0058.asesment1.navigation.SetupNavGraph
import com.abrahamputra0058.asesment1.ui.theme.Asesment1Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Asesment1Theme {
                SetupNavGraph()
            }
        }
    }
}

