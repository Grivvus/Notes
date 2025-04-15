package sstu.grivvus.notes.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Note::class, Tag::class, NoteTag::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun noteDao(): NoteDao
    abstract fun tagDao(): TagDao
    abstract fun noteTagDao(): NoteTagDao
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