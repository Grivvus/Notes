package sstu.grivvus.notes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp

// если будут баги со стартом, СМОТРЕТЬ СЮДА, в репе по другому сделано
@HiltAndroidApp
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { NotesApp() }
    }
}