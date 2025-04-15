package sstu.grivvus.notes.data

import java.time.Instant

data class AppNote(
    val id: Int?,
    var title: String = "",
    var dateOfCreation: Instant? = null,
    var text: String = "",
    val tags: MutableList<AppTag> = mutableListOf()
)

data class AppTag(
    val id: Int?,
    var name: String = ""
)

fun appNoteToNote(an: AppNote): Note {
    return Note(
        an.id ?: 0, an.title, an.dateOfCreation ?: Instant.now(), an.text
    )
}

fun appTagToTag(at: AppTag): Tag {
    return Tag(
        at.id ?: 0, at.name
    )
}

fun tagToAppTag(tag: Tag): AppTag {
    return AppTag(tag.id, tag.name)
}

fun noteToAppNote(note: Note): AppNote {
    return AppNote(
        note.id, note.title, note.dateOfCreation, note.text ?: ""
    )
}