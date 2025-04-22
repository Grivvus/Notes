package sstu.grivvus.notes

import android.app.AlertDialog
import sstu.grivvus.notes.data.AppNote
import sstu.grivvus.notes.data.DatabaseInterface
import sstu.grivvus.notes.data.tagsToStrings

object DialogBuilder {
    lateinit var builder: AlertDialog.Builder
    fun init(b: AlertDialog.Builder) {
        builder = b
    }

    fun addTagDialog(note: AppNote) {
        val tagsCopy = note.tags.toList()
        val allTags = DatabaseInterface.getAllTags()
        val allTagsText = tagsToStrings(allTags).toTypedArray()
        val activated = BooleanArray(allTags.size, {false})
        allTags.forEachIndexed { i, tag ->
            if (tag in tagsCopy) {
                activated[i] = true
            }
        }
        builder
            .setTitle("Выберите теги")
            .setPositiveButton("Сохранить") { dialog, which ->
                note.tags.clear()
                for (i in activated.indices) {
                    if (activated[i]){
                        note.tags.addLast(allTags[i])
                    }
                }
                if (note.id != null) {
                    DatabaseInterface.updateNoteTags(tagsCopy, note)
                }
            }
            .setNegativeButton("Отмена") { dialog, which ->
                dialog.cancel()
            }
            .setMultiChoiceItems(
                allTagsText, activated
            ) { dialog, which, isChecked ->
                activated[which] = isChecked
            }
        builder.create()
    }

    fun show() {
        builder.show()
    }
}