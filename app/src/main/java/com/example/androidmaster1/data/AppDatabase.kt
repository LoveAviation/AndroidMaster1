package com.example.androidmaster1.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.androidmaster1.domain.ImageDao
import com.example.androidmaster1.other_classes.image

@Database(entities = [image::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun imageDao(): ImageDao
}