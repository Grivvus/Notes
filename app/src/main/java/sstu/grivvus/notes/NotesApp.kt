package sstu.grivvus.notes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import sstu.grivvus.notes.data.AppNote
import sstu.grivvus.notes.data.fetchAllTags
import sstu.grivvus.notes.ui.theme.NotesTheme
import java.time.Instant

@Composable
fun NotesApp() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "NotesScreen"){
        composable("NotesScreen") {NotesScreen(navController)}
        composable("TagsScreen") {TagsScreen(navController)}
    }
}

@Preview(showBackground = true)
@Composable
fun NotesScreen(navController: NavController? = null) {
    NotesTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            Row(modifier = Modifier.verticalScroll(rememberScrollState()).absolutePadding(top = 85.dp)){
                NotesList(
                    content = listOf(
                        AppNote(1, "note 1", Instant.now(), text = "some text", tags = listOf("tag1" ,"tag2")),
                        AppNote(2, "note 2", Instant.now(), text = "some another text", tags = listOf("tag1", "tag3")),
                        AppNote(1, "note 1", Instant.now(), text = "some text", tags = listOf("tag1" ,"tag2")),
                        AppNote(2, "note 2", Instant.now(), text = "some another text", tags = listOf("tag1", "tag3")),
                        AppNote(1, "note 1", Instant.now(), text = "some text", tags = listOf("tag1" ,"tag2")),
                        AppNote(2, "note 2", Instant.now(), text = "some another text", tags = listOf("tag1", "tag3")),
                        AppNote(1, "note 1", Instant.now(), text = "some text", tags = listOf("tag1" ,"tag2")),
                        AppNote(2, "note 2", Instant.now(), text = "some another text", tags = listOf("tag1", "tag3")),
                    )
                )
            }
            Row(modifier = Modifier.align(Alignment.TopCenter).background(Color.White)) {
                MainPageHeader(navController)
            }
            Row(modifier = Modifier.align(Alignment.BottomCenter).background(Color.White)) {
                NewButton({})
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Preview(showBackground = true)
@Composable
fun TagsScreen(navController: NavController? = null) {
    val tags = fetchAllTags()
    NotesTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .absolutePadding(top = 85.dp, left = 10.dp, right = 10.dp)
            ) {
                Column {
                    FlowRow(
                        horizontalArrangement = Arrangement.Start,
                        maxItemsInEachRow = 3,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        for (tag in tags) {
                            SuggestionChip(
                                {}, {Text(text = tag.name, fontSize = 22.sp)},
                                modifier = Modifier.padding(end = 10.dp)
                            )
                        }
                    }
                }
            }
            Row(modifier = Modifier.align(Alignment.TopCenter).background(Color.White)) {
                MainPageHeader(navController)
            }
            Row(modifier = Modifier.align(Alignment.BottomCenter).background(Color.White)) {
                NewButton({})
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