package sstu.grivvus.notes.data

import sstu.grivvus.notes.TagsScreen
import java.time.Instant

fun instantToStringDate(date: Instant): String {
    return "01.01.2025"
}

fun shortify(str: String): String {
    return "..."
}

fun tagsToString(tag: List<String>): String {
    return "..."
}

fun fetchAllTags(): List<AppTag> {
    return listOf(
        AppTag(1, "tag1"),
        AppTag(2, "tag2"),
        AppTag(3, "tag3"),
        AppTag(4, "verylongtag"),
        AppTag(5, "tag5"),
    )
}