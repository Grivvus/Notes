package sstu.grivvus.notes

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import sstu.grivvus.notes.ui.theme.NotesTheme

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    NotesTheme {
        Greeting("Android")
    }
}