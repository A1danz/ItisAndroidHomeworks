package ru.kpfu.itis.galeev.android.myapplication.data.db.converters

import androidx.room.TypeConverter
import ru.kpfu.itis.galeev.android.myapplication.data.db.entity.SongEntity
import ru.kpfu.itis.galeev.android.myapplication.model.SongModel

class SongTypeConverter {
    @TypeConverter
    fun fromSongEntityToSongModel(songEntity: SongEntity) : SongModel {
        return SongModel(
            songEntity.id!!,
            songEntity.title,
            songEntity.author,
            songEntity.duration,
            songEntity.text,
            false
        )
    }

    @TypeConverter
    fun fromSongModelToSongEntity(songModel : SongModel) : SongEntity {
        with(songModel) {
            return SongEntity(
                this.id,
                title,
                author,
                duration,
                text
            )
        }

    }
}