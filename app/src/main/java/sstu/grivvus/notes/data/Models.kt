package sstu.grivvus.notes.data

import android.content.Context
import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import androidx.room.Update
import java.sql.Date
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
            onDelete = CASCADE
        ),
        ForeignKey(
            entity = Tag::class,
            parentColumns = ["id"],
            childColumns = ["tag_id"],
            onDelete = CASCADE
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

@Dao
interface NoteDao {
    @Query("select * from note")
    fun getAll(): List<Note>

    @Query("select * from note where id = :id limit 1")
    fun findNote(id: Int): Note

    @Query(
        "select * from note where note.id in (" +
                "select note_id from note_tag where note_tag.tag_id = :tag)"
    )
    fun getNotesByTag(tag: Int): List<Note>

    @Insert
    fun insertOne(note: Note)

    @Update
    fun updateOne(note: Note)

    @Delete
    fun delete(note: Note)
}

@Dao
interface TagDao {
    @Query("select * from tag")
    fun getAll(): List<Tag>

    @Query("select * from tag where id = :id limit 1")
    fun findTag(id: Int): Tag

    @Insert
    fun insertOne(tag: Tag)

    @Update
    fun updateOne(tag: Tag)

    @Delete
    fun delete(tag: Tag)
}

@Database(entities = [Note::class, Tag::class, NoteTag::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun noteDao(): NoteDao
    abstract fun tagDao(): TagDao
}

object DatabaseProvider {
    lateinit var instance: AppDatabase

    fun initDB(context: Context) {
        instance = Room.databaseBuilder(context, AppDatabase::class.java, "notes_db").allowMainThreadQueries().build()
    }

    fun getDB(): AppDatabase {
        return instance
    }

}