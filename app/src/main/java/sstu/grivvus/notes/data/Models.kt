package sstu.grivvus.notes.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import java.time.Instant

@Entity(tableName = "note")
data class Note(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "date_of_creation") val dateOfCreation: Instant,
    @ColumnInfo(name = "text") val text: String?,
)

@Entity(tableName = "tag")
data class Tag(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "name") val name: String
)

@Entity(
    tableName = "note_tag", primaryKeys = ["note_id", "tag_id"],
    foreignKeys = [
        ForeignKey(
            entity = Note::class,
            parentColumns = ["id"],
            childColumns = ["note_id"],
            onDelete = Companion.CASCADE
        ),
        ForeignKey(
            entity = Tag::class,
            parentColumns = ["id"],
            childColumns = ["tag_id"],
            onDelete = Companion.CASCADE
        )
    ]
)
data class NoteTag(
    @ColumnInfo(name = "note_id") val noteId: Int,
    @ColumnInfo(name = "tag_id") val tagId: Int,
)

class Converters {
    @TypeConverter
    fun fromDateToLong(date: Instant): Long {
        return date.epochSecond
    }

    @TypeConverter
    fun fromLongToDate(value: Long): Instant {
        return Instant.ofEpochSecond(value)
    }
}