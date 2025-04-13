package sstu.grivvus.notes.data

import java.time.Instant

data class AppNote(
    val id: Int,
    val title: String,
    val dateOfCreation: Instant,
    val text: String = "",
    val tags: List<String>
)

data class AppTag(
    val id: Int,
    val name: String
)