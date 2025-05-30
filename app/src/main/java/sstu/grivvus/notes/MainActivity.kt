package sstu.grivvus.notes

import android.app.AlertDialog
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import sstu.grivvus.notes.data.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DatabaseProvider.initDB(this)
        DialogBuilder.init(AlertDialog.Builder(this))
        enableEdgeToEdge()
        setContent { NotesApp() }
    }
}