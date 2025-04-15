package sstu.grivvus.notes

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import sstu.grivvus.notes.data.AppNote
import sstu.grivvus.notes.data.shortify
import sstu.grivvus.notes.data.tagsToString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.window.Popup
import androidx.navigation.NavController
import sstu.grivvus.notes.data.AddWhat
import sstu.grivvus.notes.data.AppTag
import sstu.grivvus.notes.data.DatabaseInterface
import sstu.grivvus.notes.data.convertMillisToDate
import java.time.Instant

@Composable
fun NewButton(
    navController: NavController?,
    state: AddWhat,
    modifier: Modifier = Modifier,
) {
    val onClick: () -> Unit
    if (state == AddWhat.Note) {
        onClick = {navController!!.navigate("NewNoteScreen")}
    } else if (state == AddWhat.Tag) {
        onClick = {navController!!.navigate("NewTagScreen")}
    } else {
        throw RuntimeException("Impossible event")
    }
    Column {
        Row(modifier = modifier.fillMaxWidth().padding(8.dp)) {
            Button(onClick = onClick, modifier = modifier.fillMaxWidth()) {
                Text("Добавить")
            }
        }
        Spacer(Modifier.height(15.dp))
    }
}

@Composable
fun MainPageHeader(navController: NavController?) {
    Column {
        Spacer(Modifier.height(30.dp))
        Row(modifier = Modifier.fillMaxWidth().padding(8.dp), horizontalArrangement = Arrangement.SpaceAround) {
            Column(modifier = Modifier, horizontalAlignment = Alignment.Start) {
                Button(onClick = {navController!!.navigate("NotesScreen")}, modifier = Modifier.size(150.dp, 40.dp)) {
                    Text("Заметки")
                }
            }
            Column(modifier = Modifier, horizontalAlignment = Alignment.End) {
                Button(onClick = {navController!!.navigate("TagsScreen")}, modifier = Modifier.size(150.dp, 40.dp)) {
                    Text("Теги")
                }
            }
        }
    }
}

