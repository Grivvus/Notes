package sstu.grivvus.notes

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import sstu.grivvus.notes.data.AddWhat

@Preview(showBackground = true)
@Composable
fun NewNoteButtonPreview() {
    NewButton(null, AddWhat.Tag)
}

@Preview(showBackground = true)
@Composable
fun HeaderPreview() {
    MainPageHeader(null)
}

//@Preview(showBackground = true)
//@Composable
//fun ListViewPreview() {
//    NotesList(content = listOf(
//        AppNote(1, "note 1", Instant.now(), text = "some text", tags = listOf("tag1" ,"tag2")),
//        AppNote(2, "note 2", Instant.now(), text = "some another text", tags = listOf("tag1", "tag3")),
//    ), navController = null)
//}
//
//@Preview(showBackground = true)
//@Composable
//fun NoteChangePreview() {
//    NoteView(
//        note = AppNote(
//            1, "title 1", Instant.now(),
//            "some realy long text some realy long text some realy long text " +
//                    "some realy long text some realy long texsome realy long " +
//                    "text some realy long text some realy long text some realy " +
//                    "long text some realy long text",
//            tags = listOf("tag1", "tag2"),
//        ),
//        navController = null
//    )
//}