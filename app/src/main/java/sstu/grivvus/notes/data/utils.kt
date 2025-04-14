package sstu.grivvus.notes.data

import java.text.SimpleDateFormat
import java.time.Instant
import java.util.Date
import java.util.Locale

fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
    return formatter.format(Date(millis))
}

fun shortify(str: String): String {
    return str
}

fun tagsToString(tag: List<String>?): String {
    if (tag == null) {
        return "..."
    }
    return "..."
}

fun fetchAllTags(): List<AppTag> {
    val tags = DatabaseProvider.getDB().tagDao().getAll()
    val appTags: MutableList<AppTag> = mutableListOf()
    for (tag in tags) {
        appTags.addLast(tagToAppTag(tag))
    }
    return appTags
}

enum class AddWhat {
    Note, Tag
}

fun getNoteById(id: Int): AppNote {
    if (id == -1) {
        return AppNote(null)
    }
    return noteToAppNote(DatabaseProvider.getDB().noteDao().findNote(id))
}

fun getTagById(id: Int): AppTag {
    if (id == -1){
        return AppTag(null)
    }
    return tagToAppTag(DatabaseProvider.getDB().tagDao().findTag(id))
}

fun removeTag(tag: AppTag) {
    if (tag.id == null) {
        return
    }
    DatabaseProvider.getDB().tagDao().delete(appTagToTag(tag))
}

fun saveTag(tag: AppTag) {
    if (tag.id == null || tag.id == -1){
        DatabaseProvider.getDB().tagDao().insertOne(appTagToTag(tag))
    } else {
        DatabaseProvider.getDB().tagDao().updateOne(appTagToTag(tag))
    }
}

fun getAllTags(): List<AppTag> {
    val tags = DatabaseProvider.getDB().tagDao().getAll()
    val appTags: MutableList<AppTag> = mutableListOf()
    for (tag in tags) {
        appTags.addLast(tagToAppTag(tag))
    }
    return appTags
}

fun removeNote(note: AppNote) {
    if (note.id == null) {
        return
    }
    DatabaseProvider.getDB().noteDao().delete(appNoteToNote(note))
}

fun saveNote(note: AppNote) {
    if (note.id == null || note.id == -1) {
        DatabaseProvider.getDB().noteDao().insertOne(appNoteToNote(note))
    } else {
        println("note ${note.id} updated")
        DatabaseProvider.getDB().noteDao().updateOne(appNoteToNote(note))
    }
}

fun getAllNotes(): List<AppNote> {
    val notes = DatabaseProvider.getDB().noteDao().getAll()
    val appNotes: MutableList<AppNote> = mutableListOf()
    for (note in notes) {
        appNotes.addLast(noteToAppNote(note))
    }
    return appNotes
}