@Composable
fun NotesList(
    modifier: Modifier = Modifier,
    content: List<AppNote>,
    navController: NavController?
) {
    Column(modifier = modifier.fillMaxWidth().padding(3.dp)) {
        for (note in content) {
            FoldedNote(note, navController)
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun NoteView(
    modifier: Modifier = Modifier,
    note: AppNote,
    navController: NavController?,
) {
    var noteTitle by remember { mutableStateOf(note.title) }
    var noteText by remember { mutableStateOf(note.text) }
    var noteDateState = remember { mutableStateOf(note.dateOfCreation?.toEpochMilli() ?: Instant.now().toEpochMilli()) }

    Box(modifier = modifier.fillMaxSize()) {
        Row(modifier = modifier.padding(top=100.dp, bottom = 220.dp).fillMaxSize()) {
            Column(verticalArrangement = Arrangement.SpaceAround,
                modifier = Modifier.fillMaxSize()) {
                Row{
                    Column(modifier = modifier.fillMaxWidth()) {
                        Row(modifier = Modifier.fillMaxWidth().padding(bottom = 5.dp)) {
                            Text("Дата")
                        }
                        Row(modifier = Modifier.fillMaxWidth()) {
                            DatePickerDocked(noteDateState)
                        }
                    }
                }
                Row {
                    Column(modifier = modifier.fillMaxWidth()) {
                        Row(modifier = Modifier.fillMaxWidth().padding(bottom = 5.dp)) {
                            Text("Заголовок")
                        }
                        Row(modifier = Modifier.fillMaxWidth()) {
                            TextField(
                                value = noteTitle, onValueChange = {noteTitle = it},
                                modifier = modifier.fillMaxWidth()
                            )
                        }
                    }
                }
                Row {
                    Column(modifier = modifier.fillMaxWidth()) {
                        Row(modifier = Modifier.fillMaxWidth().padding(bottom = 5.dp)) {
                            Text("Текст")
                        }
                        Row(modifier = Modifier.fillMaxWidth()) {
                            TextField(noteText, onValueChange = {noteText = it}, modifier = modifier.fillMaxWidth())
                        }
                    }
                }
                Row {
                    Column {
                        Row {
                            Button(onClick = {
                                DialogBuilder.addTagDialog(note)
                                DialogBuilder.show()
                            }) {
                                Text("Добавить тег")
                            }
                        }
                        FlowRow {
                            for (tag in note.tags) {
                                SuggestionChip(onClick = {}, label = { Text(tag.name) })
                            }
                        }
                    }
                }
            }
        }
        Row(modifier = Modifier.align(Alignment.TopEnd).background(Color.White)) {
            Column {
                Spacer(Modifier.height(30.dp))
                Row {
                    Button(onClick = {
                        DatabaseInterface.removeNote(note)
                        navController!!.navigate("NotesScreen")
                    }) { Text("Удалить") }
                }
            }
        }
        Row(modifier = Modifier.align(Alignment.BottomCenter).background(Color.White).padding(bottom = 50.dp)){
            Column {
                Button(onClick = {
                    note.title = noteTitle
                    note.text = noteText
                    note.dateOfCreation = Instant.ofEpochMilli(noteDateState.value)
                    DatabaseInterface.saveNote(note)
                    navController!!.navigate("NotesScreen")
                }) { Text("Сохранить") }
            }
            Column {
                Button(onClick = {navController!!.navigate("NotesScreen")}) { Text("Отмена") }
            }
        }
    }
}



@Composable
fun TagView(
    tag: AppTag, modifier: Modifier = Modifier,
    navController: NavController?
) {
    var tagName by remember { mutableStateOf(tag.name) }
    val thisNotes = DatabaseInterface.getNotesByTag(tag)

    Box(modifier = modifier.fillMaxSize()) {
        Row(modifier = modifier.padding(top=100.dp, bottom = 100.dp).fillMaxSize()) {
            Column(verticalArrangement = Arrangement.Top,
                modifier = Modifier.fillMaxSize()) {
                Row{
                    Column(modifier = modifier.fillMaxWidth()) {
                        Row(modifier = Modifier.fillMaxWidth().padding(bottom = 5.dp)) {
                            Text("Имя")
                        }
                        Row(modifier = Modifier.fillMaxWidth()) {
                            TextField(
                                tagName, onValueChange = {tagName = it}
                            )
                        }
                    }
                }
                Row {
                    Spacer(Modifier.height(30.dp))
                }
                Row {
                    Column() {
                        for (note in thisNotes) {
                            FoldedNote(note, navController = navController)
                        }
                    }
                }
            }
        }
        Row(modifier = Modifier.align(Alignment.TopEnd).background(Color.White)) {
            Column {
                Spacer(Modifier.height(30.dp))
                Row {
                    Button(onClick = {
                        DatabaseInterface.removeTag(tag)
                        navController!!.navigate("TagsScreen")
                    }) { Text("Удалить") }
                }
            }
        }
        Row(modifier = Modifier.align(Alignment.BottomCenter).background(Color.White).padding(bottom = 50.dp)){
            Column {
                Button(onClick = {
                    tag.name = tagName
                    DatabaseInterface.saveTag(tag)
                    navController!!.navigate("TagsScreen")
                }) { Text("Сохранить") }
            }
            Column {
                Button(onClick = {navController!!.navigate("TagsScreen")}) { Text("Отмена") }
            }
        }
    }
}

@Composable
fun FoldedNote(
    note: AppNote, navController: NavController? = null,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.padding(1.dp)
            .clickable(enabled = true, onClick = {
                navController!!.navigate("NoteView/${note.id}")
            })
    ) {
        Column {
            HorizontalDivider(thickness = 2.dp)
            Row(modifier = modifier) {
                Text(convertMillisToDate(note.dateOfCreation?.toEpochMilli() ?: Instant.now().toEpochMilli()))
            }
            Row(modifier = modifier) {
                Text(text = note.title, fontWeight = FontWeight.Bold)
            }
            Row(modifier = modifier) {
                Text(shortify(note.text))
            }
            Row(modifier = modifier) {
                Text(tagsToString(note.tags))
            }
            HorizontalDivider(thickness = 2.dp)
            Spacer(modifier = modifier.height(5.dp))
        }
    }
}

@Composable
fun NewNoteScreen(navController: NavController) {
    NoteView(note = AppNote(null), navController = navController)
}

@Composable
fun NewTagScreen(navController: NavController) {
    TagView(tag = AppTag(null), navController = navController)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerDocked(noteDateState: MutableState<Long>) {
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = noteDateState.value)
    val selectedDate = datePickerState.selectedDateMillis?.let {
        convertMillisToDate(it)
    } ?: ""

    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = selectedDate,
            onValueChange = { },
            label = { Text("Когда?") },
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = { showDatePicker = !showDatePicker }) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Выберите дату"
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
        )

        if (showDatePicker) {
            Popup(
                onDismissRequest = {
                    noteDateState.value = datePickerState.selectedDateMillis ?: 0
                    showDatePicker = false
                },
                alignment = Alignment.TopStart
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset(y = 64.dp)
                        .shadow(elevation = 4.dp)
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(16.dp)
                ) {
                    DatePicker(
                        state = datePickerState,
                        showModeToggle = false
                    )
                }
            }
        }
    }
}