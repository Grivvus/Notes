package sstu.grivvus.notes.data

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
    return formatter.format(Date(millis))
}

fun shortify(str: String): String {
    return str
}

fun tagsToString(tags: List<AppTag>): String {
    if (tags == null || tags.size == 0) {
        return ""
    }
    if (tags.size > 2) {
        return "${tags[0].name}, ${tags[1].name}, ..."
    }
    if (tags.size == 1){
        return tags[0].name
    }
    return "${tags[0].name}, ${tags[1].name}"
}

fun tagsToStrings(tags: List<AppTag>): List<String> {
    val tagsText: MutableList<String> = mutableListOf()
    for (tag in tags) {
        tagsText.addLast(tag.name)
    }
    return tagsText
}

enum class AddWhat {
    Note, Tag
}

object DatabaseInterface {

    fun getNoteById(id: Int): AppNote {
        if (id == -1) {
            return AppNote(null)
        }
        val noteApp = noteToAppNote(DatabaseProvider.getDB().noteDao().findNote(id))
        getTagsOfNote(noteApp)
        return noteApp
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
            val appNote = noteToAppNote(note)
            getTagsOfNote(appNote)
            println(appNote.tags)
            appNotes.addLast(appNote)
        }
        return appNotes
    }

    fun getTagsOfNote(note: AppNote): AppNote {
        if (note.id == null || note.id == -1){
            return note
        }
        val tags = DatabaseProvider.getDB().noteTagDao().findTagsByNoteId(note.id)
        for (tag in tags) {
            note.tags.addLast(AppTag(tag.id, tag.name))
        }
        return note
    }

    fun getNotesByTag(tag: AppTag): List<AppNote> {
        val notes = DatabaseProvider.getDB().noteTagDao().findNotesByTagId(tag.id!!)
        val appNotes: MutableList<AppNote> = mutableListOf()
        for (note in notes) {
            appNotes.addLast(
                AppNote(
                    note.id, note.title, note.dateOfCreation, note.text ?: ""
                )
            )
        }
        return appNotes
    }

    fun updateNoteTags(oldTags: List<AppTag>, note: AppNote) {
        for (oldTag in oldTags) {
            if (oldTag !in note.tags) {
                DatabaseProvider.getDB().noteTagDao().deleteOne(oldTag.id!!, note.id!!)
            }
        }
        for (newTag in note.tags) {
            println(newTag)
            DatabaseProvider.getDB().noteTagDao().insertOne(note.id!!, newTag.id!!)
        }
    }
}