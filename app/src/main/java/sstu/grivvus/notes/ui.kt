package sstu.grivvus.notes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.unit.dp
import sstu.grivvus.notes.data.AppNote
import sstu.grivvus.notes.data.instantToStringDate
import sstu.grivvus.notes.data.shortify
import sstu.grivvus.notes.data.tagsToString
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController

@Composable
fun NewButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
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
    content: List<AppNote>
) {
    Column(modifier = modifier.fillMaxWidth().padding(3.dp)) {
        for (note in content) {
            Row(modifier = modifier.padding(1.dp)) {
                Column {
                    HorizontalDivider(thickness = 2.dp)
                    Row(modifier = modifier) {
                        Text(instantToStringDate(note.dateOfCreation))
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
    }
}

@Composable
fun NoteView(
    modifier: Modifier = Modifier,
    note: AppNote
) {
    var noteTitle by remember { mutableStateOf(note.title) }
    var noteText by remember { mutableStateOf(note.text) }

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
                            Text(instantToStringDate(note.dateOfCreation))
                        }
                    }
                }
                Row {
                    Column(modifier = modifier.fillMaxWidth()) {
                        Row(modifier = Modifier.fillMaxWidth().padding(bottom = 5.dp)) {
                            Text("Заголовок")
                        }
                        Row(modifier = Modifier.fillMaxWidth()) {
                            TextField(value = noteTitle, onValueChange = {}, modifier = modifier.fillMaxWidth())
                        }
                    }
                }
                Row {
                    Column(modifier = modifier.fillMaxWidth()) {
                        Row(modifier = Modifier.fillMaxWidth().padding(bottom = 5.dp)) {
                            Text("Текст")
                        }
                        Row(modifier = Modifier.fillMaxWidth()) {
                            TextField(noteText, onValueChange = {}, modifier = modifier.fillMaxWidth())
                        }
                    }
                }
            }
        }
        Row(modifier = Modifier.align(Alignment.TopEnd).background(Color.White)) {
            Column {
                Spacer(Modifier.height(30.dp))
                Row {
                    Button(onClick = {}) { Text("Удалить") }
                }
            }
        }
        Row(modifier = Modifier.align(Alignment.BottomCenter).background(Color.White).padding(bottom = 50.dp)){
            Column {
                Button(onClick = {}) { Text("Сохранить") }
            }
            Column {
                Button(onClick = {}) { Text("Отмена") }
            }
        }
    }
}