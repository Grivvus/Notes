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
        val note = DatabaseProvider.getDB().noteDao().findNote(id)
        if (note == null) {
            return AppNote(null)
        }
        val noteApp = noteToAppNote(note)
        getTagsOfNote(noteApp)
        return noteApp
    }

    fun getTagById(id: Int): AppTag {
        if (id == -1){
            return AppTag(null)
        }
        val tag = DatabaseProvider.getDB().tagDao().findTag(id)
        if (tag == null) {
            return AppTag(null)
        }
        return tagToAppTag(tag)
    }

    fun removeTag(tag: AppTag) {
        if (tag.id == null) {
            return
        }
        DatabaseProvider.getDB().noteTagDao().deleteByTag(tag.id)
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
        DatabaseProvider.getDB().noteDao().delete(note.id)
    }

    fun saveNote(note: AppNote) {
        if (note.id == null || note.id == -1) {
            DatabaseProvider.getDB().noteDao().insertOne(appNoteToNote(note))
            val curId = DatabaseProvider.getDB().noteDao().getNoteByAll(
                note.title, note.dateOfCreation ?: Instant.now(), note.text
            )

            updateNoteTags(
                listOf<AppTag>(),
                AppNote(curId, note.title, note.dateOfCreation, note.text, note.tags)
            )
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
        val notes = DatabaseProvider.getDB().noteTagDao().findNotesByTagId(tag.id ?: -1)
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
                DatabaseProvider.getDB().noteTagDao().deleteOne(oldTag.id!!, note.id ?: -1)
            }
        }
        for (newTag in note.tags) {
            DatabaseProvider.getDB().noteTagDao().insertOne(note.id!!, newTag.id!!)
        }
    }
}