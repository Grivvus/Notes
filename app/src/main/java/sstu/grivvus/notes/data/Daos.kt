package sstu.grivvus.notes.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update


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

@Dao
interface NoteTagDao {
    @Query("select * from tag where id in" +
            "(select tag_id from note_tag where note_id = :id)")
    fun findTagsByNoteId(id: Int): List<Tag>

    @Query("select * from note where id in" +
            "(select note_id from note_tag where tag_id = :id)")
    fun findNotesByTagId(id: Int): List<Note>

    @Query("insert or ignore into note_tag (note_id, tag_id) values (:noteId, :tagId)")
    fun insertOne(noteId: Int, tagId: Int)

    @Query("delete from note_tag where note_id = :noteId and tag_id = :tagId")
    fun deleteOne(tagId: Int, noteId: Int)
}