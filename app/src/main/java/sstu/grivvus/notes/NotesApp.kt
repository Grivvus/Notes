package sstu.grivvus.notes

import android.app.AlertDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import sstu.grivvus.notes.data.AddWhat
import sstu.grivvus.notes.data.AppNote
import sstu.grivvus.notes.data.DatabaseInterface
import sstu.grivvus.notes.data.tagsToStrings
import sstu.grivvus.notes.ui.theme.NotesTheme

@Composable
fun NotesApp() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "NotesScreen"){
        composable("NotesScreen") {NotesScreen(navController)}
        composable("TagsScreen") {TagsScreen(navController)}
        composable("NoteView/{noteId}") { backStackEntry ->
            val noteId = backStackEntry.arguments?.getString("noteId")?.toInt() ?: -1
            NoteView(note = DatabaseInterface.getNoteById(noteId), navController = navController)
        }
        composable("TagView/{tagId}") { backStackEntry ->
            val tagId = backStackEntry.arguments?.getString("tagId")?.toInt() ?: -1
            TagView(tag = DatabaseInterface.getTagById(tagId), navController = navController)
        }
        composable("NewNoteScreen") { NewNoteScreen(navController) }
        composable("NewTagScreen") { NewTagScreen(navController) }
    }
}

@Preview(showBackground = true)
@Composable
fun NotesScreen(navController: NavController? = null) {
    NotesTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            Row(modifier = Modifier.verticalScroll(rememberScrollState())
                .absolutePadding(top = 85.dp, bottom = 85.dp)
            ) {
                NotesList(
                    content = DatabaseInterface.getAllNotes(), navController = navController
                )
            }
            Row(modifier = Modifier.align(Alignment.TopCenter).background(Color.White)) {
                MainPageHeader(navController)
            }
            Row(modifier = Modifier.align(Alignment.BottomCenter).background(Color.White)) {
                NewButton(navController, AddWhat.Note)
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Preview(showBackground = true)
@Composable
fun TagsScreen(navController: NavController? = null) {
    val tags = DatabaseInterface.getAllTags()
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
                                { navController!!.navigate("TagView/${tag.id}") },
                                {Text(text = tag.name, fontSize = 22.sp)},
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
                NewButton(navController, AddWhat.Tag)
            }
        }
    }
}

object DialogBuilder {
    lateinit var builder: AlertDialog.Builder
    fun init(b: AlertDialog.Builder) {
        builder = b
    }

    fun addTagDialog(note: AppNote) {
        val tagsCopy = note.tags.toList()
        val allTags = DatabaseInterface.getAllTags()
        val allTagsText = tagsToStrings(allTags).toTypedArray()
        val activated = BooleanArray(allTags.size, {false})
        allTags.forEachIndexed { i, tag ->
            if (tag in tagsCopy) {
                activated[i] = true
            }
        }
        builder
            .setTitle("Выберите теги")
            .setPositiveButton("Сохранить") { dialog, which ->
                note.tags.clear()
                for (i in activated.indices) {
                    if (activated[i]){
                        note.tags.addLast(allTags[i])
                    }
                }
                DatabaseInterface.updateNoteTags(tagsCopy, note)
            }
            .setNegativeButton("Отмена") { dialog, which ->
                dialog.cancel()
            }
            .setMultiChoiceItems(
                allTagsText, activated
            ) { dialog, which, isChecked ->
                activated[which] = isChecked
            }
        builder.create()
    }

    fun show() {
        builder.show()
    }
}